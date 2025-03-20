package com.trip.mymy.service;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;
import com.trip.mymy.mybatis.BoardMapper;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

	@Autowired BoardMapper mapper;
	
	public List<Map<String, Object>> searchBoardList(int page, int category, String searchType, String keyword) {
	    int limit = 6;
	    int offset = (page - 1) * limit;

	    Map<String, Object> params = new HashMap<>();
	    params.put("offset", offset);
	    params.put("limit", limit);
	    params.put("category", category);
	    params.put("searchType", searchType);
	    params.put("keyword", "%" + keyword + "%"); // LIKE 검색을 위해 % 추가

	    return mapper.searchBoardList(params);
	}

	public int getSearchTotalPosts(int category, String searchType, String keyword) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("category", category);
	    params.put("searchType", searchType);
	    params.put("keyword", "%" + keyword + "%");

	    return mapper.getSearchTotalPosts(params);
	}

	// 댓글 저장
	public void addReply(BoardRepDTO replyDTO) {
		System.out.println(replyDTO.getBoardNo());
		// PARENT_NO가 NULL이면 기본값 0 설정
		System.out.println("reply" + replyDTO.getParentNo());
		if (replyDTO.getParentNo() == null) {
			replyDTO.setParentNo(0);
		}

		// PARENT_NO가 0보다 크면 부모 댓글이 존재하는지 확인
		if (replyDTO.getParentNo() > 0) {
			int parentExists = mapper.checkParentExists(replyDTO.getParentNo());
			if (parentExists == 0) {
				throw new RuntimeException("부모 댓글이 존재하지 않습니다! (잘못된 parentNo: " + replyDTO.getParentNo() + ")");
			}
		}

		// 댓글 추가
		mapper.addReply(replyDTO);
	}

	// 댓글 목록 조회
	public List<BoardRepDTO> getRepliesByBoardNo(int boardNo) {
		List<BoardRepDTO> replies = mapper.getRepData(boardNo);
		// System.out.println("Service 댓글 조회 결과: " + replies);
		return replies;
	}

	// 댓글 삭제 (대댓글 포함)
	@Override
	public int deleteReply(int replyNo, String id) {
	    // 댓글 작성자 조회
	    String writerId = mapper.getReplyWriter(replyNo);

	    // 댓글이 존재하지 않는 경우
	    if (writerId == null) {
	        return 0;
	    }

	    // 댓글 작성자와 로그인한 사용자 비교
	    if (!writerId.equals(id)) {
	        throw new RuntimeException("해당 댓글을 삭제할 권한이 없습니다.");
	    }

	    // 4️⃣ 댓글 삭제 실행 (삭제된 행 개수 반환)
	    return mapper.deleteReply(replyNo);
	}


	//게시글 좋아요
	public boolean toggleLike(String id, int boardNo) {
		
		Map<String, Object> params= new HashMap<>();
		params.put("id", id);
		params.put("boardNo", boardNo);
		
		int liked= mapper.checkUserLike(id, boardNo);
		
		if(liked == 0) {
			mapper.addLike(params);
		}else {
			mapper.removeLike(params);
		}
		mapper.updateBoardLikes(boardNo);
		
		int updatedLikes= mapper.getBoardLikes(boardNo);
		
		// System.out.println("좋아요 변경 후 개수: " + updatedLikes);
		return liked == 0;
	}

	public int getBoardLikes(int boardNo) {
		return mapper.getBoardLikes(boardNo);
	}
	
	public boolean checkUserLike(String id, int boardNo) {
        return mapper.checkUserLike(id, boardNo) > 0;
    }

	// 게시글 목록 조회
	public List<Map<String, Object>> getBoardList(int page, int category, String id) {
		int limit = 6;
		int offset = (page - 1) * limit;
		List<BoardDTO> boardList = new ArrayList<BoardDTO>();

		Map<String, Integer> params = new HashMap<>();
		params.put("offset", offset);
		params.put("limit", limit);
		params.put("category", category);  // 카테고리 추가
		
		if(category == 1) {
			boardList = mapper.getUserBoardList(offset, limit, category, id);
		}else {
			//System.out.println("getBoardList"+offset +", limit=" + limit + ", category=" + category);
			boardList = mapper.getBoardList(params);
		}
		
//		 System.out.println("[Service] 게시글 목록 응답 데이터:");
//		    for (BoardDTO post : boardList) {
//		        System.out.println("게시글 No." + post.getBoardNo() + " - 좋아요 수: " + post.getBoardLikes());
//		    }
		    
		//System.out.println("조회된 게시글 개수:"+boardList.size());
		List<Map<String, Object>> responseList = new ArrayList<>();
		
		for (BoardDTO post : boardList) {
			Map<String, Object> postMap = new HashMap<>();
			postMap.put("boardNo", post.getBoardNo());
			postMap.put("title", post.getTitle());
			postMap.put("id", post.getId());
			postMap.put("boardCnt", post.getBoardCnt());
			postMap.put("boardLikes", post.getBoardLikes());
			postMap.put("boardOpen", post.getBoardOpen());
			postMap.put("date", post.getDate().toString());

			// 첫 번째 이미지 추출
			String content = post.getContent();
			if (content != null) {
				Document doc = Jsoup.parse(content);
				Element imgTag = doc.selectFirst("img");
				String thumbnail = (imgTag != null) 
						? imgTag.attr("src") 
								: "http://localhost:8080/mymy/resources/images/default-thumbnail.jpg";
				postMap.put("thumbnail", thumbnail); // 썸네일만 Map에 추가
			}

			responseList.add(postMap);
		}

		return responseList;
	}
	//전체 게시글 수
	public int getTotalPosts(int category) {
		return mapper.getTotalPosts(category);
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
		mapper.boardCnt(boardNo); //조회수 증가
		BoardDTO post = mapper.getPost(boardNo);
		if (post != null) {
			post.setHashtags(tagList(boardNo));  // 해시태그 조회 및 설정
			// 웹페이지에서 보일 때 다시 `<br>` 태그로 변환
			post.setContent(post.getContent().replace("\n", "<br>"));
		}
		return post;
	}

	// 게시글 저장 (HTML 정리 + 첫 번째 이미지 자동 추출)
	public int writeSave(BoardDTO dto) {
	    System.out.println("📌 writeSave 호출됨!");

	    if (dto == null) {
	        System.out.println("❌ dto가 null입니다!");
	        return 0;
	    }
	    if (dto.getTitle() == null) {
	        System.out.println("❌ dto.getTitle()이 null입니다!");
	    }
	    if (dto.getContent() == null) {
	        System.out.println("❌ dto.getContent()이 null입니다!");
	    }
	    if (dto.getBoardOpen() == null) {
	        System.out.println("❌ dto.getBoardOpen()이 null입니다! 기본값(1) 설정");
	        dto.setBoardOpen(1);
	    }
	    if (dto.getBoardCategory() == null) {
	        System.out.println("❌ dto.getBoardCategory()가 null입니다! 기본값(1) 설정");
	        dto.setBoardCategory(1);
	    }

	    try {
	        // HTML 내용 정리
	        Document doc = Jsoup.parse(dto.getContent());
	        doc.select("script, style").remove();

	        // <br> 태그를 (\n)로 변환
	        String formattedContent = doc.body().html().replace("<br>", "\n").replace("<br/>", "\n");

	        // <img> 태그만 유지
	        formattedContent = Jsoup.clean(formattedContent, "", org.jsoup.safety.Safelist.basicWithImages(), new Document.OutputSettings().prettyPrint(false));
	        dto.setContent(formattedContent); // 변경된 내용을 DTO에 적용

	        // 게시글 저장
	        int result = mapper.writeSave(dto);
	        
	        // 저장된 boardNo 확인
	        int boardNo = dto.getBoardNo();
	        System.out.println("✅ 저장된 boardNo: " + boardNo);

	        // 해시태그 저장
	        if (result == 1 && dto.getHashtags() != null && !dto.getHashtags().isEmpty()) {
	            addTags(boardNo, dto.getHashtags());
	        }

	        return boardNo;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; // 실패 시 -1 반환
	    }
	}


	
	// 게시글 수정 (태그 포함)
	public boolean modify(BoardDTO dto) {
		try {
			// 게시글 수정
			int result = mapper.modify(dto);

			if (result == 1) {
	            // 계획 게시글이면 해시태그 제거
	            if (dto.getBoardCategory() == 1) {
	                deleteTags(dto.getBoardNo());
	                dto.setHashtags(null); // DTO에서도 제거
	            } else {
	                // 기록 게시글만 태그 저장
	                deleteTags(dto.getBoardNo());
	                if (dto.getHashtags() != null && !dto.getHashtags().isEmpty()) {
	                    addTags(dto.getBoardNo(), dto.getHashtags());
	                }
	            }
	            return true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// 게시글 삭제
	@Transactional
	public boolean deleteBoard(int boardNo) {
	    int result = mapper.deleteBoard(boardNo);
	    //System.out.println("삭제된 행 개수: " + result);
	    return result > 0;
	}

	// 특정 게시글과 연결된 해시태그 삭제
	public void deleteTags(int boardNo) {
		mapper.deleteBoardTags(boardNo);
	}

	// 게시글과 연결된 해시태그 추가
	public void addTags(int boardNo, List<String> tags) {
	    if (tags != null && !tags.isEmpty()) {
	        for (String tag : tags) {
	            if (tag != null && !tag.trim().isEmpty()) {
	                try {
	                    // 태그 삽입 (중복 방지)
	                    mapper.insertTag(tag.trim());

	                    // 게시글과 태그 연결
	                    Map<String, Object> params = new HashMap<>();
	                    params.put("boardNo", boardNo);
	                    params.put("tagName", tag.trim());
	                    mapper.insertBoardTag(params);

	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	}

	// 특정 게시글의 해시태그 조회
	public List<String> tagList(int boardNo) {
	    List<String> tags = mapper.getTagsByBoardNo(boardNo);
	    // System.out.println("가져온 해시태그: " + tags);
	    return tags;
	}

	public void updateTags(int boardNo, List<String> tags) {
		// TODO Auto-generated method stub
		
	}
	
	// 여행 메이트 게시글 작성
	public boolean writeMateBoardSave(BoardDTO dto) {
	    System.out.println("writeMateBoardSave 요청 데이터: " + dto);

	    dto.setBoardCategory(3); // 여행 메이트 게시판
	    dto.setBoardOpen(1);  // 공개 설정
	    dto.setBoardCnt(0);
	    dto.setBoardLikes(0);
	    
	    // <br> 태그가 포함된 HTML을 그대로 저장
	    String contentWithBr = dto.getContent().replace("\n", "<br>");
	    dto.setContent(contentWithBr);

	    int result = mapper.insertMateBoard(dto);
	    System.out.println("insert 결과: "+result);
	    System.out.println("저장된 boardNo:"+dto.getBoardNo());
	    return result > 0;
	}
	
	// 여행 메이트 게시글 수정
	public boolean modifyMateBoard(BoardDTO dto) {
	    if (dto.getBoardNo() == 0 || dto.getTitle() == null || dto.getContent() == null) {
	        return false; // 잘못된 요청이면 수정 불가
	    }

	    // `<br>` 태그 유지하도록 수정
	    String cleanContent = dto.getContent().replace("\n", "<br>");

	    dto.setContent(cleanContent);

	    int result = mapper.modifyMateBoard(dto);
	    return result == 1;
	}
	
	// 여행 메이트 게시글 삭제
	public boolean deleteMateBoard(int boardNo) {
	    try {
	        mapper.deleteBoard(boardNo); // 게시글 삭제
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	// 여행 메이트 게시글 상세페이지
	public BoardDTO getMateBoardDetail(int boardNo) {
	    mapper.boardCnt(boardNo); // 조회수 증가
	    BoardDTO post = mapper.getMateBoardDetail(boardNo);
	    return post;
	}
	
	// 여행 메이트 검색
	public List<Map<String, Object>> searchMateBoardList(int page, int category, String searchType, String keyword) {
	    int limit = 6;
	    int offset = (page - 1) * limit;

	    // % 중복 방지
	    if (keyword.startsWith("%") && keyword.endsWith("%")) {
	        keyword = keyword.substring(1, keyword.length() - 1);
	    }

	    Map<String, Object> params = new HashMap<>();
	    params.put("offset", offset);
	    params.put("limit", limit);
	    params.put("category", category);
	    params.put("searchType", searchType);
	    params.put("keyword", "%" + keyword + "%");

	    try {
	        return mapper.searchMateBoardList(params);
	    } catch (Exception e) {
	        throw new RuntimeException("SQL 실행 중 오류 발생: " + e.getMessage(), e);
	    }
	}
	
	// 여행 메이트 검색결과
	public int getSearchMateTotalPosts(String searchType, String keyword) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("category", 3);
	    params.put("searchType", searchType);

	    // LIKE 검색을 위해 % 추가
	    if (!keyword.contains("%")) {
	        params.put("keyword", "%" + keyword + "%");
	    } else {
	        params.put("keyword", keyword);
	    }

	    return mapper.getSearchMateTotalPosts(params);
	}


}
