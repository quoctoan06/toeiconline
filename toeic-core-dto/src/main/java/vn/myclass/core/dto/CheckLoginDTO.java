package vn.myclass.core.dto;

import java.io.Serializable;

public class CheckLoginDTO implements Serializable {
    private String roleName;
    private boolean isUserExist;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isUserExist() {
        return isUserExist;
    }

    public void setUserExist(boolean userExist) {
        isUserExist = userExist;
    }
}
