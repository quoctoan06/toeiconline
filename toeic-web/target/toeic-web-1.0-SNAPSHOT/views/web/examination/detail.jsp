<%@ page import="vn.myclass.core.web.common.WebConstant" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<c:set var="PHOTO" value="<%= WebConstant.EXAMINATION_TYPE_PHOTO %>"/>
<c:set var="QUESTION_RESPONSE" value="<%= WebConstant.EXAMINATION_TYPE_QUESTION_RESPONSE %>"/>
<c:set var="SINGLE_PASSAGE" value="<%= WebConstant.EXAMINATION_TYPE_SINGLE_PASSAGE %>"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="label.home" bundle="${lang}"/></title>
    <script src="<c:url value="/template/countdown/countdown.js"/>" type="text/javascript"></script>
</head>
<body>
<form action="<c:url value="/bai-thi-dap-an.html"/>" method="post" id="formUrl">
    <!-- START COUNTDOWN -->
    <script type="text/javascript">
        function doneHandler(result) {
            var year = result.getFullYear();
            var month = result.getMonth() + 1; // bump by 1 for humans
            var day = result.getDate();
            var h = result.getHours();
            var m = result.getMinutes();
            var s = result.getSeconds();

            var UTC = result.toString();

            var output = UTC + "\n";
            output += "year: " + year + "\n";
            output += "month: " + month + "\n";
            output += "day: " + day + "\n";
            output += "h: " + h + "\n";
            output += "m: " + m + "\n";
            output += "s: " + s + "\n";
        }
        var myCountdownTest = new Countdown({
            time: 30,       // đồng hồ đếm ngược 30 giây
            width	: 300,
            height	: 50,
            onComplete : doneHandler
        });
    </script>
    <!-- END COUNTDOWN -->

    <div class="image group">
        <div class="grid news_desc">    <%--phần câu hỏi và các câu trả lời--%>
            <div style="overflow: auto; height: 500px"> <%--giới hạn chiều dài khi show danh sách ra--%>
             <c:forEach var="item" items="${items.listResult}">
                 <c:if test="${item.number != null}">
                     <b>${item.number}. ${item.question}</b>
                 </c:if>
                 <c:if test="${item.number == null}">  <%--câu hỏi của đoạn paragraph không có số thứ tự, còn lại có hết--%>
                     <b>${item.question}</b>
                 </c:if>
                <c:choose>
                    <c:when test="${item.type == PHOTO}">   <%--câu hỏi dạng PHOTO có cả image và audio--%>
                        <p>
                            <img src="<c:url value="/repository/${item.image}"/>" width="300px" height="150px">
                        </p>
                        <p>
                            <audio controls>
                                <source src="<c:url value="/repository/${item.audio}"/>" type="audio/mpeg">
                            </audio>
                        </p>
                    </c:when>
                    <c:when test="${item.type == QUESTION_RESPONSE}">   <%--câu hỏi dạng QUESTION_RESPONSE chỉ có audio--%>
                        <p>
                            <audio controls>
                                <source src="<c:url value="/repository/${item.audio}"/>" type="audio/mpeg">
                            </audio>
                        </p>
                    </c:when>
                    <c:when test="${item.type == SINGLE_PASSAGE}">  <%--câu hỏi dạng SINGLE_PASSAGE chỉ có đoạn văn--%>
                        <p>
                                ${item.paragraph}
                        </p>
                        <c:if test="${empty item.paragraph}">  <%--trong DB, phần có paragraph thì không có các option và ngược lại--%>
                            <p>                                <%--2 dạng bài thi PHOTO và QUESTION_RESPONSE, option là script của phần nghe nên không được show ra--%>
                                    ${item.option1}
                            </p>
                            <p>
                                    ${item.option2}
                            </p>
                            <p>
                                    ${item.option3}
                            </p>
                            <p>
                                    ${item.option4}
                            </p>
                        </c:if>
                    </c:when>
                </c:choose>
            </c:forEach>
            </div>
        </div>
        <div class="grid images_3_of_1">    <%--phần chọn đáp án--%>
            <div style="overflow: auto; height: 500px" >
                <c:forEach var="item" items="${items.listResult}">
                    <c:if test="${not empty item.correctAnswer}">   <%--có correctAnswer tức là phần câu hỏi, khi đó hiển thị ra các ô để chọn đáp án --%>
                        ${item.number}.&nbsp;&nbsp;&nbsp;
                        A <input type="radio" name="answerOfUser[${item.examinationQuestionId}]" value="A"> <%--gắn id để truyền về controller biết được user chọn đáp án gì cho câu hỏi nào--%>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        B <input type="radio" name="answerOfUser[${item.examinationQuestionId}]" value="B">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        C <input type="radio" name="answerOfUser[${item.examinationQuestionId}]" value="C">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        D <input type="radio" name="answerOfUser[${item.examinationQuestionId}]" value="D">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <br/>
                        <br/>
                        <br/>
                    </c:if>
                </c:forEach>
            </div>
            <input type="hidden" name="examinationId" value="${items.examinationId}"/>
            <input type="submit" class="btn btn-primary" value="<fmt:message key='label.examination.submit' bundle='${lang}'/>"/>
        </div>
    </div>
</form>
<script type="text/javascript">
    $(document).ready(function () {
    });
    $(function(){
        setTimeout(function(){  // đủ 30s form sẽ tự submit
            $('#formUrl').submit();
        },30000);
    });
</script>
</body>
</html>
