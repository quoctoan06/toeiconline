package vn.myclass.controller.admin;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import vn.myclass.command.ListenGuidelineCommand;
import vn.myclass.core.common.utils.UploadUtil;
import vn.myclass.core.dto.ListenGuidelineDTO;
import vn.myclass.core.service.ListenGuidelineService;
import vn.myclass.core.service.impl.ListenGuidelineServiceImpl;
import vn.myclass.core.web.common.WebConstant;
import vn.myclass.core.web.utils.FormUtil;
import vn.myclass.core.web.utils.RequestUtil;
import vn.myclass.core.web.utils.SingletonServiceUtil;
import vn.myclass.core.web.utils.WebCommonUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/admin-guideline-listen-list.html", "/admin-guideline-listen-edit.html"})
public class ListenGuidelineController extends HttpServlet {
    private final Logger log = Logger.getLogger(this.getClass());
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ListenGuidelineCommand command = FormUtil.populate(ListenGuidelineCommand.class, request);
        if(command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_LIST)) {
            if(command.getCrudaction() != null && command.getCrudaction().equals(WebConstant.REDIRECT_DELETE)) { // xử lý chức năng xoá
                List<Integer> ids = new ArrayList<Integer>();
                for(String item : command.getCheckList()) {
                    ids.add(Integer.parseInt(item));
                }
                Integer result = SingletonServiceUtil.getListenGuidelineServiceInstance().delete(ids);  // trả về số lượng phần tử đã xoá
                if(result != ids.size()) {  // thông báo lỗi
                    command.setCrudaction(WebConstant.REDIRECT_ERROR);
                }
            }
            // xử lý chức năng show danh sách và chức năng tìm kiếm theo title
            executeSearchListenGuideline(request, command);
            if(command.getCrudaction() != null) {
                Map<String, String> mapMessage = buildMapRedirectMessage(resourceBundle);
                WebCommonUtil.addRedirectMessage(request, command.getCrudaction(), mapMessage);
            }
            request.setAttribute(WebConstant.LIST_ITEMS, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/listenguideline/list.jsp");
            rd.forward(request, response);
        } else if(command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_EDIT)) {
            if(command.getPojo() != null && command.getPojo().getListenGuidelineId() != null) {     // nếu click nút Edit
                command.setPojo(SingletonServiceUtil.getListenGuidelineServiceInstance().findByListenGuidelineId(command.getPojo().getListenGuidelineId()));
            }
            request.setAttribute(WebConstant.FORM_ITEM, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/listenguideline/edit.jsp");
            rd.forward(request, response);
        }
    }

    // hàm thực hiện search request
    private void executeSearchListenGuideline(HttpServletRequest request, ListenGuidelineCommand command) {
        Map<String, Object> properties = buildMapProperties(command);
        RequestUtil.initSearchBean(request, command);
        Object[] objects = SingletonServiceUtil.getListenGuidelineServiceInstance().findListenGuidelineByProperty(properties,
                command.getSortExpression(), command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems());
        command.setListResult((List<ListenGuidelineDTO>) objects[1]);
        command.setTotalItems(Integer.parseInt(objects[0].toString()));
    }

    // hàm trả về danh sách các trường muốn tìm kiếm (để đưa danh sách này vào hàm findByProperty)
    private Map<String, Object> buildMapProperties(ListenGuidelineCommand command) {     // đối với trang ListenGuideline thì ta chỉ search theo trường title
        Map<String, Object> properties = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(command.getPojo().getTitle())) {      // khi bấm Search thì title chỉ có trường hợp empty, chứ không null
            properties.put("title", command.getPojo().getTitle());
        }
        return properties;
    }

    // map chứa thông báo cùng key tương ứng
    private Map<String, String> buildMapRedirectMessage(ResourceBundle resourceBundle) {
        Map<String, String> mapMessage = new HashMap<String, String>();
        mapMessage.put(WebConstant.REDIRECT_INSERT, resourceBundle.getString("label.guideline.listen.message.add.success"));
        mapMessage.put(WebConstant.REDIRECT_UPDATE, resourceBundle.getString("label.guideline.listen.message.update.success"));
        mapMessage.put(WebConstant.REDIRECT_DELETE, resourceBundle.getString("label.guideline.listen.message.delete.success"));
        mapMessage.put(WebConstant.REDIRECT_ERROR, resourceBundle.getString("label.message.error"));
        return mapMessage;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ListenGuidelineCommand command = new ListenGuidelineCommand();
        UploadUtil uploadUtil = new UploadUtil();
        Set<String> titleValue = buildSetValueListenGuideline();    // những trường cần lấy
        Object[] objects = uploadUtil.writeOrUploadFile(request, titleValue, WebConstant.LISTENGUIDELINE);
        boolean checkUploadImageStatus = (Boolean)objects[0];
        if(!checkUploadImageStatus) {   // nếu upload file bị lỗi
            response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_error");     // thì thông báo lỗi ở trang list.jsp
        } else {    // ngược lại upload thành công và trả về được map chứa giá trị các trường
            ListenGuidelineDTO dto = command.getPojo();
            Map<String, String> mapValue = (Map<String, String>) objects[3];    // map chứa các thuộc tính cùng giá trị của các regular form field(text...)
            if(StringUtils.isNotBlank(objects[2].toString())) {     // nếu không upload ảnh thì sẽ trả về tên file ảnh là empty nên cần check
                dto.setImage(objects[2].toString());    // lấy tên file ảnh
            }
            dto = returnValueListenGuidelineDTO(dto, mapValue);
            if(dto != null) {   // khi đã có dữ liệu
                if(dto.getListenGuidelineId() != null) {
                    // update
                    try {
                        ListenGuidelineDTO oldListenGuidelineDTO = SingletonServiceUtil.getListenGuidelineServiceInstance().findByListenGuidelineId(dto.getListenGuidelineId());
                        if(dto.getImage() == null) {    // nếu ta không upload hình mới thì khi cập nhật theo dto trên image sẽ empty
                            dto.setImage(oldListenGuidelineDTO.getImage());     // nên ta lấy hình cũ đưa vào dto này
                        }
                        dto.setCreatedDate(oldListenGuidelineDTO.getCreatedDate());     // ngày tạo không đổi
                        ListenGuidelineDTO result = SingletonServiceUtil.getListenGuidelineServiceInstance().updateListenGuideline(dto);
                        if(result != null) {
                            response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_update");
                        } else {
                            response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_error");
                        }
                    } catch (ObjectNotFoundException e) {
                        log.error(e.getMessage(), e);
                        response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_error");
                    }
                } else {
                    // add new
                    try {   // bắt ngoại lệ từ tầng Service và xử lý ngoại lệ
                        SingletonServiceUtil.getListenGuidelineServiceInstance().saveListenGuideline(dto);
                        response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_insert");
                    } catch (ConstraintViolationException e) {  // ngoại lệ này tức là người dùng không nhập giá trị vào view và submit (giả sử chưa validate bằng jquery)
                        log.error(e.getMessage(), e);           // khi lưu vào database một trong các trường NOT NULL bị nhận giá trị null
                        response.sendRedirect("/admin-guideline-listen-list.html?urlType=url_list&&crudaction=redirect_error");
                    }
                }
            }

        }
    }

    // lưu giá trị các thuộc tính nhập từ view vào DTO
    private ListenGuidelineDTO returnValueListenGuidelineDTO(ListenGuidelineDTO dto, Map<String, String> mapValue) {
        for(Map.Entry<String, String> item : mapValue.entrySet()) {
            if(item.getKey().equals("pojo.title")) {
                dto.setTitle(item.getValue());
            } else if(item.getKey().equals("pojo.content")) {
                dto.setContent(item.getValue());
            } else if(item.getKey().equals("pojo.listenGuidelineId")) {     // có thêm trường này khi chọn Edit, không có khi chọn Add new
                dto.setListenGuidelineId(Integer.parseInt(item.getValue().toString()));
            }
        }
        return dto;
    }

    // tập hợp những trường cần lấy
    private Set<String> buildSetValueListenGuideline() {
        Set<String> returnValue = new HashSet<String>();
        returnValue.add("pojo.title");
        returnValue.add("pojo.content");
        returnValue.add("pojo.listenGuidelineId");
        return returnValue;
    }
}
