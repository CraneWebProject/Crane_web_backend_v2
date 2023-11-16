package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.AttachmentFile.AttachmentFileRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.AttachmentFile.AttachmentFileResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Board.BoardRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Board.BoardResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Comment.CommentRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Comment.CommentResponseDto;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.AttachmentFile;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Comment;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.Status.StatusCode;
import com.sch.crane.cranewebbackend_v2.Service.Service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/createBoard")
    public ResponseEntity<Board> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        Board board = boardService.createBoard(boardRequestDto);
        return ResponseEntity.status(StatusCode.CREATED).body(board);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long boardId) {
        boardService.increaseBoardView(boardId);
        BoardResponseDto boardResponseDto = boardService.readBoardById(boardId);
        return ResponseEntity.status(StatusCode.OK).body(boardResponseDto);
    } // 순서 괜찮은지

    @GetMapping("/list/{boardCategory}")
    public ResponseEntity<List<BoardResponseDto>> getBoardByCategory(@PathVariable("boardCategory")BoardCategory boardCategory) {
        List<BoardResponseDto> boardResponseDtoList = boardService.readBoardByCategory(boardCategory);
        return ResponseEntity.status(StatusCode.OK).body(boardResponseDtoList);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BoardResponseDto>> getBoardByUser(@PathVariable("userId") Long userId) {
        List<BoardResponseDto> boardResponseDtoList = boardService.readBoardByUser(userId);
        return ResponseEntity.status(StatusCode.OK).body(boardResponseDtoList);
    }

    @DeleteMapping("/deleteBoard/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.delBoard(boardId);
        return ResponseEntity.status(StatusCode.OK).body("보드 삭제됨.");
    }

    @PutMapping("/updateBoard/{boardId}")
    public ResponseEntity<Board> updateBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardRequestDto boardRequestDto) {
        Board board = boardService.editBoard(boardId, boardRequestDto);
        return ResponseEntity.status(StatusCode.OK).body(board);
    }

    @PostMapping("/createComment")
    public ResponseEntity<Comment> createComment(@PathVariable Long boardId,@RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = boardService.writeComment(boardId, commentRequestDto);
        return ResponseEntity.status(StatusCode.CREATED).body(comment);
    }

    @PutMapping("/updateComment/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("commentId")Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = boardService.editComment(commentId, commentRequestDto);
        return ResponseEntity.status(StatusCode.OK).body(comment);
    }

/*   @GetMapping("/")
    public ResponseEntity<List<CommentResponseDto>> getComment(@PathVariable("boardId") Long boardId)
    {
        return ResponseEntity.ok(boardService.readCommentByBoard(boardId));
    } // */

    @DeleteMapping("/deleteComment/{commentId}")
    public Long deleteComment(@PathVariable("commentId")Long commentId) {
        boardService.delComment(commentId);
        return commentId;
    }

    @PostMapping("/attachmentFile")
    public ResponseEntity<AttachmentFile> createAttachmentFile(@RequestBody Long boardId, AttachmentFileRequestDto attachmentFileRequestDto) {
        AttachmentFile attachmentFile = boardService.createAttachmentFile(boardId, attachmentFileRequestDto);
        return ResponseEntity.status(StatusCode.CREATED).body(attachmentFile);

    }

    @PutMapping("/editAttachmentFile/{attachmentFileId}")
    public ResponseEntity<AttachmentFile>
            updateAttachmentFile(@PathVariable("attachmentFileId") Long attachmentFileId, @RequestBody AttachmentFileRequestDto attachmentFileRequestDto) {
        AttachmentFile attachmentFile = boardService.editAttachmentFile(attachmentFileId, attachmentFileRequestDto);
        return ResponseEntity.status(StatusCode.OK).body(attachmentFile);
    }

    @DeleteMapping("/deleteAttachmentFile/{attachmentFileId}")
    public Long deleteAttachmentFile(@PathVariable("attachmentFileId") Long attachmentFileId) {
        boardService.deleteAttachmentFile(attachmentFileId);
        return attachmentFileId;
    }

    @GetMapping("/{attachmentFileId}")
    public ResponseEntity<AttachmentFileRequestDto> getAttachmentFile(@PathVariable("attachmentFileId")Long attachmentFileId) {
        AttachmentFileRequestDto attachmentFileRequestDto = boardService.readAttachmentFile(attachmentFileId);
        return ResponseEntity.status(StatusCode.OK).body(attachmentFileRequestDto);
    }

}
