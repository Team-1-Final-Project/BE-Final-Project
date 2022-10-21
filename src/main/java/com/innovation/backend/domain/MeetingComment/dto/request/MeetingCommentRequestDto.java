package com.innovation.backend.domain.MeetingComment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingCommentRequestDto {
    private Long meetingId;
    private String content;
}
