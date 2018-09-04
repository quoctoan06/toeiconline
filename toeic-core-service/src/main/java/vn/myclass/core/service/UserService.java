package vn.myclass.core.service;

import vn.myclass.core.dto.CheckLoginDTO;
import vn.myclass.core.dto.UserDTO;
import vn.myclass.core.dto.UserImportDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    CheckLoginDTO checkLogin(String name, String password);
    Object[] findByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit);
    UserDTO findById(Integer userId);
    void saveUser(UserDTO userDTO);
    UserDTO updateUser(UserDTO userDTO);
    void validateImportUser(List<UserImportDTO> userImportDTOS);
    void saveUserImport(List<UserImportDTO> userImportDTOS);
}
