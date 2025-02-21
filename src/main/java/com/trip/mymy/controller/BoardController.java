package com.trip.mymy.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;
import com.trip.mymy.service.BoardService;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board")
public class BoardController {
	@Autowired BoardService bs;

	@GetMapping("/writeForm")
	public String writeForm() {
		return "board/writeForm"; // writeForm.jsp를 반환
	}
	@PostMapping("/writeSave")
	public ResponseEntity<Map<String, Object>> writeSave(@RequestBody BoardDTO dto) {
		dto.setId("a"); // 기본 ID 설정
		dto.setBoardCategory(1); // 기본 카테고리 설정

		String result = bs.writeSave(dto);
		Map<String, Object> response = new HashMap<>();

		if (result.contains("성공")) {
			response.put("message", result);
			response.put("boardNo", dto.getBoardNo());
			return ResponseEntity.ok(response);
		} else {
			response.put("message", result);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}


	//	@PostMapping("/writeSave")
	//	public void writeSave(BoardDTO dto, HttpServletRequest request, HttpServletResponse res) throws IOException {
	//	    dto.setId("a"); // 테스트용 아이디 설정
	//	    dto.setBoardCategory(1);
	//
	//	    String path = request.getContextPath();
	//
	//	    // `boardOpen` 값이 null이면 기본값(1: 공개)으로 설정
	//	    if (dto.getBoardOpen() == null) {
	//	        dto.setBoardOpen(1);
	//	    }
	//
	//	    // 게시글 저장
	//	    String msg = bs.writeSave(dto, path);
	//
	//	    res.setContentType("text/html; charset=utf-8");
	//	    PrintWriter out = res.getWriter();
	//	    out.print(msg);
	//	}
	// 이미지 업로드 처리uploadSummernoteImageFile
	@PostMapping(  "/uploadSummernoteImageFile")
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
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> list(@RequestParam(value = "page", defaultValue = "1") int page) {
		int totalPosts = bs.getTotalPosts();
		int pageSize = 6;
		int totalPages = (totalPosts + pageSize - 1) / pageSize;

		if (totalPages == 0) totalPages = 1;

		List<BoardDTO> boardList = bs.getBoardList(page);

		Map<String, Object> response = new HashMap<>();
		response.put("boardList", boardList);
		response.put("currentPage", page);
		response.put("totalPages", totalPages);

		return ResponseEntity.ok(response);
	}

	//    @GetMapping("/list")
	//    public String list(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
	//        int totalPosts = bs.getTotalPosts(); // 전체 게시글 수 가져오기
	//        int pageSize = 6; // 한 페이지당 게시글 6개
	//        int totalPages = (totalPosts + pageSize - 1) / pageSize; // 전체 페이지 계산
	//
	//        if (totalPages == 0) totalPages = 1; // 최소 1페이지는 존재하도록 설정
	//
	//        List<BoardDTO> boardList = bs.getBoardList(page);
	//
	//        model.addAttribute("boardList", boardList);
	//        model.addAttribute("currentPage", page);
	//        model.addAttribute("totalPages", totalPages); // 전체 페이지 수 추가 (JSP에서 사용)
	//
	//        return "board/list";
	//    }
	//    @GetMapping("/detail")
	//    public String detail(@RequestParam("boardNo") int boardNo, Model model) {
	//        BoardDTO post = bs.getPost(boardNo);
	//        List<BoardRepDTO> replies = bs.getRepliesByBoardNo(boardNo);
	//        
	//        // depth 정보를 저장할 Map
	//        Map<Integer, Integer> depthMap = new HashMap<>();
	//        List<BoardRepDTO> sortedReplies = new ArrayList<>();
	//
	//        // 부모 댓글 기준으로 depth 계산
	//        organizeReplies(replies, sortedReplies, depthMap, 0, 0);
	//        
	//        model.addAttribute("post", post);
	//        model.addAttribute("replies",replies);
	//        model.addAttribute("depthMap", depthMap); // ✅ JSP에서 depth 참조 가능하도록 추가
	//        
	//        return "board/detail"; // detail.jsp 반환
	//    }
	// // 재귀적으로 댓글을 정렬하고 depth를 저장하는 메서드
	//    private void organizeReplies(List<BoardRepDTO> replies, List<BoardRepDTO> sortedReplies, Map<Integer, Integer> depthMap, int parentNo, int depth) {
	//        for (BoardRepDTO reply : replies) {
	//            if (reply.getParentNo() == parentNo) {
	//                depthMap.put(reply.getRepNo(), depth); // ✅ depth 저장
	//                sortedReplies.add(reply);
	//                organizeReplies(replies, sortedReplies, depthMap, reply.getRepNo(), depth + 1);
	//            }
	//        }
	//    }

	@GetMapping("/detail")
	public ResponseEntity<BoardDTO> detail(@RequestParam("boardNo") int boardNo, Model model) {
		BoardDTO post = bs.getPost(boardNo);
		//        model.addAttribute("post", post);
		return ResponseEntity.ok(post);
	}

	@GetMapping("/modifyForm")
	public String modifyForm(@RequestParam("boardNo") int boardNo, Model model) {
		BoardDTO post= bs.getPost(boardNo);
		model.addAttribute("post", post);
		return "board/modifyForm";
	}
	// 게시글 수정
	@PostMapping("/modify")
	public ResponseEntity<String> modifyPost(@RequestBody BoardDTO dto) {
		BoardDTO existingPost = bs.getPost(dto.getBoardNo());

		if (!"a".equals(existingPost.getId())) {  // 임시로 "a" 사용자로 설정
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
		}

		String result = bs.modify(dto, "/board/detail");
		return ResponseEntity.ok(result);
	}

	// 게시글 삭제
	@DeleteMapping("/delete/{boardNo}")
	public ResponseEntity<String> deletePost(@PathVariable int boardNo) {
		BoardDTO existingPost = bs.getPost(boardNo);

		if (!"a".equals(existingPost.getId())) {  // 임시로 "a" 사용자로 설정
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
		}

		String result = bs.delete(boardNo, "/board/list");
		return ResponseEntity.ok(result);
	}

	//    @PostMapping("/modify")
	//    public void modify(BoardDTO dto, HttpServletRequest request, HttpServletResponse res) throws IOException {
	//        // 게시글 수정 후 msg와 url반환
	//        String msg = bs.modify(dto, request.getContextPath());
	//
	//        // 응답 타입 설정
	//        res.setContentType("text/html; charset=utf-8");
	//        PrintWriter out = res.getWriter();
	//
	//        // 메시지와 URL을 포함한 스크립트로 페이지 이동
	//        out.print(msg); 
	//    }
	//    @DeleteMapping("/delete/{boardNo}")
	//    public void delete(@PathVariable("boardNo") int boardNo, HttpServletRequest request, HttpServletResponse res) throws IOException {
	//        // 게시글 삭제 후 msg와 url반환
	//        String msg = bs.delete(boardNo, request.getContextPath());
	//
	//        // 응답 타입 설정
	//        res.setContentType("text/html; charset=utf-8");
	//        PrintWriter out = res.getWriter();
	//
	//        out.print(msg);
	//    }
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

		// 응답 데이터 구성
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


}