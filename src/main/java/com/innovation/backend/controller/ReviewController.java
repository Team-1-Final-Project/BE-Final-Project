package com.innovation.backend.controller;

import com.innovation.backend.dto.request.ReviewRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.dto.response.ReviewResponseDto;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  //후기 생성
  @PostMapping("/review/{meetingId}")
  public ResponseDto<ReviewResponseDto> createReview(@PathVariable("meetingId") Long meetingId, @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestPart("data") ReviewRequestDto requestDto,
      @RequestPart(required = false) MultipartFile image){
    return ResponseDto.success(reviewService.createReview(meetingId,requestDto,userDetails,image));
  }

  //후기 수정
  @PutMapping("/review/{reviewId}")
  public ResponseDto<String> updateReview (@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("reviewId") Long reviewId, @RequestBody ReviewRequestDto requestDto){
    reviewService.updateReview(reviewId, requestDto,userDetails);
    return ResponseDto.success("후기가 수정되었습니다.");
  }

  //후기 사진 수정

  //후기 삭제
  @DeleteMapping ("/review/{reviewId}")
  public ResponseDto<String> deleteReview (@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("reviewId") Long reviewId){
    reviewService.deleteReview(reviewId, userDetails);
    return ResponseDto.success("후기를 삭제하였습니다.");
  }

  //후기 전체 조회
  @GetMapping("/review")
  public ResponseDto<List<ReviewResponseDto>> getAllReview(){
    return ResponseDto.success(reviewService.getAllReview());
  }

  //후기 상세 조회
  @GetMapping("/review/{reviewId}")
  public ResponseDto<ReviewResponseDto> getReview (@PathVariable("reviewId") Long reviewId){
    return ResponseDto.success(reviewService.getReview(reviewId));
  }

  //모임별 후기 조회
  @GetMapping("/review/meeting/{meetingId}")
  public ResponseDto<List<ReviewResponseDto>> getReviewByMeeting (@PathVariable("meetingId") Long meetingId){
    return ResponseDto.success(reviewService.getReviewByMeeting(meetingId));
  }

  //내가 작성한 후기 조회
  @GetMapping("/mypage/review")
  public ResponseDto<List<ReviewResponseDto>> getMyReview (@AuthenticationPrincipal UserDetailsImpl userDetails){
    return ResponseDto.success(reviewService.getMyReview(userDetails));
  }
}
