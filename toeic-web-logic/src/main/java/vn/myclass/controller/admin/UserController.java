package vn.myclass.controller.admin;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.myclass.command.UserCommand;
import vn.myclass.core.common.utils.ExcelPoiUtil;
import vn.myclass.core.common.utils.SessionUtil;
import vn.myclass.core.common.utils.UploadUtil;
import vn.myclass.core.dto.RoleDTO;
import vn.myclass.core.dto.UserDTO;
import vn.myclass.core.dto.UserImportDTO;
import vn.myclass.core.service.RoleService;
import vn.myclass.core.service.UserService;
import vn.myclass.core.service.impl.RoleServiceImpl;
import vn.myclass.core.service.impl.UserServiceImpl;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/admin-user-list.html", "/ajax-admin-user-edit.html", "/admin-user-import.html", "/admin-user-import-validate.html"})
public class UserController extends HttpServlet {
    private final Logger log = Logger.getLogger(this.getClass());
    // hằng chỉ dùng trong Controller này thì khai báo ở đây
    private final String SHOW_IMPORT_USER = "show_import_user"; // hiển thị view upload file ở list.jsp
    private final String READ_EXCEL = "read_excel";             // đọc và validate trang sheet, trả về sheetValues qua key LIST_USER_IMPORT và urlType=VALIDATE_IMPORT
    private final String VALIDATE_IMPORT = "validate_import";   // hiển thị các lỗi sau khi validate file excel ở doPost
    private final String LIST_USER_IMPORT = "list_user_import"; // key chứa sheetValues trong Session
    private final String IMPORT_DATA = "import_data";           // sau khi validate xong data, insert user vào database
    ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserCommand command = FormUtil.populate(UserCommand.class, request);
        UserDTO pojo = command.getPojo();
        if(command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_LIST)) {
            Map<String, Object> mapProperty = new HashMap<String, Object>();
            RequestUtil.initSearchBean(request, command);
            Object[] objects = SingletonServiceUtil.getUserServiceInstance().findByProperty(mapProperty, command.getSortExpression(), command.getSortDirection(),
                    command.getFirstItem(), command.getMaxPageItems());
            command.setListResult((List<UserDTO>) objects[1]);
            command.setTotalItems(Integer.parseInt(objects[0].toString()));
            request.setAttribute(WebConstant.LIST_ITEMS, command);

            if(command.getCrudaction() != null) {
                Map<String, String> mapMessage = buildMapRedirectMessage(resourceBundle);
                WebCommonUtil.addRedirectMessage(request, command.getCrudaction(), mapMessage);
            }

            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/list.jsp");
            rd.forward(request, response);
        } else if(command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_EDIT)) {
             if(pojo != null && pojo.getUserId() != null) {  // null tức chọn Add new, không null là chọn Edit
                command.setPojo(SingletonServiceUtil.getUserServiceInstance().findById(pojo.getUserId()));
             }
            command.setRoleDTOS(SingletonServiceUtil.getRoleServiceInstance().findAll());     // danh sách role
            request.setAttribute(WebConstant.FORM_ITEM, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/edit.jsp");
            rd.forward(request, response);
        } else if(command.getUrlType() != null && command.getUrlType().equals(SHOW_IMPORT_USER)) {  // hiển thị view upload file
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/importuser.jsp");
            rd.forward(request, response);
        } else if(command.getUrlType() != null && command.getUrlType().equals(VALIDATE_IMPORT)) {   // hiển thị các lỗi sau khi validate file excel ở doPost
            // lấy list chứa Sheet Value đã đọc trong doPost
            List<UserImportDTO> sheetValues = (List<UserImportDTO>) SessionUtil.getInstance().getValue(request, LIST_USER_IMPORT);
            command.setUserImportDTOS(returnListUserImport(command, sheetValues, request));
            request.setAttribute(WebConstant.LIST_ITEMS, command);
            RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/importuser.jsp");
            rd.forward(request, response);
        }
    }

    // xử lý phân trang rồi trả về list user đọc từ file excel
    private List<UserImportDTO> returnListUserImport(UserCommand command, List<UserImportDTO> sheetValues, HttpServletRequest request) {
        command.setMaxPageItems(3);
        RequestUtil.initSearchBean(request, command);
        command.setTotalItems(sheetValues.size());

        // xử lý phân trang bằng tay (không cần thiết, chỉ cần xử lý phân trang bằng RequestUtil, setMaxPageItems và setTotalItems là đủ rồi)
        int firstIndex = command.getFirstItem();
        if (firstIndex > command.getTotalItems()) {
            firstIndex = 0;
            command.setFirstItem(firstIndex);
        }
        int lastIndex = command.getFirstItem() + command.getMaxPageItems();
        if (sheetValues.size() > 0) {
            if (lastIndex > sheetValues.size()) {
                lastIndex = sheetValues.size();
            }
        }
        return sheetValues.subList(firstIndex, lastIndex);
    }

    private Map<String, String> buildMapRedirectMessage(ResourceBundle resourceBundle) {
        Map<String, String> mapMessage = new HashMap<String, String>();
        mapMessage.put(WebConstant.REDIRECT_INSERT, resourceBundle.getString("label.user.message.add.success"));
        mapMessage.put(WebConstant.REDIRECT_UPDATE, resourceBundle.getString("label.user.message.update.success"));
        mapMessage.put(WebConstant.REDIRECT_DELETE, resourceBundle.getString("label.user.message.delete.success"));
        mapMessage.put(WebConstant.REDIRECT_ERROR, resourceBundle.getString("label.message.error"));
        return mapMessage;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UploadUtil uploadUtil = new UploadUtil();
        Set<String> value = new HashSet<String>();
        value.add("urlType");
        Object[] objects = uploadUtil.writeOrUploadFile(request, value, WebConstant.EXCEL);
        try {
            UserCommand command = FormUtil.populate(UserCommand.class, request);
            UserDTO pojo = command.getPojo();
            if(command.getUrlType() != null && command.getUrlType().equals(WebConstant.URL_EDIT)) {
                if(command.getCrudaction() != null && command.getCrudaction().equals(WebConstant.INSERT_UPDATE)) {  // nếu chọn Add new hoặc Edit
                    RoleDTO roleDTO = new RoleDTO();
                    roleDTO.setRoleId(command.getRoleId());
                    pojo.setRoleDTO(roleDTO);
                    if(pojo != null && pojo.getUserId() != null) {
                        // update user
                        UserDTO oldUserDTO = SingletonServiceUtil.getUserServiceInstance().findById(pojo.getUserId());
                        pojo.setCreatedDate(oldUserDTO.getCreatedDate());
                        SingletonServiceUtil.getUserServiceInstance().updateUser(pojo);
                        request.setAttribute(WebConstant.MESSAGE_RESPONSE, WebConstant.REDIRECT_UPDATE);
                    } else {
                        // save new user
                        SingletonServiceUtil.getUserServiceInstance().saveUser(pojo);
                        request.setAttribute(WebConstant.MESSAGE_RESPONSE, WebConstant.REDIRECT_INSERT);
                    }
                }
                RequestDispatcher rd = request.getRequestDispatcher("/views/admin/user/edit.jsp");
                rd.forward(request, response);
            }

            if(objects != null) {
                String urlType = null;
                Map<String, String> mapValue = (Map<String, String>) objects[3];
                for(Map.Entry<String, String> item : mapValue.entrySet()) {
                    if(item.getKey().equals("urlType")) {
                        urlType = item.getValue();
                    }
                }
                if(urlType != null && urlType.equals(READ_EXCEL)) {
                    String fileLocation = (String) objects[1];
                    String fileName = (String) objects[2];
                    // get sheet values from excel file
                    List<UserImportDTO> sheetValues = returnValueOfSheetFromExcelFile(fileName, fileLocation);
                    // validate data
                    validateData(sheetValues);

                    // lưu list trên vào session và gửi đến doGet, ở doGet sẽ lấy list ra và gửi đến trang jsp
                    SessionUtil.getInstance().putValue(request, LIST_USER_IMPORT, sheetValues);
                    response.sendRedirect("/admin-user-import-validate.html?urlType=validate_import");
                } else if(command.getUrlType() != null && command.getUrlType().equals(IMPORT_DATA)) {
                    // list sheetValues đã được lưu trong session
                    List<UserImportDTO> sheetValues = (List<UserImportDTO>) SessionUtil.getInstance().getValue(request, LIST_USER_IMPORT);
                    // import into database
                    SingletonServiceUtil.getUserServiceInstance().saveUserImport(sheetValues);
                    // sau khi lấy ra thì cần xoá attribute trong session
                    SessionUtil.getInstance().remove(request, LIST_USER_IMPORT);
                    response.sendRedirect("/admin-user-list.html?urlType=url_list");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            request.setAttribute(WebConstant.MESSAGE_RESPONSE, WebConstant.REDIRECT_ERROR);
        }
    }

    // validate data
    private void validateData(List<UserImportDTO> sheetValues) {
        Set<String> stringSet = new HashSet<String>();
        for(UserImportDTO item : sheetValues) {
            validateRequiredFields(item);
            validateDuplicateUsernameBetweenRowOfSheet(item, stringSet);
        }
        // validation 3: username trong file excel không được duplicate với các username trong database
        // validation 4: rolename trong file excel nếu có giá trị khác với tập giá trị có sẵn trong database(vd: manager) thì phải báo lỗi
        SingletonServiceUtil.getUserServiceInstance().validateImportUser(sheetValues);
    }

    // validation thứ 1: username, password và rolename không được trống
    private void validateRequiredFields(UserImportDTO item) {
        String message = "";
        if(StringUtils.isBlank(item.getUserName())) {
            message += "</br>";
            message += resourceBundle.getString("label.username.not.empty");
        }
        if(StringUtils.isBlank(item.getPassword())) {
            message += "</br>";
            message += resourceBundle.getString("label.password.not.empty");
        }
        if(StringUtils.isBlank(item.getRoleName())) {
            message += "</br>";
            message += resourceBundle.getString("label.rolename.not.empty");
        }
        if(StringUtils.isNotBlank(message)) {
            item.setValid(false);   // nếu message không empty, tức có lỗi thì ta không kiểm tra những lỗi khác nữa
        }
        item.setError(message);
    }

    // validation thứ 2: username các hàng trong sheet không duplicate
    private void validateDuplicateUsernameBetweenRowOfSheet(UserImportDTO item, Set<String> stringSet) {
        String message = item.getError();
        if(!stringSet.contains(item.getUserName())) {   // stringSet ban đầu rỗng nên đầu tiên cần add vào
            stringSet.add(item.getUserName());
        } else {
            if(item.isValid()) {    // nếu valid == true, tức những validation khác đã OK thì ta mới thực hiện validation này
                message += "</br>";
                message += resourceBundle.getString("label.username.sheet.duplicate");
            }
        }
        if(StringUtils.isNotBlank(message)) {
            item.setValid(false);
            item.setError(message);
        }
    }

    // hàm trả về tập giá trị của sheet đầu tiên (sheet 0) trong file excel
    private List<UserImportDTO> returnValueOfSheetFromExcelFile(String fileName, String fileLocation) throws IOException {
        Workbook workbook = ExcelPoiUtil.getWorkbook(fileName, fileLocation);
        Sheet sheet = workbook.getSheetAt(0);      // first sheet
        List<UserImportDTO> sheetValues = new ArrayList<UserImportDTO>();   // list chứa giá trị của 1 sheet của file excel
        // chạy từ row 1 vì row 0 là tiêu đề
        // chương trình đọc từng row, sau đó đọc từng cell trên row đó
        for(int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            UserImportDTO userImportDTO = readDataOfOneRow(row);
            sheetValues.add(userImportDTO);
        }
        return sheetValues;
    }

    private UserImportDTO readDataOfOneRow(Row row) {
        UserImportDTO userImportDTO = new UserImportDTO();
        userImportDTO.setUserName(ExcelPoiUtil.getCellValue(row.getCell(0)));   // first cell
        userImportDTO.setPassword(ExcelPoiUtil.getCellValue(row.getCell(1)));
        userImportDTO.setFullName(ExcelPoiUtil.getCellValue(row.getCell(2)));
        userImportDTO.setRoleName(ExcelPoiUtil.getCellValue(row.getCell(3)));
        return userImportDTO;
    }
}
