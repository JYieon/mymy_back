package com.trip.mymy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.BoardService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/mateboard")
public class MateBoardController {
	@Autowired
	private BoardService bs;
	@Autowired TokenProvider tp;

	@PostMapping("/write")
	public ResponseEntity<String> writeMateBoard(@RequestBody BoardDTO dto, @RequestHeader("Authorization") String token) {
		// 토큰이 비어 있거나 null일 경우 처리
		if (token == null || token.isEmpty()) {
			return ResponseEntity.badRequest().body("JWT 토큰이 비어 있거나 null입니다.");
		}
		try {
			// "Bearer " 부분을 제거하고 실제 토큰만 사용
			String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

			// 토큰을 통해 인증 정보를 가져옴
			Authentication authentication = tp.getAuthentication(jwtToken);
			if (authentication == null) {
				return ResponseEntity.status(401).body("인증 실패");
			}

			// MemberDTO에서 사용자 정보를 가져옴
			MemberDTO member = (MemberDTO) authentication.getPrincipal();
			dto.setId(member.getId());  // 로그인한 사용자의 ID 설정

			// 기본 값 설정
			dto.setBoardCategory(3);  // 여행 메이트 카테고리 지정

			// 게시글 저장
			boolean success = bs.writeMateBoardSave(dto);
			return success ? ResponseEntity.ok("여행 메이트 게시글이 저장되었습니다.")
					: ResponseEntity.badRequest().body("게시글 저장 실패!");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
		}
	}

	@GetMapping("/list")
	public ResponseEntity<List<Map<String, Object>>> getMateBoardList(
			@RequestParam(value = "page", defaultValue = "1") int page) {

		List<Map<String, Object>> boardList = bs.getBoardList(page, 3, "none");
		return ResponseEntity.ok(boardList);
	}

	// 여행 메이트 게시글 상세 조회
	@GetMapping("/detail/{boardNo}")
	public ResponseEntity<BoardDTO> getMateBoardDetail(@PathVariable int boardNo) {
		BoardDTO post = bs.getMateBoardDetail(boardNo); //
		return ResponseEntity.ok(post);
	}

	// 여행 메이트 게시글 수정
	@PostMapping("/modify/{boardNo}")
	public ResponseEntity<String> modifyMateBoard(@PathVariable int boardNo, @RequestBody BoardDTO dto) {
		if (dto.getTitle() == null || dto.getContent() == null) {
			return ResponseEntity.badRequest().body("제목과 내용은 필수 입력 항목입니다.");
		}

		dto.setBoardNo(boardNo); // URL에서 받은 boardNo를 DTO에 설정
		boolean success = bs.modifyMateBoard(dto);

		return success ? ResponseEntity.ok("여행 메이트 게시글이 수정되었습니다.")
				: ResponseEntity.badRequest().body("게시글 수정 실패!");
	}

	// 여행 메이트 게시글 삭제
	@DeleteMapping("/delete/{boardNo}")
	public ResponseEntity<String> deleteMateBoard(@PathVariable int boardNo) {
		boolean success = bs.delete(boardNo);
		return success ? ResponseEntity.ok("여행 메이트 게시글이 삭제되었습니다.")
				: ResponseEntity.badRequest().body("게시글 삭제 실패!");
	}
	// 댓글 목록 조회 
	@GetMapping("/replyList/{boardNo}")
	public ResponseEntity<List<BoardRepDTO>> getReplies(@PathVariable int boardNo) {
		List<BoardRepDTO> replies = bs.getRepliesByBoardNo(boardNo);
		return ResponseEntity.ok(replies);
	}

	// 댓글 작성
	@PostMapping("/addReply")
	public ResponseEntity<String> addReply(@RequestBody BoardRepDTO dto, @RequestHeader("Authorization") String token) {
		// 토큰이 비어 있거나 null일 경우 처리
		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT 토큰이 비어 있거나 null입니다.");
		}
		// "Bearer " 부분을 제거하고 실제 토큰만 사용
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

		// 토큰을 통해 인증 정보를 가져옴
		Authentication authentication = tp.getAuthentication(jwtToken);
		if (authentication == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
		}

		MemberDTO member = (MemberDTO) authentication.getPrincipal();
		dto.setId(member.getId()); // 사용자 ID 설정

		// 댓글 작성
		bs.addReply(dto);
		return ResponseEntity.ok("댓글 작성 성공!");
	}

	// 댓글 삭제
	@DeleteMapping("/deleteReply/{replyNo}")
	public ResponseEntity<String> deleteReply(@PathVariable int replyNo) {
		try {
			bs.deleteReply(replyNo, "경로_예시");
			return ResponseEntity.ok("댓글이 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("댓글 삭제 실패: " + e.getMessage());
		}
	}

	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> searchMateBoardList(
			@RequestParam("searchType") String searchType,
			@RequestParam("keyword") String keyword,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		int category = 3;
		// 검색 결과 가져오기
		List<Map<String, Object>> searchResults = bs.searchMateBoardList(page, category, searchType, keyword);
		int totalPosts = bs.getSearchMateTotalPosts(searchType, keyword);
		int pageSize = 6;
		int totalPages = (totalPosts + pageSize - 1) / pageSize;

		// 페이지 정보와 함께 응답 반환
		Map<String, Object> response = new HashMap<>();
		response.put("boardList", searchResults);
		response.put("currentPage", page);
		response.put("totalPages", totalPages);

		return ResponseEntity.ok(response);
	}



}
