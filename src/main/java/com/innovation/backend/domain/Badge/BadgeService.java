package com.innovation.backend.domain.Badge;


import com.innovation.backend.domain.Badge.Badge;
import com.innovation.backend.domain.DailyMission.repository.DailyMissionRepository;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Badge.TagBadge;
import com.innovation.backend.domain.Badge.BadgeRepository;
import com.innovation.backend.domain.Badge.TagBadgeRepository;
import com.innovation.backend.domain.Notification.NotificationService;
import com.innovation.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final TagBadgeRepository tagBadgeRepository;
    private final BadgeRepository badgeRepository;
    private final NotificationService notificationService;
    private final DailyMissionRepository dailyMissionRepository;

    // 뱃지 중복여부
    public boolean hasBadge(Member member,TagBadge tagBadge) {
        if(badgeRepository.existsByMemberAndTagBadge(member,tagBadge)){
            return false;
        }
        return true;
    }

    // WelcomeBadge 획득
    public void getWelcomeBadge(UserDetailsImpl userDetails, String badgeName) {
        Member member = userDetails.getMember();
        Long id = userDetails.getMember().getId();
        TagBadge tagBadge = tagBadgeRepository.findByBadgeName(badgeName);
        if(hasBadge(member,tagBadge)){
            Badge badge = new Badge(tagBadge,member);
            badgeRepository.save(badge);
            notificationService.sendEvent(id,"WelcomeBadge를 획득하였습니다.");
        }
    }

    // MissionChallenger 획득
    public void getMissionChallengerBadge(UserDetailsImpl userDetails, String badgeName) {
        Member member = userDetails.getMember();
        Long id = userDetails.getMember().getId();
        TagBadge tagBadge = tagBadgeRepository.findByBadgeName(badgeName);
        int missionCount = dailyMissionRepository.countByMember(member);
        if(hasBadge(member,tagBadge) && missionCount >= 5) {
            Badge badge = new Badge(tagBadge,member);
            badgeRepository.save(badge);
            notificationService.sendEvent(id,"MissionChallengerBadge를 획득하였습니다.");
        }
    }
}
