package com.innovation.backend.domain.Notification.controller;



import com.innovation.backend.domain.Notification.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id) {
        return notificationService.subscribe(id);
    }

    @GetMapping("/test/{id}")
    public void test(@PathVariable Long id){
        notificationService.sendEvent(id,"연결 성공입니다");
    }

}
