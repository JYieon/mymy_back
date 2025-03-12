<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
<style>
/* 게시글 목록 스타일 */
.board-container {
	width: 80%;
	margin: auto;
}

.board-list {
	display: grid;
	grid-template-columns: repeat(3, 1fr); /* 🔹 3개의 컬럼 */
	gap: 20px; /* 🔹 여백 조정 */
	justify-content: center;
}

.board-item {
	border: 1px solid #ddd;
	border-radius: 10px;
	padding: 10px;
	text-align: center;
	background-color: #fff;
	box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1);
	transition: transform 0.2s ease-in-out;
}

.board-item:hover {
	transform: scale(1.05);
}

.board-item img {
	width: 100%;
	height: 200px; /* 🔹 이미지 크기 조정 */
	object-fit: cover; /* 🔹 이미지 비율 유지 */
	border-radius: 10px;
}

.board-item h3 {
	font-size: 16px;
	margin: 10px 0;
}

.board-item p {
	font-size: 14px;
	color: #555;
}
/* 페이징 스타일 */
.pagination {
	text-align: center;
	margin-top: 20px;
}

.pagination a {
	text-decoration: none;
	padding: 8px 12px;
	margin: 0 5px;
	border-radius: 5px;
	background-color: #ddd;
	color: black;
}

.pagination a:hover {
	background-color: #333;
	color: white;
}

.pagination .current {
	background-color: #333;
	color: white;
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="board-list">
		<c:forEach var="post" items="${boardList}">
			<div class="board-item">
				<a
					href="${pageContext.request.contextPath}/board/detail?boardNo=${post.boardNo}">
					<!-- 기본 썸네일 경로 설정 --> <c:set var="defaultThumbnail"
						value="${pageContext.request.contextPath}/resources/images/default-thumbnail.jpg" />

					<!-- 첫 번째 이미지 추출 --> <c:set var="thumbnail" value="" /> <c:if
						test="${not empty post.content and fn:contains(post.content, '<img')}">
						<c:set var="thumbnailStart"
							value="${fn:substringAfter(post.content, '<img src=\"')}" />
                    <c:set var="thumbnailEnd" value="${fn:substringBefore(thumbnailStart, '\"')}" />
						<c:if test="${not empty thumbnailEnd}">
							<c:set var="thumbnail" value="${thumbnailEnd}" />
						</c:if>
					</c:if> <!-- 썸네일 이미지 출력 (기본 썸네일 적용) --> <img
					src="${empty thumbnail ? defaultThumbnail : thumbnail}" alt="썸네일"
					width="100%" height="200px"
					style="object-fit: cover; border-radius: 10px;">

					<h3>
						<c:if test="${post.boardOpen eq 0}">
							<c:out value="🔒"/>
						</c:if>
						${post.title}
					</h3>

					<p>조회수: ${post.boardCnt} | 댓글: ${post.boardLikes}</p>
					<p>${post.id}</p>
				</a>
			</div>
		</c:forEach>
	</div>

	<!-- 페이징 처리 -->
	<div class="pagination">
		<c:if test="${currentPage > 1}">
			<a href="?page=${currentPage - 1}">이전</a>
		</c:if>

		<c:forEach var="page" begin="1" end="${totalPages}">
			<c:choose>
				<c:when test="${page == currentPage}">
					<span class="current">${page}</span>
				</c:when>
				<c:otherwise>
					<a href="?page=${page}">${page}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<c:if test="${currentPage < totalPages}">
			<a href="?page=${currentPage + 1}">다음</a>
		</c:if>
	</div>

	<a href="${pageContext.request.contextPath}/board/writeForm">글쓰기</a>
	</div>
</body>
</html>
