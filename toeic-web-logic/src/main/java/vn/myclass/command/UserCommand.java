package vn.myclass.command;

import vn.myclass.core.dto.RoleDTO;
import vn.myclass.core.dto.UserDTO;
import vn.myclass.core.dto.UserImportDTO;
import vn.myclass.core.web.command.AbstractCommand;

import java.util.List;

public class UserCommand extends AbstractCommand<UserDTO> {
    public UserCommand() {
        this.pojo = new UserDTO();      // ở đây pojo là dạng UserDTO nên tương tự với: UserDTO pojo = new UserDTO();
    }

    private String confirmPassword;
    private List<RoleDTO> roleDTOS;     // để hiển thị danh sách role ngoài View
    private Integer roleId;
    private List<UserImportDTO> userImportDTOS;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<RoleDTO> getRoleDTOS() {
        return roleDTOS;
    }

    public void setRoleDTOS(List<RoleDTO> roleDTOS) {
        this.roleDTOS = roleDTOS;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<UserImportDTO> getUserImportDTOS() {
        return userImportDTOS;
    }

    public void setUserImportDTOS(List<UserImportDTO> userImportDTOS) {
        this.userImportDTOS = userImportDTOS;
    }
}
