package com.innovation.backend.scheduler;


import com.innovation.backend.entity.Meeting;
import com.innovation.backend.enums.MeetingStatus;
import com.innovation.backend.repository.MeetingRepository;
import java.time.LocalDate;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MeetingScheduler {

  private final MeetingRepository meetingRepository;

  // 초, 분, 시, 일, 월, 주 순서
  @Scheduled(cron = "0 1 0 * * *") // 매일 밤 12시 1분
  @Transactional
  public void changeMeetingStatus(){

   List<Meeting> meetingList = meetingRepository.findAll();


    for (Meeting meeting : meetingList){
      if(meeting.getJoinEndDate().isBefore(LocalDate.now())){
        meeting.setMeetingStatus(MeetingStatus.PASS_DEADLINE);
      }if(meeting.getMeetingEndDate().isBefore(LocalDate.now())){
        meeting.setMeetingStatus(MeetingStatus.COMPLETED_MEETING);
      }
    }

    for (Meeting meeting : meetingList){
      if(meeting.getJoinStartDate().equals(LocalDate.now())){
        meeting.setMeetingStatus(MeetingStatus.CAN_JOIN);
      }
    }
  }
}
