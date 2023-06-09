package com.groot.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.groot.backend.dto.response.PlanDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="plans")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "pot_id", updatable = false, insertable = false)
    private Long potId;

    @Column(name = "user_id", updatable = false, insertable = false)
    private Long userPK;

    @Column(name = "code")
    private Integer code;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Column(name = "done")
    private boolean done;

    @Column(name = "diary_id", updatable = false, insertable = false)
    private Long diaryId;

    @ManyToOne(targetEntity = PotEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "pot_id")
    @JsonManagedReference
    private PotEntity potEntity;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private UserEntity userEntity;

    @ManyToOne(targetEntity = DiaryEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "diary_id")
    @JsonManagedReference
    private DiaryEntity diaryEntity;

    public List<PlanDTO> toPlanDTOList(List<PlanEntity> planEntities){
        List<PlanDTO> dtoList = new ArrayList<>();
        for(PlanEntity plan: planEntities){
            PlanDTO result = PlanDTO.builder()
                    .planId(plan.getId())
                    .code(plan.getCode())
                    .imgPath(plan.getPotEntity().getImgPath())
                    .potName(plan.getPotEntity().getName())
//                    .dateTime(plan.getDateTime())
                    .potId(plan.getPotId())
                    .done(plan.isDone())
                    .userPK(plan.getUserPK())
                    .build();
            dtoList.add(result);
        }

        return dtoList;
    }
}
