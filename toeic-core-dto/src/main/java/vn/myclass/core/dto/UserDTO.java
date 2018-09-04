package vn.myclass.core.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class UserDTO implements Serializable {
    private Integer userId;
    private String name;
    private String password;
    private String fullname;
    private Timestamp createdDate;
    private RoleDTO roleDTO;
    private UserImportDTO userImportDTO;
    private List<ResultDTO> results;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public RoleDTO getRoleDTO() {
        return roleDTO;
    }

    public void setRoleDTO(RoleDTO roleDTO) {
        this.roleDTO = roleDTO;
    }

    public UserImportDTO getUserImportDTO() {
        return userImportDTO;
    }

    public void setUserImportDTO(UserImportDTO userImportDTO) {
        this.userImportDTO = userImportDTO;
    }

    public List<ResultDTO> getResults() {
        return results;
    }

    public void setResults(List<ResultDTO> results) {
        this.results = results;
    }
}
