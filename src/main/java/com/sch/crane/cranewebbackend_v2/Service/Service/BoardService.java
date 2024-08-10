package com.sch.crane.cranewebbackend_v2.Service.Service;

import com.sch.crane.cranewebbackend_v2.Data.DTO.AttachmentFile.AttachmentFileRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.AttachmentFile.AttachmentFileResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Board.BoardRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Board.BoardResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Comment.CommentRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Comment.CommentResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.AttachmentFile.AttachmentRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Board.BoardRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Comment.CommentRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.AttachmentFile;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Comment;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardState;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Auth.JWT.UserDetailsImpl;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.AbstractAuditable_;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    @Transactional
    public Board createBoard(BoardRequestDto boardRequestDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = ((org.springframework.security.core.userdetails.User)principal).getUsername();

        Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
        if(optionalUser.isEmpty()){
            throw new BadCredentialsException("사용자 인증 에러");
        }

        Board board = Board.builder()
                .boardTitle(boardRequestDto.getBoardTitle())
                .boardCategory(boardRequestDto.getBoardCategory())
                .boardContents(boardRequestDto.getBoardContents())
                .boardView(0)
                .user(optionalUser.get())
                .boardState(BoardState.DEFAULT)
                .build();

        return boardRepository.save(board);
    } // userName 추가

//    @Transactional
//    public void BoardTest(){
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String userEmail = ((org.springframework.security.core.userdetails.User)principal).getUsername();
//        System.out.println(principal);
//        System.out.println(userEmail);
//    }

    @Transactional
    public Board editBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NoSuchElementException("보드가 존재하지 않습니다."));

        board.updateBoard(boardRequestDto.getBoardTitle(), boardRequestDto.getBoardContents(), boardRequestDto.getBoardCategory());
        return boardRepository.save(board);
    }

    @Transactional
    public void delBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new NoSuchElementException("보드가 존재하지 않습니다."));
        List<Comment> commentList = commentRepository.findByBId(boardId);
        for(Comment c : commentList)
        {
            commentRepository.deleteById(c.getCid());
        }
        if(!commentList.isEmpty())
        {
            throw new EntityExistsException("댓글이 모두 삭제되지 않았습니다");
        }
        boardRepository.delete(board);
        List<AttachmentFile> attachmentFileList = attachmentRepository.findAttachmentFileByBoardId(boardId);
        for(AttachmentFile a : attachmentFileList)
        {
            attachmentRepository.deleteById(a.getAid());
        }
        if(!attachmentFileList.isEmpty())
        {
            throw new EntityExistsException("첨부파일이 존재하지 않습니다.");
        }
    }

    @Transactional
    public BoardResponseDto readBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new NoSuchElementException("유저가 존재하지 않습니다.")
        );
        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .bid(boardId)
                .boardTitle(board.getBoardTitle())
                .boardContents(board.getBoardContents())
                .boardView(board.getBoardView())
                .boardCategory(board.getBoardCategory())
                .uid(board.getUser().getUid())
                .userName(board.getUser().getUserName())
                .createdDate(board.getCreatedDate())
                .build();
        return boardResponseDto;
    }

    @Transactional
    public List<BoardResponseDto> readBoardByCategory(BoardCategory boardCategory) {
        List<Board> boardList = boardRepository.findBoardByCategory(boardCategory);
        if(boardList.isEmpty())
        {
            throw new NoSuchElementException("보드가 존재하지 않습니다");
        }
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for(Board b : boardList)
        {
            BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                    .bid(b.getBid())
                    .boardTitle(b.getBoardTitle())
                    .boardCategory(b.getBoardCategory())
                    .boardView(b.getBoardView())
                    .boardContents(b.getBoardContents())
                    .uid(b.getUser().getUid())
                    .userName(b.getUser().getUserName())
                    .createdDate(b.getCreatedDate())
                    .build();

            boardResponseDtoList.add(boardResponseDto);
        }
        return boardResponseDtoList;
    }

    @Transactional
    public List<BoardResponseDto> readBoardByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new NoSuchElementException("유저가 존재하지 않습니다.")
        );
        List<Board> boardList = boardRepository.findBoardByUser(user);
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for(Board b : boardList)
        {
            BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                    .boardTitle(b.getBoardTitle())
                    .boardView(b.getBoardView())
                    .boardContents(b.getBoardContents())
                    .userName(b.getUser().getUserName())
                    .createdDate(b.getCreatedDate())
                    .build();
            boardResponseDtoList.add(boardResponseDto);
        }
        return boardResponseDtoList;
    }

    @Transactional
    public Comment writeComment(Long boardId, CommentRequestDto commentRequestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
            ()-> new NoSuchElementException("보드가 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .commentContents(commentRequestDto.getCommentContents())
                .board(board)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment editComment(Long commentId, CommentRequestDto commentRequestDto) {
       Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new NoSuchElementException("코멘트가 존재하지 않습니다")
        );

       comment.updateComment(commentRequestDto.getCommentContents());
       return commentRepository.save(comment);
    }

    @Transactional
    public List<CommentResponseDto> readCommentByBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new NoSuchElementException("보드가 존재하지 않습니다")
        );
        List<Comment> commentList = commentRepository.findByBId(boardId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment c : commentList)
        {
            CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                    .cid(c.getCid())
                    .commentContents(c.getCommentContents())
                    .board(c.getBoard())
                    .uid(c.getUser().getUid())
                    .userName(c.getUser().getUserName())
                    .build();
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }

    @Transactional
    public void delComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new NoSuchElementException("코멘트가 존재하지 않습니다.")
        );
        commentRepository.delete(comment);
    }

    @Transactional
    public int increaseBoardView(Long boardId) {
        return boardRepository.increaseView(boardId);
    }

    @Transactional
    public AttachmentFile createAttachmentFile(Long boardId, AttachmentFileRequestDto attachmentFileRequestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new NoSuchElementException("보드가 존재하지 않습니다")
        );
        AttachmentFile attachmentFile = AttachmentFile.builder()
                .attachTitle(attachmentFileRequestDto.getAttachTitle())
                .attachPath(attachmentFileRequestDto.getAttachPath())
                .board(boardRepository.findById(boardId).orElseThrow()) // *
                .build();
       return  attachmentRepository.save(attachmentFile);
    }

    @Transactional
    public AttachmentFile editAttachmentFile(Long aId, AttachmentFileRequestDto attachmentFileRequestDto) {
        AttachmentFile attachmentFile = attachmentRepository.findById(aId).orElseThrow(
                () -> new NoSuchElementException("첨부파일이 존재하지 않습니다.")
        );
        attachmentFile.updateAttachmentFile(attachmentFileRequestDto.getAttachTitle(), attachmentFileRequestDto.getAttachPath());

        return attachmentRepository.save(attachmentFile);
    }

    @Transactional
    public void deleteAttachmentFile(Long aId) {
        AttachmentFile attachmentFile = attachmentRepository.findById(aId).orElseThrow(
                () -> new NoSuchElementException("첨부파일이 존재하지 않습니다.")
        );
        attachmentRepository.deleteById(aId);
    }

    @Transactional
    public AttachmentFileRequestDto readAttachmentFile(Long attachmentFileId) {
        AttachmentFile attachmentFile = attachmentRepository.findById(attachmentFileId).orElseThrow(
                ()-> new NoSuchElementException("첨부파일이 존재하지 않습니다.")
        );
        AttachmentFileRequestDto attachmentFileRequestDto= AttachmentFileRequestDto.builder()
                .attachPath(attachmentFile.getAttachPath())
                .attachTitle(attachmentFile.getAttachTitle())
                .build();
        return attachmentFileRequestDto;
    }

}
