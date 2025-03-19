<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

	<h2>${post.title}</h2>
	<p>작성자: ${post.id}</p>
	<p>작성일: ${post.date}</p>
	<p>
		조회수: <span id="view-count">${post.boardCnt}</span>
	</p>
	<hr>
	<pre>${post.content}</pre>
	<hr>

	<!-- ✅ 좋아요 버튼 -->
	<button id="like-btn" onclick="toggleLike(${post.boardNo})">
		<span id="like-icon">❤️</span> <span id="like-count">${post.boardLikes}</span>
	</button>

	<!-- ✅ 북마크 버튼 -->
	<button id="bookmark-btn" onclick="toggleBookmark()">북마크 추가</button>

	<a href="${pageContext.request.contextPath}/board/modifyForm?boardNo=${post.boardNo}" class="btn btn-warning">수정</a>
	<button type="button" onclick="deletePost(${post.boardNo})">삭제</button>

	<hr>

	<!-- 📝 댓글 작성 -->
	<h3>댓글 작성</h3>
	<textarea id="replyContent" placeholder="댓글을 입력하세요..."
		style="width: 100%; height: 50px;"></textarea>
	<button onclick="addReply(${post.boardNo})">등록</button>

	<hr>

	<!-- 🗨️ 댓글 목록 -->
	<h3>댓글 목록</h3>
	<div id="replyList">
		<c:forEach var="reply" items="${replies}">
			<%-- 부모 댓글의 depth 값을 가져와서 자식 댓글의 depth를 자동으로 증가 --%>
			<c:set var="currentDepth" value="${depthMap[reply.repNo]}" />

			<div style="border-left: ${currentDepth * 20}px solid #ddd; padding:10px; margin-left: ${currentDepth * 20}px;">
				<p><strong>${reply.id}</strong> | ${reply.repDate}</p>
				<p>${reply.repContent}</p>
				<textarea id="child-reply-${reply.repNo}" placeholder="대댓글 입력..." style="width:100%; height:40px;"></textarea>
				<button onclick="addChildReply(${post.boardNo}, ${reply.repNo})">대댓글 작성</button>
				<button onclick="deleteReply(${reply.repNo})">삭제</button>
			</div>
		</c:forEach>
	</div>

	<script>
	
	 let id = "a";
	 
    $(document).ready(function() {
        // ✅ 임시로 'a' 사용자 아이디로 지정
        let boardNo = '${post.boardNo}';
        
        checkLike(boardNo);
        checkBookmark(id, boardNo);
    });

    // ✅ 좋아요 추가/삭제 (토글)
    function toggleLike(boardNo) {
        $.post("${pageContext.request.contextPath}/board/like", 
            { boardNo: boardNo }, 
            function(response) {
                $("#like-count").text(response.likes);  // 좋아요 개수 업데이트
            }
        ).fail(function() {
            console.error("좋아요 처리 중 오류가 발생했습니다.");
        });
    }
    
    function checkLike(boardNo) {
        $.get("${pageContext.request.contextPath}/board/like/check", 
            { boardNo: boardNo },  // ✅ `id`는 필요 없음
            function(likeCount) { // ✅ 정수값 반환
                $("#like-count").text(likeCount); // 좋아요 개수 업데이트
                $("#like-icon").text(likeCount > 0 ? "❤️" : "🤍"); // 아이콘 변경
            }
        ).fail(function() {
            console.error("좋아요 상태 확인 오류");
        });
    }

    // 북마크 추가/삭제 (토글)
    function toggleBookmark() {
        let id = "a"; // ✅ 임시로 'a' 사용자 아이디로 지정
        let boardNo = '${post.boardNo}';
        $.post("${pageContext.request.contextPath}/board/bookmark/toggle", 
            { id: id, boardNo: boardNo },
            function(response) {
                checkBookmark(id, boardNo); // 북마크 상태 다시 확인
            }
        ).fail(function() {
            console.error("북마크 처리 중 오류 발생");
        });
    }

    //북마크 여부 확인 후 버튼 변경
    function checkBookmark() {
        let id = "a"; // ✅ 임시로 'a' 사용자 아이디로 지정
        let boardNo = '${post.boardNo}';
        $.get("${pageContext.request.contextPath}/board/bookmark/check", 
            { id: id, boardNo: boardNo }, 
            function(isBookmarked) {
                let bookmarkBtn = $("#bookmark-btn");
                if (isBookmarked === true) {
                    bookmarkBtn.text("북마크 삭제").css("background", "#ff6961");
                } else {
                    bookmarkBtn.text("북마크 추가").css("background", "#77dd77");
                }
            }
        ).fail(function() {
            console.error("북마크 상태 확인 오류");
        });
    }

    // ✅ 게시글 삭제
    function deletePost(boardNo) {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        fetch("${pageContext.request.contextPath}/board/delete/" + boardNo, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(response => {
            if (response.ok) {
                alert("삭제 성공!");
                location.href = "${pageContext.request.contextPath}/board/list"; // ✅ 목록 페이지로 이동
            } else {
                alert("삭제 실패! 다시 시도해주세요.");
            }
        })
        .catch(error => {
            console.error("삭제 오류:", error);
            alert("게시글 삭제 중 오류가 발생했습니다.");
        });
    }

    // ✅ 댓글 등록
    function addReply(boardNo) {
        let content = $("#replyContent").val();
        if (content.trim() === "") {
            alert("댓글을 입력하세요!");
            return;
        }

        $.ajax({
            url: '${pageContext.request.contextPath}/board/addReply', 
            type: "POST",
            data: {
                boardNo: boardNo,
                repContent: content
            },
            success: function () {
                alert("댓글이 등록되었습니다!");
                location.reload();
            },
            error: function () {
                alert("댓글 등록 실패!");
            }
        });
    }

    // ✅ 대댓글 등록 (대댓글의 대댓글도 가능)
    function addChildReply(boardNo, parentNo) {
        let content = $("#child-reply-" + parentNo).val();
        if (content.trim() === "") {
            alert("대댓글을 입력하세요!");
            return;
        }

        $.ajax({
            url: '${pageContext.request.contextPath}/board/addReply', 
            type: "POST",
            data: {
                boardNo: boardNo,
                repContent: content,
                parentNo: parentNo // 부모 댓글 또는 대댓글의 댓글
            },
            success: function () {
                alert("대댓글이 등록되었습니다!");
                location.reload();
            },
            error: function () {
                alert("대댓글 등록 실패!");
            }
        });
    }

    function deleteReply(replyNo) {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        fetch("${pageContext.request.contextPath}/board/deleteReply/" + replyNo, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            }
        })
        .then(response => response.text())
        .then(message => {
            alert(message);
            location.reload();
        })
        .catch(error => {
            console.error("댓글 삭제 실패:", error);
            alert("댓글 삭제 중 오류가 발생했습니다.");
        });
    }
	</script>

	<a href="${pageContext.request.contextPath}/board/list">목록으로</a>
	
</body>
</html>
