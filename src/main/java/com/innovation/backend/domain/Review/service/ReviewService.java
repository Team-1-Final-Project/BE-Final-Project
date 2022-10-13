package com.innovation.backend.domain.Review.service;

import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Review.domain.Review;
import com.innovation.backend.domain.Review.dto.ReviewRequestDto;
import com.innovation.backend.domain.Review.dto.ReviewResponseDto;
import com.innovation.backend.domain.Review.repository.ReviewRepository;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.enums.MeetingStatus;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.domain.Crew.repository.CrewRepository;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import com.innovation.backend.global.util.S3Upload;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@AllArgsConstructor
public class ReviewService {
  private final MeetingRepository meetingRepository;
  private final ReviewRepository reviewRepository;
  private final S3Upload s3Upload;
  private final CrewRepository crewRepository;
  private final MemberRepository memberRepository;

  //후기 생성
  @Transactional
  public ReviewResponseDto createReview (Long meetingId, ReviewRequestDto requestDto, UserDetailsImpl userDetails,
      MultipartFile image){
    // 모임정보 가져오기
    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    //로그인 유저 정보 가져오기
    Member member = memberRepository.findById(userDetails.getMember().getId())
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));

    //모임마감된 모임인지 검증
    isCompletedMeeting(meeting);

    //가입했던 모임인지 검증
    isJoinedMeeting(member,meeting);

    //이전에 쓴 적 없는지 검증
    isAlreadyWrite(member,meeting);

    String reviewImage = null;
    String reviewThumbImage = null;

    if (image != null && !image.isEmpty()) {
      try {
        reviewImage = s3Upload.uploadFiles(image, "reviews");
        log.info(reviewImage);
        reviewThumbImage = s3Upload.uploadThumbFile(image,"thumbs");
      } catch (IOException e) {
        log.error(e.getMessage());
      }}

    Review review = new Review(requestDto, member, meeting,reviewImage,reviewThumbImage);
    reviewRepository.save(review);
    meeting.getReviews().add(review);
    member.getReviews().add(review);
    return new ReviewResponseDto(review);
  }

  //후기 수정
  @Transactional
  public void updateReview (Long reviewId, ReviewRequestDto requestDto,UserDetailsImpl userDetails, MultipartFile image){
    //해당 후기 찾기
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(()->new CustomErrorException(ErrorCode.NOT_FOUND_REVIEW));
    //로그인 유저 정보 가져오기
    Member member = memberRepository.findById(userDetails.getMember().getId())
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));

    //같은 작성자인지 확인
    isWrittenBy(member,review);

    String reviewImage = review.getReviewImage();
    String reviewThumbImage = review.getReviewThumbImage();

    if(reviewImage != null){
      if(image == null || image.isEmpty()){
        reviewImage = review.getReviewImage();
        reviewThumbImage = review.getReviewThumbImage();
      }else if (!image.isEmpty()) {
        try{
          s3Upload.fileDelete(reviewImage);
          s3Upload.fileDelete(reviewThumbImage);
          reviewImage = s3Upload.uploadFiles(image,"reviews");
          log.info(reviewImage);
          reviewThumbImage = s3Upload.uploadThumbFile(image,"thumbs");
        }catch (IOException e){
          log.error(e.getMessage());
        }}
    }else{
      if(image == null || image.isEmpty()){
        reviewImage = null;
        reviewThumbImage = null;
      }else if (!image.isEmpty()){
        try{
          reviewImage = s3Upload.uploadFiles(image,"reviews");
          reviewThumbImage = s3Upload.uploadThumbFile(image,"thumbs");
        }catch (IOException e){
          log.error(e.getMessage());
        }
      }
    }
    review.updateReview(requestDto,reviewImage,reviewThumbImage);
    reviewRepository.save(review);
  }

  //후기 삭제
  @Transactional
  public void deleteReview (Long reviewId,UserDetailsImpl userDetails){
    //해당 후기 찾기
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(()->new CustomErrorException(ErrorCode.NOT_FOUND_REVIEW));
    //로그인 유저 정보 가져오기
    Member member = memberRepository.findById(userDetails.getMember().getId())
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));

    //같은 작성자인지 확인
    isWrittenBy(member,review);
    reviewRepository.delete(review);
    s3Upload.fileDelete(review.getReviewImage());
    s3Upload.fileDelete(review.getReviewThumbImage());
  }

  //후기 전체 조회 (전체)
  public Page<ReviewResponseDto> getAllReview(Pageable pageable){

    Page<Review> reviews = reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
    List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();

    for(Review review : reviews){
      ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
      reviewResponseDtos.add(reviewResponseDto);
    }
    return new PageImpl<>(reviewResponseDtos, pageable, reviews.getTotalElements());
  }

  //후기 상세 조회
  public ReviewResponseDto getReview (Long reviewId){
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(()->new CustomErrorException(ErrorCode.NOT_FOUND_REVIEW));

    return new ReviewResponseDto(review);
  }

  //후기 모임별 조회
  public List<ReviewResponseDto> getReviewByMeeting (Long meetingId){
    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    List<Review> reviews = meeting.getReviews();
    List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();

    for(Review review : reviews){
      ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
      reviewResponseDtos.add(reviewResponseDto);
    }
    return reviewResponseDtos;
  }

  //작성자가 참여했던 모임인지 확인
  public void isJoinedMeeting (Member member, Meeting meeting){
    Optional<Crew> crewOptional = crewRepository.findByMemberAndMeeting(member,meeting);
    if(crewOptional.isEmpty()){
      throw new CustomErrorException(ErrorCode.NEVER_JOIN);
    }
  }

  // 작성자와 같은 유저인지 확인하기
  public void isWrittenBy (Member member, Review review){
    Member author = review.getMember();
    if(!(member.equals(author))){
      throw new CustomErrorException(ErrorCode.NOT_SAME_MEMBER);
    }
  }

  //이전에 쓴 적 없는지 검증
  public void isAlreadyWrite (Member member,Meeting meeting){
    List<Review> reviews = reviewRepository.findByMember(member);

    for(Review review : reviews){
      if(review.getMeeting() == meeting){
        throw new CustomErrorException(ErrorCode.ONLY_ONE_REVIEW);
      }}}

  //모임마감된 모임인지 검증
  public void isCompletedMeeting (Meeting meeting){
    if (meeting.getMeetingStatus() != MeetingStatus.COMPLETED_MEETING){
      throw new CustomErrorException(ErrorCode.CAN_WRITE_COMPLETED_MEETING);
    }
  }

}
