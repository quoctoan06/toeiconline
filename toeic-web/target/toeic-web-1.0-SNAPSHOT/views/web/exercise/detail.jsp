<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/taglib.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="label.exercise.listening.practice" bundle="${lang}"/></title>
</head>
<body>
<form action="" method="get" id="formUrl">
    <div class="row">
        <div class="span12">
            <ul class="thumbnails">
                <li class="span12">
                    <div class="thumbnail" id="result">
                        <br/>
                        <c:forEach var="item" items="${items.listResult}">
                            <p>
                                <b>${item.question}</b>
                            </p>
                            <c:if test="${item.image != null}">
                                <p>
                                    <img src="<c:url value="/repository/${item.image}"/>" width="300px" height="150px">
                                </p>
                            </c:if>
                            <c:if test="${item.audio != null}">
                                <p>
                                    <audio controls>
                                        <source src="<c:url value="/repository/${item.audio}"/>" type="audio/mpeg">
                                    </audio>
                                </p>
                            </c:if>
                            <p>
                                <input type="radio" name="answerOfUser" value="A"/>
                                    ${item.option1}
                            </p>
                            <p>
                                <input type="radio" name="answerOfUser" value="B"/>
                                    ${item.option2}
                            </p>
                            <p>
                                <input type="radio" name="answerOfUser" value="C"/>
                                    ${item.option3}
                            </p>
                            <p>
                                <input type="radio" name="answerOfUser" value="D"/>
                                    ${item.option4}
                            </p>
                            <input type="hidden" name="exerciseId" value="${item.exercise.exerciseId}" id="exerciseId"/>
                        </c:forEach>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <!--Pagination-->
    <div class="row">
        <div class="span12">
            <ul id="pagination-demo" class="pagination-sm"></ul>
        </div>
        <input type="hidden" name="page" id="page" value="${items.page}"/>
        <%--khi submit form, controller sẽ nhận được page và exerciseId để tìm được exerciseQuestionDTO ở trang hiện tại và hiển thị đáp án--%>
        <input type="button" class="btn btn-info" value="<fmt:message key='label.show.answer' bundle='${lang}'/>" id="btnShowAnswer"/>
        <input type="button" class="btn btn-info" value="<fmt:message key='label.do.again' bundle='${lang}'/>" id="btnAgain"/>
    </div>
</form>
<script>
    var totalPages = ${items.totalPages};
    var startPage = ${items.page};

    $(document).ready(function () {
        $('#btnShowAnswer').click(function () { // chỉ show đáp án của câu hỏi ở trang hiện tại
            if($('input[name="answerOfUser"]:checked').length > 0) {    // nếu người dùng đã chọn đáp án
                $('#formUrl').submit();
            } else {    // ngược lại nếu chưa chọn đáp án nào
                alert('Bạn chưa chọn đáp án nào!');
            }
        });
        $('#btnAgain').click(function () {  // load lại trang hiện tại (startPage) để làm lại câu hỏi
            var exerciseId = $('#exerciseId').val();
            window.location = "/bai-tap-nghe.html?page="+startPage+"&exerciseId="+exerciseId+"";
        });
    });
    $('#formUrl').submit(function (e) {    // chỉ submit form khi bấm nút "Xem đáp án"
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: '/ajax-bai-tap-dap-an.html',
            data: $(this).serialize(),
            dataType: 'html',
            success: function (res) {
                $('#result').html(res); // nếu thành công, controller trả vể trang result.jsp và được gán đè lên element có id=result ở trang detail.jsp
            },
            error: function (res) {
                console.log(res);
            }
        });
    });

    // twbs-pagination
    $('#pagination-demo').twbsPagination({
        totalPages: totalPages,
        startPage: startPage,
        visiblePages: 0,
        onPageClick: function (event, page) {
            if (page != startPage) {
                $('#page').val(page);
                var exerciseId = $('#exerciseId').val();
                // không dùng form.submit() vì đã dùng cho ajax
                // dùng window.location -> submit đến 1 URL, đồng thời truyền được các tham số để request nhận
                window.location = "/bai-tap-nghe.html?page="+page+"&exerciseId="+exerciseId+"";
            }
        }
    });
</script>
</body>
</html>
