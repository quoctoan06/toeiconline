<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglib.jsp" %>
<c:url var="editUserUrl" value="/ajax-admin-user-edit.html">
    <c:param name="urlType" value="url_edit"/>
</c:url>
<c:choose>  <%--khi thông báo success hay error thì sẽ có messageResponse, ngược lại hiển thị form nhập--%>
    <c:when test="${not empty messageResponse}">
        ${messageResponse}
    </c:when>
    <c:otherwise>
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <c:if test="${not empty item.pojo.userId}">
                        <h4 class="modal-title"><fmt:message key="label.user.edit" bundle="${lang}"/></h4>
                    </c:if>
                    <c:if test="${empty item.pojo.userId}">
                        <h4 class="modal-title"><fmt:message key="label.user.add" bundle="${lang}"/></h4>
                    </c:if>
                </div>
                <form action="${editUserUrl}" method="post" id="editUserForm">
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="md-form">
                                    <input type="text" class="form-control" placeholder="<fmt:message key='label.user.name' bundle='${lang}'/>"
                                           value="${item.pojo.name}" id="username" name="pojo.name" required/>
                                </div>
                            </div>
                            <br/>
                            <br/>
                            <div class="col-md-12">
                                <div class="md-form">
                                    <input type="text" class="form-control" placeholder="<fmt:message key='label.user.fullname' bundle='${lang}'/>"
                                           value="${item.pojo.fullname}" name="pojo.fullname"/>
                                </div>
                            </div>
                            <br/>
                            <br/>
                            <div class="col-md-12">
                                <div class="md-form">
                                    <input type="password" class="form-control" placeholder="<fmt:message key='label.user.password' bundle='${lang}'/>"
                                           value="${item.pojo.password}" id="password" name="pojo.password" required/>
                                </div>
                            </div>
                            <br/>
                            <br/>
                            <div class="col-md-12">
                                <div class="md-form">
                                    <c:choose>
                                        <c:when test="${not empty item.pojo.userId}">   <%--nếu là Edit--%>
                                            <select id="role" name="roleId">
                                                <option value="${item.pojo.roleDTO.roleId}">${item.pojo.roleDTO.name}</option>  <%--hiển thị role đã có sẵn của user--%>
                                                    <%--sau đó hiển thị những role còn lại--%>
                                                <c:forEach items="${item.roleDTOS}" var="roleItem">                     <%--items là danh sách đổ vào--%>
                                                    <c:if test="${roleItem.roleId != item.pojo.roleDTO.roleId}">
                                                        <option value="${roleItem.roleId}">${roleItem.name}</option>    <%--value là giá trị sẽ truyền vào Controller và lưu vào database--%>
                                                    </c:if>
                                                </c:forEach>
                                            </select>
                                        </c:when>
                                        <c:otherwise>               <%--ngược lại nếu là Add new, hiển thị tất cả role trong database--%>
                                            <select id="role" name="roleId">
                                                <option><fmt:message key="label.option.role" bundle="${lang}"/></option>
                                                <c:forEach items="${item.roleDTOS}" var="roleItem">
                                                    <option value="${roleItem.roleId}">${roleItem.name}</option>
                                                </c:forEach>
                                            </select>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                    <c:if test="${not empty item.pojo.userId}">     <%--nếu userId == empty thì sẽ bị xem bằng 0--%>
                        <input type="hidden" name="pojo.userId" value="${item.pojo.userId}"/>
                    </c:if>
                    <input type="hidden" name="crudaction" id="crudactionInsertOrUpdate"/>
                </form>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal"><fmt:message key="label.close" bundle="${lang}"/></button>
                    <button type="button" id="btnSave" class="btn btn-primary"><fmt:message key="label.save" bundle="${lang}"/></button>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>