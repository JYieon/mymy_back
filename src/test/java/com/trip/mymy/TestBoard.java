package com.trip.mymy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.controller.BoardController;
import com.trip.mymy.controller.BoardRepController;
import com.trip.mymy.controller.BookmarkController;
import com.trip.mymy.controller.MateBoardController;
import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.service.BoardService;
import com.trip.mymy.service.BookmarkService;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations= {"classpath:testBoard.xml",
"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
public class TestBoard {
	@Autowired BoardController bc;
	@Autowired BoardService bs;
	@Autowired BookmarkController bmc;
	@Autowired BookmarkService bms;
	@Autowired MateBoardController mc;
	@Autowired BoardRepController rc;
	
	@Test
	public void testToggleLike() {
	    String id = "a";
	    int boardNo = 72;

	    // 좋아요 추가
	    ResponseEntity<Map<String, Object>> addResponse = bc.toggleLike(Map.of("boardNo", boardNo));
	    System.out.println("Add Like Response: " + addResponse.getBody());
	    assertTrue((boolean) addResponse.getBody().get("liked"));

	    // 좋아요 상태 확인 (좋아요가 추가된 상태여야 함)
	    int likesAfterAdd = bs.getLikes(boardNo);
	    System.out.println("Likes after add: " + likesAfterAdd);
	    assertEquals(1, likesAfterAdd); // 좋아요가 1로 증가했어야 함

	    // 좋아요 삭제
	    ResponseEntity<Map<String, Object>> deleteResponse = bc.toggleLike(Map.of("boardNo", boardNo));
	    System.out.println("Delete Like Response: " + deleteResponse.getBody());
	    assertFalse((boolean) deleteResponse.getBody().get("liked"));

	    // 좋아요 상태 확인 (좋아요가 삭제된 상태여야 함)
	    int likesAfterDelete = bs.getLikes(boardNo);
	    System.out.println("Likes after delete: " + likesAfterDelete);
	    assertEquals(0, likesAfterDelete); // 좋아요가 0으로 되돌아왔어야 함
	}


	// 북마크 추가 및 삭제 토글 테스트
	@Test
	public void testToggleBookmark() {
	    String id = "a";
	    int boardNo = 72; 

	    // 북마크 추가
	    ResponseEntity<String> addResponse = bmc.toggleBookmark(id, boardNo);
	    System.out.println("Add Response: " + addResponse.getBody());
	    assertEquals("북마크 추가", addResponse.getBody());

	    // 북마크 상태 확인
	    boolean isBookmarked = bms.isBookmarked(id, boardNo);
	    System.out.println("Is Bookmarked after add: " + isBookmarked);
	    assertTrue(isBookmarked);

	    // 북마크 삭제
	    ResponseEntity<String> deleteResponse = bmc.toggleBookmark(id, boardNo);
	    System.out.println("Delete Response: " + deleteResponse.getBody());
	    assertEquals("북마크 삭제", deleteResponse.getBody());

	    // 북마크 상태 확인
	    isBookmarked = bms.isBookmarked(id, boardNo);
	    System.out.println("Is Bookmarked after delete: " + isBookmarked);
	    assertFalse(isBookmarked);
	}


	// 계획 게시판 글 작성 테스트 (boardCategory = 1)
    @Test
    public void testWritePlanPost() {
        BoardDTO dto = new BoardDTO();
        dto.setId("a");
        dto.setTitle("일반 게시판 테스트");
        dto.setContent("이것은 일반 게시판 테스트입니다.");
        dto.setBoardOpen(1);
        dto.setBoardCategory(1); // 일반 게시판

        boolean result = bs.writeSave(dto);
        assertTrue(result);

        BoardDTO savedPost = bs.getPost(dto.getBoardNo());
        assertNotNull(savedPost);
        assertEquals(dto.getTitle(), savedPost.getTitle());
    }
    
    @Test
    public void testModifyPlanPost() {
        // 게시글 저장
        BoardDTO dto = new BoardDTO();
        dto.setId("a");
        dto.setTitle("계획 게시글 수정 전");
        dto.setContent("계획 게시글 수정 전");
        dto.setBoardOpen(1);
        dto.setBoardCategory(1); // 계획 게시글

        boolean saveResult = bs.writeSave(dto);
        assertTrue(saveResult);

        // 저장된 boardNo 확인
        System.out.println("저장된 boardNo (writeSave 후): " + dto.getBoardNo());
        assertNotSame(0, dto.getBoardNo());

        // 3저장된 게시글 다시 조회하여 boardNo 확인
        BoardDTO savedPost = bs.getPost(dto.getBoardNo());
        assertNotNull(savedPost);
        System.out.println("조회된 게시글의 boardNo: " + savedPost.getBoardNo());

        // 수정 전 데이터 출력
        System.out.println("수정 전 제목: " + savedPost.getTitle());
        System.out.println("수정 전 내용: " + savedPost.getContent());

        // DTO에 저장된 boardNo 설정 (MyBatis에서 자동 증가한 값 가져오기)
        dto.setBoardNo(savedPost.getBoardNo());

        // 게시글 수정
        dto.setTitle("수정된 계획 게시글 제목");
        dto.setContent("수정된 계획 게시글 내용");
        boolean modifyResult = bs.modify(dto);
        assertTrue(modifyResult);

        // 수정 결과 확인
        System.out.println("수정 완료 여부: " + modifyResult);

        // 수정 후 게시글 조회
        BoardDTO updatedPost = bs.getPost(dto.getBoardNo());
        assertNotNull(updatedPost);

        // 수정된 데이터 출력
        System.out.println("수정된 게시글 제목: " + updatedPost.getTitle());
        System.out.println("수정된 게시글 내용: " + updatedPost.getContent());

        // 게시글 제목이 변경되었는지 확인
        assertEquals("수정된 계획 게시글 제목", updatedPost.getTitle());
        assertEquals("수정된 계획 게시글 내용", updatedPost.getContent());
    }

    @Test
    public void testDeletePlanPost() {
        // 게시글 저장
        BoardDTO dto = new BoardDTO();
        dto.setId("a");
        dto.setTitle("계획 게시글 삭제 전");
        dto.setContent("계획 게시글 삭제 전");
        dto.setBoardOpen(1);
        dto.setBoardCategory(1); // 계획 게시글

        boolean saveResult = bs.writeSave(dto);
        assertTrue(saveResult);

        // 저장된 boardNo 확인
        System.out.println("저장된 boardNo (writeSave 후): " + dto.getBoardNo());
        assertNotSame(0, dto.getBoardNo());

        // 저장된 게시글 다시 조회하여 boardNo 확인
        BoardDTO savedPost = bs.getPost(dto.getBoardNo());
        assertNotNull(savedPost);
        System.out.println("조회된 게시글의 boardNo: " + savedPost.getBoardNo());

        // 게시글 삭제
        boolean deleteResult = bs.delete(dto.getBoardNo());
        assertTrue(deleteResult);

        // 삭제 후 게시글 조회 (삭제된 게시글이 없음을 확인)
        BoardDTO deletedPost = bs.getPost(dto.getBoardNo());
        assertNull(deletedPost);
        System.out.println("삭제 후 게시글 조회 결과: " + deletedPost);
    }

    
    // 기록 게시판 글 작성 테스트 (boardCategory = 2)
    @Test
    public void testWriteRecordPost() {
        BoardDTO dto = new BoardDTO();
        dto.setId("a");
        dto.setTitle("기록 게시판 테스트");
        dto.setContent("이것은 기록 게시판 테스트입니다.");
        dto.setBoardOpen(1);
        dto.setBoardCategory(2); // 기록 게시판

        boolean result = bs.writeSave(dto);
        assertTrue(result);

        BoardDTO savedPost = bs.getPost(dto.getBoardNo());
        assertNotNull(savedPost);
        assertEquals(dto.getTitle(), savedPost.getTitle());
    }

    // 여행 메이트 게시판 글 작성 테스트 (boardCategory = 3)
    @Test
    public void testWriteMatePost() {
        BoardDTO dto = new BoardDTO();
        dto.setId("a");
        dto.setTitle("여행 메이트 테스트");
        dto.setContent("이것은 여행 메이트 게시판 테스트입니다.");
        dto.setBoardOpen(1);
        dto.setBoardCategory(3); // 여행 메이트 게시판

        boolean result = bs.writeMateBoardSave(dto);
        // System.out.println("게시글 저장 결과: " + result);
        // System.out.println("저장된 boardNo: " + dto.getBoardNo()); // boardNo 확인

        assertTrue(result);
        
        BoardDTO savedPost = bs.getMateBoardDetail(dto.getBoardNo());
        // System.out.println("조회된 게시글: " + savedPost);

        assertNotNull(savedPost);
        assertEquals(dto.getTitle(), savedPost.getTitle());
    }

    // 모든 게시글이 정상적으로 조회되는지 확인
    @Test
    public void testGetBoardList() {
        List<Map<String, Object>> list = bs.getBoardList(1, 2); // 기록 게시판(2) 조회
        assertNotNull(list);
        assertTrue(list.size() >= 0); // 리스트가 0개여도 오류가 나면 안 됨
    }
	
    
}
