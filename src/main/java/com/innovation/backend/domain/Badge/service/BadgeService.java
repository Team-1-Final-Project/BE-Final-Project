package com.innovation.backend.domain.Badge.service;


import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Badge.domain.TagBadge;
import com.innovation.backend.domain.Badge.repository.BadgeRepository;
import com.innovation.backend.domain.Badge.repository.TagBadgeRepository;
import com.innovation.backend.domain.Board.repository.BoardRepository;
import com.innovation.backend.domain.Board.repository.HeartBoardRepository;
import com.innovation.backend.domain.Comment.repository.CommentRepository;
import com.innovation.backend.domain.DailyMission.repository.DailyMissionRepository;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Notification.Service.NotificationService;
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
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final HeartBoardRepository heartBoardRepository;

    // 뱃지 중복여부
    public boolean hasBadge(Member member, TagBadge tagBadge) {
        if(badgeRepository.existsByMemberAndTagBadge(member,tagBadge)){
            return false;
        }
        return true;
    }

    // 뱃지 공통 로직 메소드
    public void commonBadge(UserDetailsImpl userDetails, String badgeName) {
        Member member = userDetails.getMember();
        Long id = userDetails.getMember().getId();
        TagBadge tagBadge = tagBadgeRepository.findByBadgeName(badgeName);
        if(hasBadge(member,tagBadge)){
            Badge badge = new Badge(tagBadge,member);
            badgeRepository.save(badge);
            notificationService.sendEvent(id,badgeName+"를 획득하였습니다.");
        }
    }

    // WelcomeBadge 획득 - 첫 로그인시
    public void getWelcomeBadge(UserDetailsImpl userDetails, String badgeName) {
        commonBadge(userDetails, badgeName);
    }

    // WelcomeCommunityBadge 획득 - 첫 게시글 작성시
    public void getWelcomeCommunityBadge(UserDetailsImpl userDetails, String badgeName) {
        int boardCount = boardRepository.countByMember(userDetails.getMember());
        // CommunityActivist_1 Badge 획독 - 작성한 게시글 5개이상 보유시
        if(boardCount >= 5) {
            badgeName = "Community Activist_1 Badge";
        }
        commonBadge(userDetails, badgeName);
    }

    // CommunityActivist_2Badge 획독 - 작성한 댓글 5개이상 보유시
    public void getCommunityActivist_2Badge(UserDetailsImpl userDetails, String badgeName) {
        int commentCount = commentRepository.countByMember(userDetails.getMember());
        if(commentCount >= 5) {
            commonBadge(userDetails, badgeName);
        }
    }

    // HeartMakerBadge 획독 - 좋아요한 게시글 5개이상시
    public void getHeartMakerBadge(UserDetailsImpl userDetails, String badgeName) {
        int heartCount = heartBoardRepository.countByMember(userDetails.getMember());
        if(heartCount >= 5) {
            commonBadge(userDetails, badgeName);
        }
    }

    // WelcomeMeetingBadge 획득 - 첫 모임 생성시
    public void getWelcomeMeetingBadge(UserDetailsImpl userDetails, String badgeName) {
        commonBadge(userDetails, badgeName);
    }

    // MissionStarterBadge 획득 - 첫 데일리미션 성공시
    public void getMissionStarterBadge(UserDetailsImpl userDetails, String badgeName) {
        int missionCount = dailyMissionRepository.countByMember(userDetails.getMember());
        // MissionChallengerBadge 획득 - 데일리미션 5회이상 성공시
        if(missionCount >=5){
            badgeName = "MissionChallenger Badge";
        }
        commonBadge(userDetails, badgeName);

    }


}
