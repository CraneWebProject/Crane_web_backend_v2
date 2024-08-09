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
import com.sch.crane.cranewebbackend_v2.Service.Service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

//    @GetMapping("/test")
//    public void testboard(){
//        boardService.BoardTest();
//    }

    @PreAuthorize("hasRole('ADMIN') and hasRole('MANAGER') and hasRole('MEMBER') and hasRole('GRADUATED')")
    @PostMapping("/createBoard")
    public ResponseEntity<Board> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.createBoard(boardRequestDto));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long boardId) {
        boardService.increaseBoardView(boardId);
        return ResponseEntity.ok(boardService.readBoardById(boardId));
    } // 순서 괜찮은지

    //axios 요청에서는 get에 body를 담아 보낼 수 없음.
    //따라서 header의 param에 정보를 담아오고 읽어오는 방식으로 수정.
    @GetMapping("/list")
    public ResponseEntity<List<BoardResponseDto>>
                getBoardByCategory(HttpServletRequest request) {
        String stringCategory = request.getParameter("BoardCategory");
        BoardCategory boardCategory =  BoardCategory.valueOf(stringCategory);
        return ResponseEntity.ok(boardService.readBoardByCategory(boardCategory));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BoardResponseDto>> getBoardByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(boardService.readBoardByUser(userId));
    }

    @DeleteMapping("/deleteBoard/{boardId}")
    public Long deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.delBoard(boardId);
        return boardId;
    }

    @PutMapping("/updateBoard/{boardId}")
    public ResponseEntity<Board> updateBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.editBoard(boardId, boardRequestDto));
    }

    @PostMapping("/createComment")
    public ResponseEntity<Comment>
                createComment(@PathVariable Long boardId,@RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(boardService.writeComment(boardId, commentRequestDto));
    }

    @PutMapping("/updateComment/{commentId}")
    public ResponseEntity<Comment>
                updateComment(@PathVariable("commentId")Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(boardService.editComment(commentId, commentRequestDto));
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
        return ResponseEntity.ok(boardService.createAttachmentFile(boardId, attachmentFileRequestDto));
    }

    @PutMapping("/editAttachmentFile/{attachmentFileId}")
    public ResponseEntity<AttachmentFile>
            updateAttachmentFile(@PathVariable("attachmentFileId") Long attachmentFileId, @RequestBody AttachmentFileRequestDto attachmentFileRequestDto) {
        return ResponseEntity.ok(boardService.editAttachmentFile(attachmentFileId, attachmentFileRequestDto));
    }

    @DeleteMapping("/deleteAttachmentFile/{attachmentFileId}")
    public Long deleteAttachmentFile(@PathVariable("attachmentFileId") Long attachmentFileId) {
        boardService.deleteAttachmentFile(attachmentFileId);
        return attachmentFileId;
    }

//    @GetMapping("/{attachmentFileId}")
//    public ResponseEntity<AttachmentFileRequestDto> getAttachmentFile(@PathVariable("attachmentFileId")Long attachmentFileId) {
//        return ResponseEntity.ok(boardService.readAttachmentFile(attachmentFileId));
//    }

}
