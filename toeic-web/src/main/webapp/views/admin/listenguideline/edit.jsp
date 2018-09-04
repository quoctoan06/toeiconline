<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglib.jsp"%>
<c:url var="formUrl" value="/admin-guideline-listen-edit.html"/>
<html>
<head>
    <title><fmt:message key="label.guideline.listen.edit" bundle="${lang}"/></title>
    <style>
        .error {    /* khi vi phạm các rules trong jquery validator, thông báo tương ứng sẽ được hiển thị và có class="error" */
            color: red;
        }
    </style>
</head>
<body>
<div class="main-content">
    <div class="main-content-inner">
        <div class="breadcrumbs" id="breadcrumbs">
            <script type="text/javascript">
                try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
            </script>

            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-home home-icon"></i>
                    <a href="#"><fmt:message key="label.home" bundle="${lang}"/></a>
                </li>
                <li class="active"><fmt:message key="label.guideline.listen.edit" bundle="${lang}"/></li>
            </ul><!-- /.breadcrumb -->
        </div>
        <div class="page-content">
            <div class="row">
                <div class="col-xs-12">
                    <c:if test="${not empty messageResponse}">
                        <div class="alert alert-block alert-${alert}">
                            <button type="button" class="close" data-dismiss="alert">
                                <i class="ace-icon fa fa-times"></i>
                            </button>
                                ${messageResponse}
                        </div>
                    </c:if>
                    <form action="${formUrl}" method="post" enctype="multipart/form-data" id="formEdit">
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right"><fmt:message key="label.guideline.title" bundle="${lang}"/></label>
                            <div class="col-sm-9">
                                <input type="text" name="pojo.title" id="title" value="${item.pojo.title}"/>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right"><fmt:message key="label.upload.image" bundle="${lang}"/></label>
                            <div class="col-sm-9">
                                <input type="file" name="file" id="uploadImage"/>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="form-group">    <%--view image--%>
                            <label class="col-sm-3 control-label no-padding-right"><fmt:message key="label.image.view" bundle="${lang}"/></label>
                            <div class="col-sm-9">
                                <c:if test="${not empty item.pojo.image}">
                                    <c:set var="image" value="/repository/${item.pojo.image}"/>
                                </c:if>
                                <img src="${image}" id="viewImage" width="150px" height="150px">
                            </div>
                        </div>
                        <br/>
                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right"><fmt:message key="label.guideline.content" bundle="${lang}"/></label>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <c:if test="${not empty item.pojo.content}">    <%--phần content trong DB phải not null, nếu nó ko empty thì ta hiển thị ra--%>
                                    <c:set var="content" value="${item.pojo.content}"/>
                                </c:if>
                                <textarea name="pojo.content" cols="80" rows="10" id="listenGuidelineContent">${content}</textarea>
                            </div>
                        </div>
                        <br/>
                        <br/>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <input type="submit" class="btn btn-white btn-warning btn-bold" value="<fmt:message key="label.done" bundle="${lang}"/>"/>
                            </div>
                        </div>
                        <c:if test="${not empty item.pojo.listenGuidelineId}">
                            <input type="hidden" name="pojo.listenGuidelineId" value="${item.pojo.listenGuidelineId}"/>
                        </c:if>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var listenGuidelineId = '';
    <c:if test="${not empty item.pojo.listenGuidelineId}">
        listenGuidelineId = ${item.pojo.listenGuidelineId};
    </c:if>
    $(document).ready(function () {
        var editor = CKEDITOR.replace('listenGuidelineContent');  // nhúng CKEDITOR vào phần content
        CKFinder.setupCKEditor( editor, '/ckfinder/' );            // nhúng CKFinder vào CKEditor

        validateData();

        $('#uploadImage').change(function () {
            readURL(this, "viewImage");
        });
    });

    // hàm validate dùng jquery validator plugin
    function validateData() {
        $('#formEdit').validate({   // form bao tất cả các thẻ muốn validate
            ignore: [],
            rules: [],      // bắt sự kiện
            message: []     // thông báo ra màn hình
        });
        $( "#title" ).rules( "add", {
            required: true,
            messages: {
                required: '<fmt:message key="label.title.not.empty" bundle="${lang}"/>'
            }
        });
        if(listenGuidelineId == '') {   // chỉ validate khi Add new, còn Edit thì không cần
            $( "#uploadImage" ).rules( "add", {
                required: true,
                messages: {
                    required: '<fmt:message key="label.image.not.empty" bundle="${lang}"/>'
                }
            });
        }
        $( "#listenGuidelineContent" ).rules( "add", {
            required: function () {     // cần function vì content đã replace bằng template của CKEDITOR (cần gọi ignore trong form)
                CKEDITOR.instances.listenGuidelineContent.updateElement();
            },
            messages: {
                required: '<fmt:message key="label.content.not.empty" bundle="${lang}"/>'
            }
        });
    }

    // hàm cho phép xem ảnh ngay khi upload
    // nhận kết quả đọc file từ servlet DisplayImage
    function readURL(input, imageId) {  // input là đối tượng ta chọn để upload file, imageId là nơi đưa dữ liệu vào để hiển thị ra
        if(input.files && input.files[0]) {     // chỉ view được ảnh sau khi đã chọn upload file, input.files[0] là file đầu tiên (đang được tải lên) trong input
            // Sử dụng API FileReader để đọc file (image, text,...)
            // As this is an asynchronous method we need to setup an event listener for when the file has finished loading.
            // When the onload event is called we can examine the result property of our FileReader instance to get the file’s contents.
            // The readAsDataURL() method takes in a File or Blob and produces a data URL.
            var reader = new FileReader();
            reader.onload = function (ev) {     // onload là sự kiện image đã được load xong. Khi đó có thể lấy URL đến file đó qua reader.result
                $('#' + imageId).attr('src', reader.result);
            }
            reader.readAsDataURL(input.files[0]);   // reader đọc nội dung file đang được tải lên, và trả về 1 URL đến file đó
        }
    }
</script>
</body>
</html>
