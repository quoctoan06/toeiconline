<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/common/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:url var="urlList" value="/danh-sach-bai-huong-dan-nghe.html"/>

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="label.guideline.listen.list" bundle="${lang}"/></title>
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
                                <input name="pojo.title" type="text" class="textbox" value="${items.pojo.title}"/>
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
                        <div class="grid images_3_of_1">
                            <img src="<c:url value="/repository/${item.image}"/>" alt=""/>
                        </div>
                        <div class="grid news_desc">
                            <h3>${item.title}</h3>
                            <c:url value="/noi-dung-bai-huong-dan-nghe.html" var="detailUrl">
                                <c:param name="listenguidelineId" value="${item.listenGuidelineId}"/>
                            </c:url>
                            <h4>
                                <span><a href="${detailUrl}">Chi tiết bài hướng dẫn nghe</a></span>
                            </h4>
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
    var startPage = ${items.page};      // là page hiện tại đang đứng, được cập nhật lại giá trị khi sang trang khác
    $(document).ready(function () {
    });

    // hàm sử dụng jQuery plugin để phân trang (twbs-pagination)
    $(function () {
        var obj = $('#pagination-demo').twbsPagination({
            totalPages: totalPages,
            startPage: startPage,
            visiblePages: 3,
            onPageClick: function (event, page) {   // page sẽ có giá trị của trang hiện tại đang đứng, và sẽ nhận giá trị khi bấm chọn thanh phân trang
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