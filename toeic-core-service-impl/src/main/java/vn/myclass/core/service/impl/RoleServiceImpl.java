package vn.myclass.core.service.impl;

import vn.myclass.core.dao.RoleDao;
import vn.myclass.core.daoimpl.RoleDaoImpl;
import vn.myclass.core.dto.RoleDTO;
import vn.myclass.core.persistence.entity.RoleEntity;
import vn.myclass.core.service.RoleService;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.RoleBeanUtil;

import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {
    public List<RoleDTO> findAll() {
        List<RoleEntity> entities = SingletonDaoUtil.getRoleDaoInstance().findAll();
        List<RoleDTO> roleDTOS = new ArrayList<RoleDTO>();
        for(RoleEntity item : entities) {
            RoleDTO dto = RoleBeanUtil.entityToDTO(item);
            roleDTOS.add(dto);
        }
        return roleDTOS;
    }
}
