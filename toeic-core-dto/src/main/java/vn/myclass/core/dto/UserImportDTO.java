package vn.myclass.core.dto;

import java.io.Serializable;

// chứa những trường cần lấy ra từ file excel
public class UserImportDTO implements Serializable {
    private String userName;
    private String password;
    private String fullName;
    private String roleName;
    private boolean valid = true;   // biến kiểm tra tính hợp lệ của dữ liệu
    private String error;           // thông tin lỗi

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
