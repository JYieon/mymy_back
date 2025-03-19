<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>북마크한 게시글</title>
<style>
    .bookmark-container {
        width: 80%;
        margin: auto;
        text-align: center;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    th, td {
        border-bottom: 1px solid #ddd;
        padding: 12px;
        text-align: center;
    }
    th {
        background-color: #f4f4f4;
        font-weight: bold;
    }
    tr:hover {
        background-color: #f1f1f1;
    }
</style>
</head>
<body>
    <div class="bookmark-container">
        <h2>내가 북마크한 게시글</h2>

        <c:choose>
            <c:when test="${empty bookmarkList}">
                <p>북마크한 게시글이 없습니다.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            
                            <th>제목</th>
                            <th>아이디</th>
                            <th>작성일</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="post" items="${bookmarkList}">
                            <tr>
                             
                                <td>
                                    <a href="${pageContext.request.contextPath}/board/detail?boardNo=${post.boardNo}">
                                        ${post.title}
                                    </a>
                                </td>
                                <td>${post.id}</td>
                                <td>${post.date}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
