//package com.trip.mymy;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNotSame;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.trip.mymy.controller.BoardController;
//import com.trip.mymy.controller.BookmarkController;
//import com.trip.mymy.dto.BoardDTO;
//import com.trip.mymy.service.BoardService;
//import com.trip.mymy.service.BookmarkService;
//
//@RunWith(SpringRunner.class)
//@ContextConfiguration(locations= {"classpath:testBoard.xml",
//"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
//public class TestBoard {
//	@Autowired BoardController bc;
//	@Autowired BoardService bs;
//	@Autowired BookmarkController bmc;
//	@Autowired BookmarkService bms;
//
//	// ✅ 1️⃣ 일반 게시판 글 작성 테스트 (boardCategory = 1)
//    @Test
//    public void testWriteNormalPost() {
//        BoardDTO dto = new BoardDTO();
//        dto.setId("a");
//        dto.setTitle("일반 게시판 테스트");
//        dto.setContent("이것은 일반 게시판 테스트입니다.");
//        dto.setBoardOpen(1);
//        dto.setBoardCategory(1); // 일반 게시판
//
//        boolean result = bs.writeSave(dto);
//        assertTrue(result);
//
//        BoardDTO savedPost = bs.getPost(dto.getBoardNo());
//        assertNotNull(savedPost);
//        assertEquals(dto.getTitle(), savedPost.getTitle());
//    }
//
//    // ✅ 2️⃣ 기록 게시판 글 작성 테스트 (boardCategory = 2)
//    @Test
//    public void testWriteRecordPost() {
//        BoardDTO dto = new BoardDTO();
//        dto.setId("a");
//        dto.setTitle("기록 게시판 테스트");
//        dto.setContent("이것은 기록 게시판 테스트입니다.");
//        dto.setBoardOpen(1);
//        dto.setBoardCategory(2); // 기록 게시판
//
//        boolean result = bs.writeSave(dto);
//        assertTrue(result);
//
//        BoardDTO savedPost = bs.getPost(dto.getBoardNo());
//        assertNotNull(savedPost);
//        assertEquals(dto.getTitle(), savedPost.getTitle());
//    }
//
//    // ✅ 3️⃣ 여행 메이트 게시판 글 작성 테스트 (boardCategory = 3)
//    @Test
//    public void testWriteMatePost() {
//        BoardDTO dto = new BoardDTO();
//        dto.setId("a");
//        dto.setTitle("여행 메이트 테스트");
//        dto.setContent("이것은 여행 메이트 게시판 테스트입니다.");
//        dto.setBoardOpen(1);
//        dto.setBoardCategory(3); // 여행 메이트 게시판
//
//        boolean result = bs.writeMateBoardSave(dto);
//        assertTrue(result);
//
//        BoardDTO savedPost = bs.getMateBoardDetail(dto.getBoardNo());
//        assertNotNull(savedPost);
//        assertEquals(dto.getTitle(), savedPost.getTitle());
//    }
//
//    // ✅ 4️⃣ 모든 게시글이 정상적으로 조회되는지 확인
//    @Test
//    public void testGetBoardList() {
//        List<Map<String, Object>> list = bs.getBoardList(1, 2); // 기록 게시판(2) 조회
//        assertNotNull(list);
//        assertTrue(list.size() >= 0); // 리스트가 0개여도 오류가 나면 안 됨
//    }
//	
//}
