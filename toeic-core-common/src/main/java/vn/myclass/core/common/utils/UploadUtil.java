package vn.myclass.core.common.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import vn.myclass.core.common.constant.CoreConstant;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UploadUtil {
    private final Logger log = Logger.getLogger(this.getClass());
    private final int maxMemorySize = 1024 * 1024 * 3;      // 3MB - dung lượng tối đa của nơi lưu file tạm thời
    private final int maxRequestSize = 1024 * 1024 * 50;    // 50MB - dung lượng tối đa của file upload lên

    // thêm mới nếu chưa có hoặc cập nhật lại
    // Advanced: không chỉ upload file mà còn lấy được giá trị các thuộc tính của các regular form field
    // titleValue chứa tên các trường cần lấy ra, các trường này sau đó sẽ được thêm vào Map
    // parentFolder là tên folder cha chứa các folder con (sẽ chỉ có 1 folder cha)
    // childFolder là folder con sẽ lưu file được upload lên (sẽ có nhiều folder con như listenguideline, excel,...)
    public Object[] writeOrUploadFile(HttpServletRequest request, Set<String> titleValue, String childFolder) {
        String parentFolder = "/" + CoreConstant.FOLDER_UPLOAD;
        checkAndCreateFolder(parentFolder, childFolder);
        boolean check = true;
        String fileLocation = null;
        String name = null;
        Map<String, String> mapReturnValue = new HashMap<String, String>();     // <name, value>

        // kiểm tra nếu request là multipart hay không (phần encrypt ở form)
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if(!isMultipart) {
            System.out.println("Encrypt type is not multipart/form-data");
            check = false;
        }
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Set factory constraints
        factory.setSizeThreshold(maxMemorySize);            // set dung lượng tối đa của bộ nhớ tạm thời
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));   // set bộ nhớ tạm thời để chứa file

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Set overall request size constraint
        upload.setSizeMax(maxRequestSize);

        try {
            // Parse the request - nhận tất cả giá trị truyền từ form input trong request(bao gồm file, text,...)
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items) {   // duyệt qua hết tất cả các trường
                if (!item.isFormField()) {   // isFormField = true đối với regular form field (text...) và false đối với form file upload
                    name = item.getName();   // nếu không upload ảnh thì item.getName() trả về empty
                    if (StringUtils.isNotBlank(name)) {
                        File uploadedFile = new File(parentFolder + File.separator + childFolder + File.separator + name);    // tạo nơi để lưu file lâu dài
                        fileLocation = parentFolder + File.separator + childFolder + File.separator + name;
                        // kiểm tra xem file đã tồn tại hay chưa
                        boolean isFileExist = uploadedFile.exists();
                        try {
                            if (isFileExist) {
                                // Nếu rồi, xoá và ghi lại
                                uploadedFile.delete();
                                item.write(uploadedFile);
                            } else {
                                // Nếu chưa, ghi mới
                                item.write(uploadedFile);   // ghi file từ bộ nhớ tạm vào nơi chứa lâu dài
                            }
                        } catch (Exception e) {
                            check = false;
                            log.error(e.getMessage(), e);
                        }
                    }
                } else {    // nếu là regular form field
                    if (titleValue != null) {
                        String fieldName = item.getFieldName();     // tên trường
                        String fieldValue = null;
                        try {
                            fieldValue = item.getString("UTF-8");       // giá trị nhập vào (cần truyền UTF-8 để lấy được tiếng Việt)
                        }
                        catch (UnsupportedEncodingException e) {
                            log.error(e.getMessage(), e);
                        }
                        if (titleValue.contains(fieldName)) {        // những trường nào cần lấy ra
                            mapReturnValue.put(fieldName, fieldValue);
                        }
                    }
                }
            }
        } catch(FileUploadException e) {
            check = false;
            log.error(e.getMessage(), e);
        }
        String relativeImagePath = "";
        if(StringUtils.isNotBlank(name)) {
            relativeImagePath = childFolder + File.separator + name;
        }

        /* Return */
        // upload file thành công hay không
        // địa chỉ nơi lưu file vừa upload
        // đường dẫn ảnh tương đối (gồm tên folder con + tên ảnh)
        // 1 map chứa các thuộc tính cùng giá trị của các regular form field(text...)
        return new Object[] {check, fileLocation, relativeImagePath, mapReturnValue};
    }

    // hàm tạo thư mục cha cùng các thư mục con để lưu file được upload lên
    // khi chỉ truyền tên folder vào thì máy sẽ tự động tạo abstract pathname để tạo folder (folder sẽ đươc lưu trên ổ đĩa nơi đặt server nhưng nằm ngoài project - ở đây là ổ C)
    public void checkAndCreateFolder(String parentFolder, String childFolder) {
        File folderRoot = new File(parentFolder);   // folderRoot.getAbsolutePath() sẽ là C:\fileupload
        if(!folderRoot.exists()) {  // nếu đã tồn tại thì không tạo mới nữa
            folderRoot.mkdirs();    // tạo folder cha
        }
        File folderChild = new File(parentFolder + File.separator + childFolder);
        if(!folderChild.exists()) {
            folderChild.mkdirs();   // tạo folder con
        }
    }
}
