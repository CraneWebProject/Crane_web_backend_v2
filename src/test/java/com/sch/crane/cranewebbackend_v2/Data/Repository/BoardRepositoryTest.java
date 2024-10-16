package com.sch.crane.cranewebbackend_v2.Data.Repository;

import com.sch.crane.cranewebbackend_v2.Data.Repository.Board.BoardRepository;
import com.sch.crane.cranewebbackend_v2.Data.Repository.User.UserRepository;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.Board;
import com.sch.crane.cranewebbackend_v2.Domain.Entity.User;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardCategory;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.BoardState;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserRole;
import com.sch.crane.cranewebbackend_v2.Domain.Enums.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private Board board;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userEmail("test@test.com")
                .userName("testUser")
                .userSession(UserSession.BASS)
                .userStdId("20181537")
                .userPhNum("01025819869")
                .userDept("사물인터넷학과")
                .userBirth(LocalDate.now())
                .userPassword("asdfasdf")
                .userRole(UserRole.ROLE_MEMBER)
                .userTh(37)
                .build();
        userRepository.save(user);


        board = Board.builder()
                .boardTitle("Test Title")
                .boardContents("Test Contents")
                .boardCategory(BoardCategory.FREE)
                .boardState(BoardState.DEFAULT)
                .boardView(0)
                .user(user)
                .build();

        boardRepository.save(board);
    }

    @Test
    @DisplayName("Board ID로 찾기 테스트")
    void findByIdTest() {
        Optional<Board> foundBoard = boardRepository.findById(board.getBid());
        assertThat(foundBoard).isPresent();
        assertThat(foundBoard.get().getBoardTitle()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("Board 카테고리로 찾기 테스트")
    void findBoardByCategoryTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Board> boardPage = boardRepository.findBoardByCategory(BoardCategory.FREE, pageable);

        assertThat(boardPage).isNotEmpty();
        assertThat(boardPage.getContent().get(0).getBoardCategory()).isEqualTo(BoardCategory.FREE);
    }

    @Test
    @DisplayName("User로 Board 찾기 테스트")
    void findBoardByUserTest() {
        List<Board> boardList = boardRepository.findBoardByUserUid(user.getUid());
        assertThat(boardList).isNotEmpty();
        assertThat(boardList.get(0).getUser().getUserName()).isEqualTo("testUser");
    }

    //service 단에서 증가시키는 것으로 대체됨

//    @Test
//    @DisplayName("Board 조회수 증가 테스트")
//    void increaseViewTest() {
//        int updatedCount = boardRepository.increaseView(board.getBid());
//
//        boardRepository.flush(); // DB에 즉시 반영
//
//        assertThat(updatedCount).isEqualTo(1);
//
//
//        Optional<Board> updatedBoard = boardRepository.findById(board.getBid());
//        assertThat(updatedBoard.get().getBoardView()).isEqualTo(1);
//    }


}
