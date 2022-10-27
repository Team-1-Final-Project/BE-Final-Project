package com.innovation.backend.domain.Notification.Service;

import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Badge.domain.TagBadge;
import com.innovation.backend.domain.Badge.repository.BadgeRepository;
import com.innovation.backend.domain.Badge.repository.TagBadgeRepository;
import com.innovation.backend.domain.Badge.service.BadgeService;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import com.innovation.backend.domain.Notification.dto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final TagBadgeRepository tagBadgeRepository;
    private final BadgeRepository badgeRepository;

    private static Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();  // multi-thread에서 동시에 작업하기 위한 map 클래스

    private static final Long DEFAULT_TIMEOUT = 60L * 24 * 60 * 1000;  // 타임아웃 하루

    public SseEmitter subscribe(Long id) {

        String userId = String.valueOf(id);
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);// sse 연결 요청에 응답하기 위해 sseEmitter 객체 생성(유호시간)
        log.info("Num of SSE : "+  emitters.keySet().size());

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, userId, "SSE 연결 성공","connect");
        emitters.put(userId, emitter);  // 생성된 emitters를 저장해둠
        emitter.onCompletion(() -> emitters.remove(userId));  // 네트워크 에러
        emitter.onTimeout(() -> emitters.remove(userId));  // 타임아웃

        return emitter;
    }

    public void sendEvent(Long id, String content) {  // 이벤트 발생
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto(id,content);
        String userId = String.valueOf(id);

        if (emitters.containsKey(userId)) {   // sse가 연결된 유저이면
            SseEmitter emitter = emitters.get(userId);
            sendToClient(emitter, userId, notificationResponseDto,"message");
        }
        log.info("Num of SSE : "+  emitters.keySet().size());

    }


    // 알림 전송
    private void sendToClient(SseEmitter emitter, String userId, Object data,String type) {
        try {
            System.out.println("SSE 보내는 중 : " + userId + " " + data.toString());
            emitter.send(SseEmitter.event()
                .id(userId)
                .name(type)
                .data(data));
        } catch (IOException e) {
            emitters.remove(userId);
            e.printStackTrace();
        }
    }
}
