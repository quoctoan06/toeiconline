package vn.myclass.controller.web;

import org.apache.commons.lang.StringUtils;
import vn.myclass.command.ListenGuidelineCommand;
import vn.myclass.core.dto.ListenGuidelineDTO;
import vn.myclass.core.web.common.WebConstant;
import vn.myclass.core.web.utils.FormUtil;
import vn.myclass.core.web.utils.RequestUtil;
import vn.myclass.core.web.utils.SingletonServiceUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/danh-sach-bai-huong-dan-nghe.html", "/noi-dung-bai-huong-dan-nghe.html"})
public class ListenGuidelineController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ListenGuidelineCommand command = FormUtil.populate(ListenGuidelineCommand.class, request);
        if(request.getParameter("listenguidelineId") != null) {
            String listenGuidelineIdStr = request.getParameter("listenguidelineId");
            ListenGuidelineDTO dto = SingletonServiceUtil.getListenGuidelineServiceInstance().findByListenGuidelineId(Integer.parseInt(listenGuidelineIdStr.toString()));
            command.setPojo(dto);
            request.setAttribute(WebConstant.FORM_ITEM, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/web/listenguideline/detail.jsp");
            rd.forward(request, response);
        } else {
            executeSearchListenGuideline(command);
            request.setAttribute(WebConstant.LIST_ITEMS, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/web/listenguideline/list.jsp");
            rd.forward(request, response);
        }
    }

    // hàm thực hiện search request
    // ta cần tính 2 tham số để twbs hiển thị phân trang: page (truyền vào từ client) và totalPages (totalItems / maxPageItems)
    // ta cần tính 2 tham số để truyền vào findByProperty(): firstItem và maxPageItems
    private void executeSearchListenGuideline(ListenGuidelineCommand command) {
        Map<String, Object> properties = buildMapProperties(command);
        command.setMaxPageItems(3);
        RequestUtil.initSearchBeanManual(command);
        Object[] objects = SingletonServiceUtil.getListenGuidelineServiceInstance().findListenGuidelineByProperty(properties, command.getSortExpression(),
                command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems());
        command.setListResult((List<ListenGuidelineDTO>) objects[1]);
        command.setTotalItems(Integer.parseInt(objects[0].toString()));
        command.setTotalPages((int) Math.ceil((double) command.getTotalItems() / command.getMaxPageItems()));
    }


    // hàm trả về danh sách các trường muốn tìm kiếm (để đưa danh sách này vào hàm findByProperty)
    private Map<String, Object> buildMapProperties(ListenGuidelineCommand command) {     // đối với trang ListenGuideline thì ta chỉ search theo trường title
        Map<String, Object> properties = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(command.getPojo().getTitle())) {      // khi bấm Search thì title chỉ có trường hợp empty, chứ không null
            properties.put("title", command.getPojo().getTitle());
        }
        return properties;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
