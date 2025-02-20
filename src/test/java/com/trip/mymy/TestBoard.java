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
//	// BoardController
//	@Test
//	public void testBoardController() {
//		System.out.println("--- BoardController: " + bc);
//		assertNotNull(bc);
//	}
//
//	// BoardService의 게시글 저장
//	@Test
//	public void testBoardServiceInsert() {
//		BoardDTO dto = new BoardDTO();
//		dto.setId("a");
//		dto.setTitle("JUnit 테스트 게시글");  
//		dto.setContent("JUnit 테스트");
//		dto.setBoardOpen(0);
//		dto.setBoardCategory(1);
//
//		// 게시글 저장
//		String result = bs.writeSave(dto, "/test-path/");
//		System.out.println("게시글 저장 결과: " + result);
//
//		assertTrue(result.contains("게시글이 성공적으로 저장되었습니다!"));
//	}
//
//	// BoardService 게시글 목록 조회
//	@Test
//	public void testGetBoardList() {
//		List<BoardDTO> list = bs.getBoardList(1);
//		System.out.println("게시글 개수: " + list.size());
//
//		if (!list.isEmpty()) {
//			System.out.println("첫 번째 게시글 제목: " + list.get(0).getTitle());
//		}
//
//		assertNotSame(0, list.size());
//	}
//	
//	// 게시글 수정 테스트
//	@Test
//	public void testModifyBoard() {
//	    // 기존 게시글 중 하나 선택 (없으면 생성)
//	    List<BoardDTO> list = bs.getBoardList(1);
//	    int boardNo;
//	    
//	    if (!list.isEmpty()) {
//	        boardNo = list.get(0).getBoardNo(); // 기존 게시글 선택
//	    } else {
//	        // 게시글이 없다면 새 게시글 추가
//	        BoardDTO dto = new BoardDTO();
//	        dto.setId("a");
//	        dto.setTitle("수정 테스트 게시글");
//	        dto.setContent("수정 테스트용 내용");
//	        dto.setBoardOpen(0);
//	        dto.setBoardCategory(1);
//
//	        String result = bs.writeSave(dto, "/test-path/");
//	        System.out.println("새 게시글 생성 결과: " + result);
//	        
//	        // 생성된 게시글 번호 가져오기
//	        boardNo = Integer.parseInt(result.replaceAll("[^0-9]", ""));
//	    }
//
//	    // 수정할 내용 설정
//	    BoardDTO modifiedDto = new BoardDTO();
//	    modifiedDto.setBoardNo(boardNo);
//	    modifiedDto.setTitle("수정된 게시글 제목");
//	    modifiedDto.setContent("수정된 게시글 내용");
//
//	    String modifyResult = bs.modify(modifiedDto, "/test-path/");
//	    System.out.println("게시글 수정 결과: " + modifyResult);
//
//	    BoardDTO updatedPost = bs.getPost(boardNo);
//	    assertEquals("수정된 게시글 제목", updatedPost.getTitle());
//	    assertEquals("수정된 게시글 내용", updatedPost.getContent());
//	}
//
//	// 게시글 삭제 테스트
//	@Test
//	public void testDeleteBoard() {
//	    // 기존 게시글 중 하나 선택 (없으면 생성)
//	    List<BoardDTO> list = bs.getBoardList(1);
//	    int boardNo;
//
//	    if (!list.isEmpty()) {
//	        boardNo = list.get(0).getBoardNo(); // 기존 게시글 선택
//	    } else {
//	        // 게시글이 없다면 새 게시글 추가
//	        BoardDTO dto = new BoardDTO();
//	        dto.setId("a");
//	        dto.setTitle("삭제 테스트 게시글");
//	        dto.setContent("삭제 테스트용 내용");
//	        dto.setBoardOpen(0);
//	        dto.setBoardCategory(1);
//
//	        String result = bs.writeSave(dto, "/test-path/");
//	        System.out.println("새 게시글 생성 결과: " + result);
//	        
//	        // 생성된 게시글 번호 가져오기
//	        boardNo = Integer.parseInt(result.replaceAll("[^0-9]", ""));
//	    }
//	    String deleteResult = bs.delete(boardNo, "/test-path/");
//	    System.out.println("게시글 삭제 결과: " + deleteResult);
//
//	    // 삭제된 게시글이 DB에서 존재하지 않는지 확인
//	    BoardDTO deletedPost = bs.getPost(boardNo);
//	    assertNull(deletedPost);
//	}
//	
//	// BookmarkController
//		@Test
//		public void testBookmarkController() {
//			System.out.println("--- BookmarkController: " + bmc);
//			assertNotNull(bmc);
//		}
//
//	// 북마크 추가 및 삭제
//	@Test
//	public void testBookmarkToggle() {
//		String id = "a"; 
//		int boardNo = 3; 
//
//		// 북마크 추가
//		boolean isBookmarked = bms.toggleBookmark(id, boardNo);
//		System.out.println("북마크 추가 결과: " + (isBookmarked ? "추가됨" : "이미 존재함"));
//
//		boolean existsAfterAdd = bms.isBookmarked(id, boardNo);
//		assertTrue(existsAfterAdd);
//
//		// 북마크 삭제
//		isBookmarked = bms.toggleBookmark(id, boardNo);
//		System.out.println("북마크 삭제 결과: " + (isBookmarked ? "삭제됨" : "이미 삭제됨"));
//
//		boolean existsAfterDelete = bms.isBookmarked(id, boardNo);
//		assertFalse(existsAfterDelete);
//	}
//
//	// 북마크 목록 조회
//	@Test
//	public void testGetBookmarkList() {
//		String id = "a"; 
//		List<BoardDTO> bookmarkList = bms.getBookmarkList(id);
//
//		System.out.println("북마크된 게시글 개수: " + bookmarkList.size());
//		if (!bookmarkList.isEmpty()) {
//			System.out.println("첫 번째 북마크 게시글 제목: " + bookmarkList.get(0).getTitle());
//		}
//
//		assertNotSame(0, bookmarkList.size());
//	}
//	
//    // 좋아요 추가
//    @Test
//    public void testIncreaseLike() {
//        int boardNo = 33;  
//        int beforeLikes = bs.getLikes(boardNo);
//        bs.toggleLike(boardNo);
//        int afterLikes = bs.getLikes(boardNo);
//        assertEquals(beforeLikes + 1, afterLikes);
//    }
//
//    // 좋아요 취소
//    @Test
//    public void testDecreaseLike() {
//        int boardNo = 33; 
//        int beforeLikes = bs.getLikes(boardNo);
//        bs.toggleLike(boardNo);
//        int afterLikes = bs.getLikes(boardNo);
//        assertEquals(beforeLikes - 1, afterLikes);
//    }
//
//    // 좋아요 개수 
//    @Test
//    public void testGetLikes() {
//        int boardNo = 33;
//        int likes = bs.getLikes(boardNo);
//        assertTrue(likes >= 0);
//    }
//}
