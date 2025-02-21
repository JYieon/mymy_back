package com.trip.mymy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.dto.BoardRepDTO;
import com.trip.mymy.service.BoardService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board") // ✅ 댓글도 /board 경로에서 처리
public class BoardRepController {

	@Autowired
	private BoardService bs;

	//    /** ✅ 댓글 등록 (대댓글 포함) */
	//    @PostMapping("/addReply")
	//    public String addReply(@RequestParam("boardNo") int boardNo, 
	//                           @RequestParam("repContent") String repContent, 
	//                           @RequestParam(value = "parentNo", required = false, defaultValue = "0") int parentNo) {
	//        BoardRepDTO replyDTO = new BoardRepDTO();
	//        replyDTO.setId("a"); // 테스트용 아이디 설정
	//        replyDTO.setBoardNo(boardNo);
	//        replyDTO.setRepContent(repContent);
	//        replyDTO.setParentNo(parentNo);
	//
	//        bs.addReply(replyDTO);
	//        return "redirect:/board/detail?boardNo=" + boardNo;
	//    }
	// 댓글 작성
	@PostMapping("/addReply")
	public ResponseEntity<String> addReply(@RequestBody BoardRepDTO dto) {
		if (dto.getRepContent() == null || dto.getRepContent().isEmpty()) {
			return ResponseEntity.badRequest().body("댓글 내용이 없습니다.");
		}

		bs.addReply(dto);
		return ResponseEntity.ok("댓글 작성 성공!");
	}
	// 댓글 목록 조회
	@GetMapping("/replyList/{boardNo}")
	public List<BoardRepDTO> getReplyList(@PathVariable int boardNo) {
		return bs.getRepliesByBoardNo(boardNo);
	}
	// 댓글 삭제
	@DeleteMapping("/deleteReply/{replyNo}")
	public ResponseEntity<String> deleteReply(@PathVariable int replyNo) {
		bs.deleteReply(replyNo, null);
		return ResponseEntity.ok("댓글 삭제 성공!");
	}

	//    // 댓글목록
	//    @GetMapping("/replyList/{boardNo}")
	//    public String getReplies(@PathVariable int boardNo, Model model) {
	//        List<BoardRepDTO> replies = bs.getRepliesByBoardNo(boardNo);
	//        model.addAttribute("replies", replies);
	//        return "board/replies"; // 댓글을 별도 JSP 파일에서 표시
	//    }

	//    @DeleteMapping("/deleteReply/{replyNo}")
	//    public ResponseEntity<String> deleteReply(@PathVariable("replyNo") int replyNo) {
	//        String msg = bs.deleteReply(replyNo, "");
	//        return ResponseEntity.ok()
	//                .header("Content-Type", "text/plain; charset=UTF-8") // ✅ UTF-8 설정 추가
	//                .body(msg);
	//    }


}
