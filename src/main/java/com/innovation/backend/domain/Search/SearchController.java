package com.innovation.backend.domain.Search;

import com.innovation.backend.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

  @GetMapping("/testtest")
  public ResponseDto<String> test (){
    System.out.println("hi~");
    return ResponseDto.success("성공입니다.");
  }

}
