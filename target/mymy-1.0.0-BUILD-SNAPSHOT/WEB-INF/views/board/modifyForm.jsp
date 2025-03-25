<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>

    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/summernote/summernote-lite.css">
</head>
<body>
    <h2>게시글 수정</h2>
    <!--  summernote -->
	<script src="${pageContext.request.contextPath}/resources/summernote/summernote-lite.js"></script>
	<script src="${pageContext.request.contextPath}/resources/summernote/lang/summernote-ko-KR.js"></script>
    

    <form method="post" action="${pageContext.request.contextPath}/board/modify">
        <input type="hidden" name="boardNo" value="${post.boardNo}" />
        <input class="form-control" placeholder="제목" style="width:100%" name="title" value="${post.title}" required />
        <textarea id="summernote" name="content" required>${post.content}</textarea>
        <input type="submit" value="수정">
    </form>

   
<script>
        $(document).ready(function () {
            // jQuery가 정상적으로 로드된 후에 summernote 초기화
            $('#summernote').summernote({
                height: 300,
                lang: 'ko-KR',
                callbacks: {
                    // 이미지 업로드 처리 (파일을 서버로 전송)
                    onImageUpload: function(files) {
                        uploadImage(files[0]);
                    },
                    // Summernote 초기화 시 콘솔 로그 출력
                    onInit: function() {
                        console.log('Summernote가 실행되었습니다!');
                    },
                    // 이미지 업로드 에러 처리
                    onImageUploadError: function(err) {
                        console.log('이미지 업로드 실패: ', err);
                        alert("이미지 업로드 중 오류가 발생했습니다.");
                    }
                }
            });
            function uploadImage(file) {
                let formData = new FormData();
                formData.append("file", file);

                $.ajax({
                    url: '${pageContext.request.contextPath}/board/uploadSummernoteImageFile',
                    type: 'POST',
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function(data) {
                        if (data.fileName) {
                            console.log("이미지 업로드 완료: " + data.fileName);

                            // ✅ URL을 `/upload/파일명.png`로 수정
                            let imageUrl = "http://localhost:8080/mymy/upload/" + data.fileName;

                            let imgNode = $('<img>').attr('src', imageUrl)[0];
                            $('#summernote').summernote('insertNode', imgNode);
                        } else {
                        	console.error("❌ 이미지 업로드 AJAX 실패:", error);
                            alert("이미지 업로드 실패");
                    }
                    }
                });
            }
            $('#summernote-textarea').summernote({
                height: 450,
                lang: "ko-KR",
                placeholder: "내용을 작성하세요.",
                toolbar: [
                    ['fontname', ['fontname']],
                    ['fontsize', ['fontsize']],
                    ['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
                    ['color', ['forecolor', 'color']],
                    ['table', ['table']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['height', ['height']],
                    ['insert', ['picture', 'link', 'video']]
                ],
                fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', '맑은 고딕', '궁서', '굴림체', '굴림', '돋움체', '바탕체'],
                fontSizes: ['8', '9', '10', '11', '12', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72']
            });
        });
    </script>
</body>
</html>
