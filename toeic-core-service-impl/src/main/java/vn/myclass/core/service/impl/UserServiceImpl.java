package vn.myclass.core.service.impl;

import org.apache.commons.lang.StringUtils;
import vn.myclass.core.dao.UserDao;
import vn.myclass.core.daoimpl.UserDaoImpl;
import vn.myclass.core.dto.CheckLoginDTO;
import vn.myclass.core.dto.UserDTO;
import vn.myclass.core.dto.UserImportDTO;
import vn.myclass.core.persistence.entity.RoleEntity;
import vn.myclass.core.persistence.entity.UserEntity;
import vn.myclass.core.service.UserService;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.UserBeanUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    public CheckLoginDTO checkLogin(String name, String password) {
        CheckLoginDTO checkLoginDTO = new CheckLoginDTO();
        if(name != null && password != null) {
            Object[] objects = SingletonDaoUtil.getUserDaoInstance().checkLogin(name, password);
            checkLoginDTO.setUserExist((Boolean)objects[0]);
            if(checkLoginDTO.isUserExist()) {   // Nếu tồn tại tài khoản
                checkLoginDTO.setRoleName(objects[1].toString());
            }
        }
        return checkLoginDTO;
    }

    public Object[] findByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit) {
        Object[] objects = SingletonDaoUtil.getUserDaoInstance().findByProperty(property, sortExpression, sortDirection, offset, limit, null);
        List<UserDTO> result = new ArrayList<UserDTO>();
        for(UserEntity item : (List<UserEntity>)objects[1]) {     // phần tử thứ 2 trong objects là 1 List các Entity
            // chuyển List<Entity> sang List<DTO> để chuyển lên tầng web sử dụng
            UserDTO dto = UserBeanUtil.entityToDTO(item);
            result.add(dto);
        }
        objects[1] = result;
        return objects;
    }

    public UserDTO findById(Integer userId) {
        UserEntity entity = SingletonDaoUtil.getUserDaoInstance().findById(userId);
        UserDTO dto = UserBeanUtil.entityToDTO(entity);
        return dto;
    }

    public void saveUser(UserDTO userDTO) {
        Timestamp createdDate = new Timestamp(System.currentTimeMillis());
        userDTO.setCreatedDate(createdDate);    // chỉ khi Add New mới có createdDate, update không có
        UserEntity entity = UserBeanUtil.dtoToEntity(userDTO);
        SingletonDaoUtil.getUserDaoInstance().save(entity);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        UserEntity entity = UserBeanUtil.dtoToEntity(userDTO);
        entity = SingletonDaoUtil.getUserDaoInstance().update(entity);
        UserDTO dto = UserBeanUtil.entityToDTO(entity);
        return dto;
    }

    // hàm kiểm tra validation thứ 3 và 4 khi import user từ file excel
    // username trong file excel không được duplicate với các username trong database
    // rolename trong file excel nếu có giá trị khác với tập giá trị có sẵn trong database(vd: manager) thì phải báo lỗi
    public void validateImportUser(List<UserImportDTO> userImportDTOS) {
        List<String> names = new ArrayList<String>();
        List<String> roles = new ArrayList<String>();

        for(UserImportDTO item : userImportDTOS) {
            if(item.isValid()) {
                names.add(item.getUserName());              // add tập username từ excel sheet vào (không có duplicate vì đã validate ở bước 2)
                if(!roles.contains(item.getRoleName())) {
                    roles.add(item.getRoleName());          // add tập roleName từ excel sheet vào (không có duplicate)
                }
            }
        }

        Map<String, UserEntity> userEntityMap = new HashMap<String, UserEntity>();
        Map<String, RoleEntity> roleEntityMap = new HashMap<String, RoleEntity>();

        if(names.size() > 0) {
            List<UserEntity> userEntities = SingletonDaoUtil.getUserDaoInstance().findUsersByUsernames(names);
            for(UserEntity item : userEntities) {
                userEntityMap.put(item.getName().toUpperCase(), item);
            }
        }
        if(roles.size() > 0) {
            List<RoleEntity> roleEntities = SingletonDaoUtil.getRoleDaoInstance().findRolesByRolenames(roles);
            for(RoleEntity item : roleEntities) {
                roleEntityMap.put(item.getName().toUpperCase(), item);
            }
        }

        for(UserImportDTO item : userImportDTOS) {
            String message = item.getError();
            if(item.isValid()) {
                UserEntity userEntity = userEntityMap.get(item.getUserName().toUpperCase());
                if(userEntity != null) {    // username của row này đã tồn tại trong database
                    message += "</br>";
                    message += "Tên đăng nhập đã tồn tại trong database";
                }

                RoleEntity roleEntity = roleEntityMap.get(item.getRoleName().toUpperCase());
                if(roleEntity == null) {    // roleName của row này không tồn tại trong database
                    message += "</br>";
                    message += "Vai trò không tồn tại";
                }
                if(StringUtils.isNotBlank(message)) {
                    item.setValid(false);
                    item.setError(message.substring(5));    // bỏ </br>, hàm substring yêu cầu không empty
                }
            }
        }
    }

    // hàm lưu dữ liệu user sau khi đọc và validate từ file excel
    public void saveUserImport(List<UserImportDTO> userImportDTOS) {
        for(UserImportDTO item : userImportDTOS) {
            if(item.isValid()) {    // chỉ import những row thoả mãn tất cả validation
                UserEntity userEntity = new UserEntity();
                userEntity.setName(item.getUserName());
                userEntity.setFullName(item.getFullName());
                userEntity.setPassword(item.getPassword());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                userEntity.setCreatedDate(timestamp);
                RoleEntity roleEntity = SingletonDaoUtil.getRoleDaoInstance().findEqualUnique("name", item.getRoleName().toUpperCase());
                userEntity.setRoleEntity(roleEntity);
                SingletonDaoUtil.getUserDaoInstance().save(userEntity);
            }
        }
    }
}

