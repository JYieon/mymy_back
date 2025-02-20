package com.trip.mymy.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;
import com.trip.mymy.mybatis.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
    
    @Autowired BoardMapper mapper;
    
    // 댓글 저장
    @Transactional
    @Override
    public void addReply(BoardRepDTO replyDTO) {
        // 1️⃣ PARENT_NO가 NULL이면 기본값 0 설정
        if (replyDTO.getParentNo() == null) {
            replyDTO.setParentNo(0);
        }

        // 2️⃣ PARENT_NO가 0보다 크면 부모 댓글이 존재하는지 확인
        if (replyDTO.getParentNo() > 0) {
            int parentExists = mapper.checkParentExists(replyDTO.getParentNo());
            if (parentExists == 0) {
                throw new RuntimeException("부모 댓글이 존재하지 않습니다! (잘못된 parentNo: " + replyDTO.getParentNo() + ")");
            }
        }

        // 3️⃣ 댓글을 추가
        mapper.addReply(replyDTO);
    }

    // 게시글의 댓글 가져오기
    @Override
    public List<BoardRepDTO> getRepliesByBoardNo(int boardNo) {
        List<BoardRepDTO> replies = mapper.getRepData(boardNo);

        // 부모 댓글을 기준으로 계층적으로 정렬
        List<BoardRepDTO> sortedReplies = new ArrayList<>();
        Map<Integer, List<BoardRepDTO>> replyMap = new HashMap<>();

        // parentNo 기준으로 그룹화
        for (BoardRepDTO reply : replies) {
            replyMap.computeIfAbsent(reply.getParentNo(), k -> new ArrayList<>()).add(reply);
        }

        // 계층적으로 정렬된 댓글 리스트 생성 (JSP에서 depth를 계산할 수 있도록 순서만 정렬)
        sortReplies(sortedReplies, replyMap, 0);

        return sortedReplies;
    }

    // 부모 → 자식 순서대로 정렬
    private void sortReplies(List<BoardRepDTO> sortedReplies, Map<Integer, List<BoardRepDTO>> replyMap, int parentNo) {
        if (!replyMap.containsKey(parentNo)) return;

        for (BoardRepDTO reply : replyMap.get(parentNo)) {
            sortedReplies.add(reply);
            sortReplies(sortedReplies, replyMap, reply.getRepNo()); // 자식 댓글도 정렬
        }
    }

    // 댓글 삭제 (대댓글 포함)
    @Transactional
    @Override
    public String deleteReply(int replyNo, String path) {
    	   int result = mapper.deleteReply(replyNo);
    	   return (result == 1) ? "댓글이 성공적으로 삭제되었습니다." : "댓글 삭제에 실패했습니다.";
    	}
    
    //게시글 좋아요
    @Transactional
    public void toggleLike(int boardNo) {
        // 현재 좋아요 개수 가져오기
        int currentLikes = mapper.getLikes(boardNo);

        // 좋아요 개수가 0이면 증가, 아니면 감소
        if (currentLikes == 0) {
            mapper.increaseLike(boardNo);
        } else {
            mapper.decreaseLike(boardNo);
        }
    }

    @Transactional
    public int getLikes(int boardNo) {
        return mapper.getLikes(boardNo);
    }

    // 게시글 목록 조회
    @Override
    public List<BoardDTO> getBoardList(int page) {
        int limit = 6; // 한 페이지당 6개씩 표시
        int offset = (page - 1) * limit; // 페이지에 맞는 시작점 계산

        // ✅ offset과 limit을 Map에 담아 전달
        Map<String, Integer> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);

        List<BoardDTO> boardList = mapper.getBoardList(params);

        // 각 게시글에서 첫 번째 이미지 태그를 추출하여 썸네일로 설정
        for (BoardDTO post : boardList) {
            String content = post.getContent();
            if (content != null) {
                Document doc = Jsoup.parse(content);
                Element imgTag = doc.selectFirst("img"); // 첫 번째 이미지 찾기
                if (imgTag != null) {
                    post.setContent(imgTag.outerHtml()); // 첫 번째 이미지 태그만 content에 설정
                } else {
                    post.setContent("<img src='http://localhost:8080/mymy/resources/images/default-thumbnail.jpg' alt='기본 썸네일'>"); // 기본 썸네일
                }
            }
        }
        return boardList;
    }
    //전체 게시글 수
    public int getTotalPosts() {
        return mapper.getTotalPosts();
    }
    //게시글 조회수 증가
    private void boardCnt(int boardNo) {
    	try {
			mapper.boardCnt(boardNo);
		} catch (Exception e) {
			
		}
    }
    
    // 게시글 상세 조회 (줄바꿈 처리)
    public BoardDTO getPost(int boardNo) {
    	mapper.boardCnt(boardNo);
        BoardDTO post = mapper.getPost(boardNo);
        if (post != null) {
            // 🟢 웹페이지에서 보일 때 다시 `<br>` 태그로 변환
            post.setContent(post.getContent().replace("\n", "<br>"));
        }
        return post;
    }

    // 게시글 저장 (HTML 정리 + 첫 번째 이미지 자동 추출)
    @Override
    public String writeSave(BoardDTO dto) {
        if (dto.getBoardOpen() == 0 || dto.getBoardOpen() == 1) {
            // 정상적인 값이 들어왔으면 그대로 사용
        } else {
            dto.setBoardOpen(1); // 기본값 1 (공개) 설정
        }

        String msg;
        int result = 0;

        try {
            // HTML 내용 분석
            Document doc = Jsoup.parse(dto.getContent());
            doc.select("script, style").remove();

            // <br> 태그를 줄바꿈 문자(\n)로 변환
            String formattedContent = doc.body().html().replace("<br>", "\n").replace("<br/>", "\n");

            // <img> 태그는 유지하고 나머지 HTML 태그 정리
            formattedContent = Jsoup.clean(formattedContent, "", org.jsoup.safety.Safelist.basicWithImages(), new Document.OutputSettings().prettyPrint(false));

            dto.setContent(formattedContent); // 변경된 내용을 DTO에 적용

            // 데이터베이스에 저장
            result = mapper.writeSave(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 결과에 따라 메시지 설정
        if (result == 1) {
            msg = "게시글이 성공적으로 저장되었습니다!";
        } else {
            msg = "게시글 저장에 실패했습니다!";
        }

        return msg;
    }



    // 게시글 수정
    @Override
    public String modify(BoardDTO dto, String path) {
        int result = mapper.modify(dto);
        String msg = "";
        String url = "";
        
        if (result == 1) {
            msg = "게시글이 성공적으로 수정되었습니다!";
            url = path + "/board/detail?boardNo=" + dto.getBoardNo(); // 수정 성공 시 상세 페이지로 이동
        } else {
            msg = "게시글 수정에 실패했습니다!";
            url = path + "/board/modifyForm?boardNo=" + dto.getBoardNo(); // 수정 실패 시 수정 페이지로 이동
        }

        return getMessage(msg, url); 
    }

    // 게시글 삭제
    public String delete(int boardNo, String path) {
        int result = mapper.delete(boardNo);
        String msg = "";
        String url = "";
        
        if (result == 1) {
            msg = "게시글이 성공적으로 삭제되었습니다!";
            url = path + "/board/list"; // 삭제 성공 시 목록 페이지로 이동
        } else {
            msg = "게시글 삭제에 실패했습니다!";
            url = path + "/board/detail?boardNo=" + boardNo; // 삭제 실패 시 다시 상세 페이지로 이동
        }

        return getMessage(msg, url);
    }

    // 메시지 출력 함수 (재사용 가능)
    private String getMessage(String msg, String url) {
        return "<script>alert('" + msg + "'); location.href='" + url + "';</script>";
    }
    
}
