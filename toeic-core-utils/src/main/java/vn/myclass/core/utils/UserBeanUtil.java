package vn.myclass.core.utils;

import vn.myclass.core.dto.UserDTO;
import vn.myclass.core.persistence.entity.UserEntity;

public class UserBeanUtil {
    public static UserDTO entityToDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setUserId(entity.getUserId());
        dto.setName(entity.getName());
        dto.setPassword(entity.getPassword());
        dto.setFullname(entity.getFullName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setRoleDTO(RoleBeanUtil.entityToDTO(entity.getRoleEntity()));
        return dto;
    }

    public static UserEntity dtoToEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setUserId(dto.getUserId());
        entity.setName(dto.getName());
        entity.setPassword(dto.getPassword());
        entity.setFullName(dto.getFullname());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setRoleEntity(RoleBeanUtil.dtoToEntity(dto.getRoleDTO()));
        return entity;
    }
}
