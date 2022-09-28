package com.innovation.backend.service;

import com.innovation.backend.dto.response.LikeResultResponseDto;
import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.HeartBoard;
import com.innovation.backend.entity.HeartMeeting;
import com.innovation.backend.entity.Member;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.BoardRepository;
import com.innovation.backend.repository.HeartBoardRepository;
import com.innovation.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final HeartBoardRepository heartBoardRepository;
    //모임 좋아요
    @Transactional
    public LikeResultResponseDto addBoardLike(UserDetailsImpl userDetails, Long boardId) {
        String userId = userDetails.getUsername();
        Member member = memberRepository.findByEmail(userId).orElseThrow();
        Board board = boardRepository.findById(boardId).orElseThrow();
        int likeNums = board.getHeartBoardNums();
        HeartBoard heartBoard = new HeartBoard(member,board);
        if (isMeetingLike(member, board)) {
            heartBoardRepository.deleteByMemberAndBoard(member, board);
            board.addBoardLike(likeNums - 1);
            return new LikeResultResponseDto("게시글 좋아요 취소!");
        } else {
            heartBoardRepository.save(heartBoard);
            board.addBoardLike(likeNums + 1);
            return new LikeResultResponseDto("게시글 좋아요 성공!");
        }
    }

    // 좋아요 중복방지
    private boolean isMeetingLike(Member member, Board board) {
        return heartBoardRepository.existsByMemberAndBoard(member, board);
    }
}
