<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:url var="urlList" value="/danh-sach-bai-thi.html"/>

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="label.examination.list" bundle="${lang}"/></title>
</head>
<body>
<form action="${urlList}" method="get" id="formUrl">
    <div class="wrap">
        <div class="main">
            <div class="content">
                <div class="col span_2_of_3">
                    <div class="contact-form">
                        <div>
                            <span>
                                <input name="pojo.name" type="text" class="textbox" value="${items.pojo.name}"/>
                            </span>
                        </div>
                        <div>
                            <button class="btn btn-sm btn-success">
                                <fmt:message key="label.search" bundle="${lang}"/>
                            </button>
                        </div>
                    </div>
                </div>
                <c:forEach var="item" items="${items.listResult}">
                    <div class="image group">
                        <div class="grid news_desc">
                            <h3>${item.name}</h3>
                            <c:url value="/bai-thi-thuc-hanh.html" var="detailUrl">
                                <c:param name="examinationId" value="${item.examinationId}"/>
                            </c:url>
                            <c:if test="${not empty login_name}">   <%--cần login mới được làm bài thi--%>
                                <h4><span><a href="${detailUrl}">Làm bài thi</a></span></h4>
                            </c:if>
                            <c:if test="${empty login_name}">
                                <h4><span><fmt:message key="label.examination.login.require" bundle="${lang}"/></span></h4>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
                <ul id="pagination-demo" class="pagination-sm"></ul>    <%--twbs-pagination--%>
            </div>
        </div>
    </div>
    <input type="hidden" name="page" id="page"/>
</form>
<script type="text/javascript">
    var totalPages = ${items.totalPages};
    var startPage = ${items.page};
    $(document).ready(function () {
    });

    // hàm sử dụng jQuery plugin để phân trang (twbs-pagination)
    $(function () {
        var obj = $('#pagination-demo').twbsPagination({
            totalPages: totalPages,
            startPage: startPage,
            visiblePages: 3,
            onPageClick: function (event, page) {   // page là trang hiện tại đang đứng
                if (page != startPage) {            // điều kiện ngăn không cho form submit liên tục (vì khi vào trang thì onPageClick sẽ được gọi ngay lập tức)
                    $('#page').val(page);
                    $('#formUrl').submit();
                }
            }
        });
    });
</script>
</body>
</html>