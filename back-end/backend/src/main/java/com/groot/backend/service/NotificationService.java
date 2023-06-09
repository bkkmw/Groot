package com.groot.backend.service;

import com.groot.backend.dto.request.AlarmDTO;
import com.groot.backend.entity.NotificationEntity;
import com.groot.backend.entity.UserAlarmEntity;
import com.groot.backend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(Long userId, String lastEventId);
    void send(UserEntity receiver, String content, String url, String page, Long contentId) ;
    Long readCheck(Long notificationId);
    Page<NotificationEntity> notificationList(Long userPK, Integer page, Integer size);
    UserAlarmEntity settingNotification(AlarmDTO alarmDTO, Long userPK);
}
