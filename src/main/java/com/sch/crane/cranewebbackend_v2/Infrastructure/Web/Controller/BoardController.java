package com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller;

import com.sch.crane.cranewebbackend_v2.Data.DTO.AttachmentFile.AttachmentFileRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.AttachmentFile.AttachmentFileResponseDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Board.BoardPageDto;
import com.sch.crane.cranewebbackend_v2.Data.DTO.Board.BoardPageListDto;
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
import org.springframework.data.domain.Page;
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PostMapping("/createBoard")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.createBoard(boardRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long boardId) {
        boardService.increaseBoardView(boardId);
        return ResponseEntity.ok(boardService.readBoardById(boardId));
    } // 순서 괜찮은지

    //get에 body를 담아 보낼 수 없음.
    //따라서 header의 param에 정보를 담아오고 읽어오는 방식으로 수정.
    @GetMapping("/list")
    public ResponseEntity<BoardPageListDto> getBoardByCategory(
            @RequestParam BoardCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
//        String stringCategory = request.getParameter("BoardCategory");
//        int page = Integer.parseInt(request.getParameter("page").toString());

        if(category == BoardCategory.GALLERY){
            size = 12;
        }

        BoardPageDto dto = BoardPageDto.builder()
                .page(page)
                .size(size)
                .boardCategory(category)
                .sort(sort)
                .build();


        return ResponseEntity.ok(boardService.readBoardByCategory(dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BoardResponseDto>> getBoardByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(boardService.readBoardByUser(userId));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @DeleteMapping("/deleteBoard/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.delBoard(boardId);
        return ResponseEntity.ok(boardId);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PutMapping("/updateBoard/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardRequestDto boardRequestDto) {
        return ResponseEntity.ok(boardService.editBoard(boardId, boardRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PostMapping("/createComment")
    public ResponseEntity<Comment>
                createComment(@PathVariable Long boardId,@RequestBody CommentRequestDto commentRequestDto) {
        return ResponseEntity.ok(boardService.writeComment(boardId, commentRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
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

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @DeleteMapping("/deleteComment/{commentId}")
    public Long deleteComment(@PathVariable("commentId")Long commentId) {
        boardService.delComment(commentId);
        return commentId;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PostMapping("/attachmentFile")
    public ResponseEntity<AttachmentFile> createAttachmentFile(@RequestBody Long boardId, AttachmentFileRequestDto attachmentFileRequestDto) {
        return ResponseEntity.ok(boardService.createAttachmentFile(boardId, attachmentFileRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
    @PutMapping("/editAttachmentFile/{attachmentFileId}")
    public ResponseEntity<AttachmentFile>
            updateAttachmentFile(@PathVariable("attachmentFileId") Long attachmentFileId, @RequestBody AttachmentFileRequestDto attachmentFileRequestDto) {
        return ResponseEntity.ok(boardService.editAttachmentFile(attachmentFileId, attachmentFileRequestDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('MEMBER') or hasRole('GRADUATED')")
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
