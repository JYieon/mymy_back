package com.trip.mymy.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.BoardService;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board")
public class BoardController {
	@Autowired BoardService bs;
	@Autowired TokenProvider tp;

	@PostMapping("/writeSave")
	public ResponseEntity<String> writeSave(@RequestBody BoardDTO dto, @RequestHeader("Authorization") String token) {
	    // 토큰이 비어 있거나 null일 경우 처리
	    if (token == null || token.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT 토큰이 비어 있거나 null입니다.");
	    }
	    try {
	        // "Bearer " 부분을 제거하고 실제 토큰만 사용
	        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

	        // 토큰을 통해 인증 정보를 가져옴
	        Authentication authentication = tp.getAuthentication(jwtToken);
	        if (authentication == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
	        }

	        MemberDTO member = (MemberDTO) authentication.getPrincipal();
	        dto.setId(member.getId()); // 사용자 ID 설정

	        // 계획 게시글이면 공개 여부 및 해시태그 제거
	        if (dto.getBoardCategory() == 1) {
	            dto.setBoardOpen(null);
	            dto.setHashtags(null);
	        }

	        // 게시글 저장
	        boolean success = bs.writeSave(dto);
	        if (success) {
	            // 기록 게시글에만 해시태그 추가
	            if (dto.getBoardCategory() == 2 && dto.getHashtags() != null && !dto.getHashtags().isEmpty()) {
	                bs.addTags(dto.getBoardNo(), dto.getHashtags());
	            }
	            return ResponseEntity.ok("게시글이 성공적으로 저장되었습니다.");
	        } else {
	            return ResponseEntity.badRequest().body("게시글 저장에 실패했습니다.");
	        }
	    } catch (Exception e) {
	        // 예외 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}

	// 이미지 업로드 처리uploadSummernoteImageFile
	@PostMapping("/uploadSummernoteImageFile")
	@ResponseBody
	public Map<String, String> uploadSummernoteImageFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		Map<String, String> response = new HashMap<>();

		// 파일 저장 경로 설정 (여기서는 resources/upload 디렉토리로 설정)
		String uploadDir = "C:/summernote_image/"; 
		File uploadFolder = new File(uploadDir);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs(); // 폴더가 없으면 생성
		}
		//파일 저장
		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		String filePath = uploadDir + fileName;


		try {
			File serverFile = new File(filePath);
			file.transferTo(serverFile);
			System.out.println("저장된 파일경로 "+filePath);
			//절대 url 반환
			String fullUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/upload/" + fileName;
			response.put("fileName", fileName); // 파일명 저장
			response.put("url", fullUrl); // 절대 URL 저장
		} catch (IOException e) {
			e.printStackTrace();
			response.put("error", "파일 업로드 실패");
		}
		return response;
	}

	// 게시글 목록
	// category - 1: 계획, 2: 기록)
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> list(
			@RequestBody BoardDTO dto,
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        @RequestParam(value = "category", defaultValue = "1") int category,
	        @RequestHeader("Authorization") String token
	) {
	    // "Bearer " 부분을 제거하고 실제 토큰만 사용
	    String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

	    // 토큰을 통해 인증 정보를 가져옴
	    Authentication authentication = tp.getAuthentication(jwtToken);
	    if (authentication == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	    }
	    MemberDTO member = (MemberDTO) authentication.getPrincipal(); // 로그인한 사용자의 ID 추출
	    dto.setId(member.getId()); // 사용자 ID 설정
	    // 페이지 처리
	    int totalPosts = 0;
	    List<Map<String, Object>> boardList = new ArrayList<>();

	    if (category == 1) {
	        // category = 1일 때 로그인한 사용자 ID 기준으로 필터링
	        totalPosts = bs.getTotalPosts(category); // 전체 게시글 수 (category 1)
	        boardList = bs.getBoardList(page, category); // 전체 게시글 목록 (category 1)
	        
	        // 로그인한 사용자의 게시글만 필터링 (userId가 일치하는 게시글만 가져옴)
	        boardList = boardList.stream()
	                .filter(board -> board.get("MEMBER_ID_PK").equals(member.getId()))  // 필터링
	                .collect(Collectors.toList());
	    } else {
	        // category = 2일 때 모든 게시글 조회
	        totalPosts = bs.getTotalPosts(category); // 전체 게시글 수 (category 2)
	        boardList = bs.getBoardList(page, category); // 전체 게시글 목록 (category 2)
	    }

	    // 페이지 계산
	    int pageSize = 6;
	    int totalPages = (totalPosts + pageSize - 1) / pageSize;

	    // 응답 데이터 구성
	    Map<String, Object> response = new HashMap<>();
	    response.put("boardList", boardList); // 게시글 목록
	    response.put("currentPage", page); // 현재 페이지
	    response.put("totalPages", totalPages); // 전체 페이지 수

	    return ResponseEntity.ok(response);
	}

	// 게시글 상세 페이지
	@GetMapping("/detail")
	public ResponseEntity<Map<String, Object>> detail(@RequestParam("boardNo") int boardNo) {
		BoardDTO post = bs.getPost(boardNo);
		List<String> hashtags = bs.tagList(boardNo);  // 해시태그 조회

		Map<String, Object> response = new HashMap<>();
		response.put("post", post);
		response.put("hashtags", hashtags);

		return ResponseEntity.ok(response);
	}

	// 특정 게시글의 해시태그 조회
	@GetMapping("/tags/{boardNo}")
	public ResponseEntity<List<String>> getTags(@PathVariable int boardNo) {
		List<String> tags = bs.tagList(boardNo);
		return ResponseEntity.ok(tags);
	}

	// 게시글 수정
	@PostMapping("/modify")
	public ResponseEntity<String> modify(@RequestBody BoardDTO dto) {
		if (bs.modify(dto)) {
			bs.deleteTags(dto.getBoardNo());  // 기존 태그 삭제
			if (dto.getHashtags() != null && !dto.getHashtags().isEmpty()) {
				bs.addTags(dto.getBoardNo(), dto.getHashtags());  // 새로운 태그 추가
			}
			return ResponseEntity.ok("게시글이 성공적으로 수정되었습니다.");
		} else {
			return ResponseEntity.badRequest().body("게시글 수정에 실패했습니다.");
		}
	}

	// 게시글 삭제
	@DeleteMapping("/delete/{boardNo}")
	public ResponseEntity<String> delete(@PathVariable int boardNo) {
		if (bs.delete(boardNo)) {
			return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
		} else {
			return ResponseEntity.badRequest().body("게시글 삭제에 실패했습니다.");
		}
	}

	// 좋아요 토글
	@PostMapping("/like/toggle")
	public ResponseEntity<Map<String, Object>> toggleLike(@RequestBody Map<String, Integer> request) {
		int boardNo = request.get("boardNo");

		// 좋아요 상태 확인
		int currentLikes = bs.getLikes(boardNo);
		boolean isLiked = currentLikes > 0;

		// 좋아요 상태 반전
		if (isLiked) {
			bs.decreaseLike(boardNo);  // 좋아요 취소
		} else {
			bs.increaseLike(boardNo);  // 좋아요 등록
		}

		// 새로운 상태 조회
		int updatedLikes = bs.getLikes(boardNo);
		boolean newLikedStatus = updatedLikes > 0;

		// 응답 데이터
		Map<String, Object> response = new HashMap<>();
		response.put("liked", newLikedStatus);
		response.put("likes", updatedLikes);

		return ResponseEntity.ok(response);
	}

	// 좋아요 체크
	@GetMapping("/like/check")
	public ResponseEntity<Map<String, Object>> checkLike(@RequestParam int boardNo) {
		int likes = bs.getLikes(boardNo);

		Map<String, Object> response = new HashMap<>();
		response.put("liked", likes > 0);  // 좋아요 여부
		response.put("likes", likes);      // 좋아요 수

		return ResponseEntity.ok(response);
	}

	// 검색 기능
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> search(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "category", defaultValue = "2") int category, // 기본: 기록 게시글(2)
			@RequestParam(value = "searchType") String searchType,  // 검색 카테고리
			@RequestParam(value = "keyword") String keyword
			) {
		int totalPosts = bs.getSearchTotalPosts(category, searchType, keyword);
		int pageSize = 6;
		int totalPages = (totalPosts + pageSize - 1) / pageSize;

		// System.out.println("검색요청:searchtype="+searchType+",keyword="+keyword+", page" + page);
		List<Map<String, Object>> boardList = bs.searchBoardList(page, category, searchType, keyword);
		
		Map<String, Object> response = new HashMap<>();
		response.put("boardList", boardList);
		response.put("currentPage", page);
		response.put("totalPages", totalPages);

		return ResponseEntity.ok(response);
	}


}