package com.innovation.backend.domain.Review.controller;

import com.innovation.backend.domain.Review.dto.ReviewRequestDto;
import com.innovation.backend.domain.Review.dto.ReviewResponseDto;
import com.innovation.backend.domain.Review.service.ReviewService;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
      @PathVariable("reviewId") Long reviewId, @RequestPart("data") ReviewRequestDto requestDto,@RequestPart(required = false) MultipartFile image){
    reviewService.updateReview(reviewId, requestDto,userDetails,image);
    return ResponseDto.success("후기가 수정되었습니다.");
  }

  //후기 삭제
  @DeleteMapping ("/review/{reviewId}")
  public ResponseDto<String> deleteReview (@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("reviewId") Long reviewId){
    reviewService.deleteReview(reviewId, userDetails);
    return ResponseDto.success("후기를 삭제하였습니다.");
  }

  //후기 전체 조회
  @GetMapping("/review")
  public ResponseDto<Page<ReviewResponseDto>> getAllReview(@PageableDefault(size = 10) Pageable pageable){
    return ResponseDto.success(reviewService.getAllReview(pageable));
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
}
