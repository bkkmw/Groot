package com.groot.backend.controller;


import com.groot.backend.dto.request.PotModifyDTO;
import com.groot.backend.dto.request.PotRegisterDTO;
import com.groot.backend.dto.response.PotDetailDTO;
import com.groot.backend.dto.response.PotListDTO;
import com.groot.backend.service.PotService;
import com.groot.backend.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/pots")
@Tag(name="[POT] Pot API")
@RequiredArgsConstructor
@Slf4j
public class PotController {

    private final PotService potService;
    private final Logger logger = LoggerFactory.getLogger(PotController.class);

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create pot", description = "returns potId")
    public ResponseEntity<Map<String, Object>> createPot(HttpServletRequest request,
                                    @RequestPart("img") @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) MultipartFile multipartFile,
                                    @RequestPart("pot") @Valid @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) PotRegisterDTO potRegisterDTO) {
        logger.info("Create pot : {}", potRegisterDTO.getPotName());
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;

        Long userPK;
        try {
            userPK = JwtTokenProvider.getIdByAccessToken(request);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            logger.info("Failed to parse token : {}", request.getHeader("Authorization"));
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        try {
            Long ret = potService.createPot(userPK, potRegisterDTO, multipartFile);
            status = HttpStatus.CREATED;
            result.put("msg", "화분이 등록되었습니다.");
            result.put("potId", ret);
        } catch (IOException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("msg", "파일 업로드 실패.");
        } catch (NoSuchElementException e) {
            status = HttpStatus.NOT_FOUND;
            result.put("msg", "사용자 또는 식물을 찾을 수 없습니다.");
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("msg", "DB 저장에 실패했습니다.");
        }

        return new ResponseEntity(result, status);
    }

    @GetMapping("")
    @Operation(summary = "Get list of pot", description = "")
    public ResponseEntity<Map<String, Object>> potList(HttpServletRequest request) {

        Long userPK;
        try {
            userPK = JwtTokenProvider.getIdByAccessToken(request);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            logger.info("Failed to parse token : {}", request.getHeader("Authorization"));
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        logger.info("Get pot list of user : {}", userPK);
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;

        try {
            List<PotListDTO> list = potService.potList(userPK);
            status = HttpStatus.OK;
            result.put("pots", list);
            result.put("msg", "화분 목록 조회에 성공했습니다.");

        } catch (NoSuchElementException e) {
            logger.info("Failed to load pot list");
            status = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<>(result, status);
    }

    @GetMapping("/{potId}")
    @Operation(summary = "pot detail")
    public ResponseEntity<Map<String, Object>> potDetail(HttpServletRequest request, @PathVariable Long potId) {

        Long userPK;
        try {
            userPK = JwtTokenProvider.getIdByAccessToken(request);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            logger.info("Failed to parse token : {}", request.getHeader("Authorization"));
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        logger.info("Find pot : {}", potId);
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;

        try {
            PotDetailDTO potDetailDTO = potService.potDetail(userPK, potId);
            result.put("msg", "화분 조회에 성공했습니다.");
            result.put("pot", potDetailDTO.getPot());
            result.put("plant", potDetailDTO.getPlant());
            status = HttpStatus.OK;
        } catch (AccessDeniedException e) {
            status = HttpStatus.FORBIDDEN;
            result.put("msg", "허가되지 않은 접근입니다.");
        } catch (NoSuchElementException e) {
            status = HttpStatus.NOT_FOUND;
            result.put("msg", "화분을 찾을 수 없습니다.");
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            result.put("msg", e.getMessage());
        }

        return new ResponseEntity<>(result, status);
    }

    @PutMapping("/{potId}")
    @Operation(summary = "Modify pot info", description = "Image only")
    public ResponseEntity<Map<String, Object>> modifyPot(
            HttpServletRequest request, @PathVariable Long potId,
            @RequestPart("img") @Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) MultipartFile multipartFile,
            @RequestPart(value = "pot", required = false) @Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) PotModifyDTO potModifyDTO) {

        Long userPK;
        try {
            userPK = JwtTokenProvider.getIdByAccessToken(request);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            logger.info("Failed to parse token : {}", request.getHeader("Authorization"));
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        logger.info("modify pot : {}", potId);
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;

        try {
            String imgPath = potService.modifyPot(userPK, potId, potModifyDTO, multipartFile);
            result.put("msg", "화분 정보 변경에 성공했습니다.");
            result.put("img", imgPath);
            status = HttpStatus.OK;
        } catch (IOException e) {
            result.put("msg", "파일 업로드에 실패했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(result, status);
    }

    @DeleteMapping("/{potId}")
    @Operation(summary = "Delete pot..", description = "")
    public ResponseEntity<Map<String, Object>> deletePot(HttpServletRequest request, @PathVariable Long potId) {

        Long userPK;
        try {
            userPK = JwtTokenProvider.getIdByAccessToken(request);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            logger.info("Failed to parse token : {}", request.getHeader("Authorization"));
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        logger.info("Delete pot : {}", potId);
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;

        try {
            int ret = potService.deletePot(userPK, potId);
            result.put("msg", "성공적으로 삭제 되었습니다.");
            status = HttpStatus.OK;
        } catch (IllegalAccessException e) {
            result.put("msg", "UNAUTHORIZED");
            status = HttpStatus.FORBIDDEN;
        } catch (NoSuchElementException e) {
            result.put("msg", "존재하지 않는 화분입니다.");
            status = HttpStatus.NOT_FOUND;
       } catch (IllegalArgumentException e) {
            result.put("msg", "이미 삭제 된 화분입니다.");
            status = HttpStatus.GONE;
        } catch (Exception e) {
            result.put("msg", e.getStackTrace());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(result, status);
    }
}
