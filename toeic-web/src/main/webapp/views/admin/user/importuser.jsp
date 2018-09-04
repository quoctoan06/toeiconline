<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglib.jsp"%>
<c:url value="/admin-user-import-validate.html" var="validateExcel"/>
<c:url value="/admin-user-import.html" var="importExcel"/>
<html>
<head>
    <title><fmt:message key="label.user.import.excel" bundle="${lang}"/></title>
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
                <li><fmt:message key="label.user.list" bundle="${lang}"/></li>
                <li class="active"><fmt:message key="label.user.import.excel" bundle="${lang}"/></li>
            </ul><!-- /.breadcrumb -->
        </div>
        <div class="page-content">
            <div class="row">
                <div class="col-xs-12">
                    <c:if test="${messageResponse!=null}">
                        <div class="alert alert-block alert-${alert}">
                            <button type="button" class="close" data-dismiss="alert">
                                <i class="ace-icon fa fa-times"></i>
                            </button>
                                ${messageResponse}
                        </div>
                    </c:if>
                    <form action="${validateExcel}" method="post" enctype="multipart/form-data" id="formImport">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="col-sm-12">
                                    <input type="file" name="file"/>
                                    <br/>
                                    <button type="button" class="dt-button buttons-html5 btn btn-white btn-primary btn-bold" id="validateData">
                                        <fmt:message key="label.file.validate.import" bundle="${lang}"/>
                                    </button>
                                </div>
                            </div>
                        </div>
                        <c:if test="${not empty items.userImportDTOS}">     <%--khi list empty thì không hiển thị table--%>
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="table-responsive">
                                        <fmt:bundle basename="ApplicationResources">    <%--items as command--%>
                                            <display:table id="tableList" name="items.userImportDTOS" requestURI="${requestUrl}"
                                                           partialList="true" sort="external" size="${items.totalItems}" excludedParams="checkList"
                                                           pagesize="${items.maxPageItems}" export="false"
                                                           class="table table-fcv-ace table-striped table-bordered table-hover dataTable no-footer"
                                                           style="margin: 3em 0 1.5em;" cellspacing="0" cellpadding="0">
                                                <display:column headerClass="text-left" property="userName" titleKey="label.user.name"/>
                                                <display:column headerClass="text-left" property="password" titleKey="label.user.password"/>
                                                <display:column headerClass="text-left" property="fullName" titleKey="label.user.fullname"/>
                                                <display:column headerClass="text-left" property="roleName" titleKey="label.role.name"/>
                                                <display:column headerClass="text-left" property="error" titleKey="label.import.error"/>
                                            </display:table>
                                        </fmt:bundle>
                                    </div>
                                </div>
                            </div>
                            <button type="button" class="dt-button buttons-html5 btn btn-white btn-primary btn-bold" id="importData">
                                <fmt:message key="label.user.import" bundle="${lang}"/>
                            </button>
                        </c:if>

                        <input type="hidden" name="urlType" id="urlType"/>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('#validateData').click(function () {
            $('#urlType').val('read_excel');
            $('#formImport').submit();
        });
        $('#importData').click(function () {
            $('#urlType').val('import_data');
            $('#formImport').prop('enctype', false);    // khi import data vào database, ta chỉ cần form submit bình thường do không cần đọc file nữa
            $('#formImport').attr('action', '${importExcel}');
            $('#formImport').submit();
        })
    });
</script>
</body>
</html>
