package com.sch.crane.cranewebbackend_v2;

import com.sch.crane.cranewebbackend_v2.Data.DTO.Board.BoardRequestDto;
import com.sch.crane.cranewebbackend_v2.Data.Repository.Board.BoardRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.AttachmentFile;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Comment;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardState;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import com.sch.crane.cranewebbackend_v2.Infrastructure.Web.Controller.BoardController;
import com.sch.crane.cranewebbackend_v2.Service.Service.BoardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@WebMvcTest(BoardController.class)
public class BoardTest {

    @Autowired
    BoardController boardController;

    @MockBean
    BoardService boardService;

    Board board = Board.builder()
            .bid(1L)
            .boardTitle("TestBoard1")
            .boardContents("Contents of board1")
            .boardCategory(BoardCategory.FREE)
            .boardState(BoardState.DEFAULT)
            .build();

    User user = User.builder()
            .userEmail("test@test.com")
            .userPassword("1234")
            .userBirth(LocalDateTime.now())
            .userDept("IoT")
            .userName("testName")
            .userPhNum("01000001234")
            .userRole(UserRole.MEMBER)
            .userSession(UserSession.BASS)
            .userStdId("20180000")
            .build();


    Comment comment = Comment.builder()
            .board(board)
            .commentContents("Contents of comment1")
            .user(user)
            .build();

    AttachmentFile attachmentFile = AttachmentFile.builder()
            .board(board)
            .attachTitle("AttachmentFile1")
            .attachPath("i13277578859")
            .build();
    @Test
    @DisplayName("Board post test")
    public void createBoardTest()
    {
        Assertions.assertThat(board.getBoardTitle()).isEqualTo("TestBoard1");
        Assertions.assertThat(board.getBoardContents()).isEqualTo("Contents of board1");
        Assertions.assertThat(board.getBoardCategory()).isEqualTo(BoardCategory.FREE);
        Assertions.assertThat(board.getBoardState()).isEqualTo(BoardState.DEFAULT);

    }

    @Test
    @DisplayName("Comment Post Test")
    public void createCommentTest()
    {
        Assertions.assertThat(comment.getCommentContents()).isEqualTo("Contents of comment1");
        Assertions.assertThat(comment.getBoard()).isEqualTo(board);
        Assertions.assertThat(comment.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("AttachmentFile Post Test")
    public void AttachmnetCreateTest()
    {
        Assertions.assertThat(attachmentFile.getAttachPath()).isEqualTo("i13277578859");
        Assertions.assertThat(attachmentFile.getAttachTitle()).isEqualTo("AttachmentFile1");
    }

    @Test
    @DisplayName("Board Edit Test")
    public void BoardEditTest()
    {
        board.updateBoard("Changed Title", "Changed Content", BoardCategory.NOTICE);

        Assertions.assertThat(board.getBoardTitle()).isEqualTo("Changed Title");
        Assertions.assertThat(board.getBoardContents()).isEqualTo("Changed Content");
    }

    @Test
    @DisplayName("Update Comment Test")
    public void BoardDelTest()
    {
        comment.updateComment("Changed Comment");
        Assertions.assertThat(comment.getCommentContents()).isEqualTo("Changed Comment");
    }
}
