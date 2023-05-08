package com.groot.backend.controller;

import com.groot.backend.dto.request.PlantSearchDTO;
import com.groot.backend.dto.response.PlantDetailDTO;
import com.groot.backend.dto.response.PlantIdentificationDTO;
import com.groot.backend.dto.response.PlantThumbnailDTO;
import com.groot.backend.service.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/plants")
@Tag(name="[PLANT] Plant API")
@RequiredArgsConstructor
@Slf4j
public class PlantController {
    private final Logger logger = LoggerFactory.getLogger(PlantController.class);
    private final PlantService plantService;

    @GetMapping("/names")
    @Operation(summary = "Get plant namne list", description = "no params required")
    public ResponseEntity<Map<String, Object>> getPlantNames() {
        logger.info("Get plant name list");
        Map<String, Object> result = new HashMap<>();

        List<String> nameList = plantService.getNameList();

        logger.info("plant name list : {} found", nameList.size());
        if(nameList.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            result.put("msg", "식물 이름 조회에 성공하였습니다.");
            result.put("nameList", nameList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @GetMapping("/{plantId}")
    @Operation(summary = "Get plant detail info", description = "requires platn id")
    public ResponseEntity<Map<String, Object>> plantDetail(@PathVariable Long plantId) {
        logger.info("Get plant detail info : {}", plantId);
        Map<String, Object> result = new HashMap<>();

        PlantDetailDTO plantDetailDTO = plantService.plantDetail(plantId);

        if(plantDetailDTO == null) {
            result.put("msg", "존제하지 않는 식물입니다.");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        else {
            result.put("msg", "식물 정보 조회에 성공하였습니다.");
            result.put("plant", plantDetailDTO);

            return new ResponseEntity<>(result, HttpStatus.OK);

        }
    }

    @GetMapping()
    @Operation(summary = "Get plant thumbnail", description = "")
    public ResponseEntity<Map<String, Object>> plantList(PlantSearchDTO plantSearchDTO) {
        logger.info("Get plant list : {}", plantSearchDTO);
        logger.info("param : name : {}", plantSearchDTO.getName());
        logger.info("param : diff : {}", plantSearchDTO.getDifficulty());
        logger.info("param : lux : {}", plantSearchDTO.getLux());
        logger.info("param : growth : {}", plantSearchDTO.getGrowth());
        logger.info("page no : {}", plantSearchDTO.getPage());

        Map<String, Object> result = new HashMap<>();
        List<PlantThumbnailDTO> list = plantService.plantList(plantSearchDTO);

        if(list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        result.put("msg", "식물 목록 조회에 성공했습니다.");
        result.put("plants", list);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/identify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Get plant info by photo", description = "")
    public ResponseEntity<Map<String, Object>> identifyPlant(@RequestPart("file") MultipartFile multipartFile) {
        logger.info("Identify plant : {}", multipartFile.getOriginalFilename());
        Map<String, Object> result = new HashMap<>();
        PlantIdentificationDTO plantIdentificationDTO;

        try{
            plantIdentificationDTO = plantService.identifyPlant(multipartFile);

        } catch (InvalidContentTypeException e) {
            result.put("msg", "올바르지 않은 파일 형식입니다.");
            return new ResponseEntity<>(result, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } catch (IOException e) {
            result.put("msg", "Failed to create file");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(plantIdentificationDTO == null) {
            result.put("msg", "등록되지 않은 식물입니다.");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        result.put("msg", "식물 식별에 성공했습니다.");
        result.put("plant", plantIdentificationDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}