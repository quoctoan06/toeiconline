package vn.myclass.core.dao;

import vn.myclass.core.data.dao.GenericDao;
import vn.myclass.core.persistence.entity.RoleEntity;

import java.util.List;

public interface RoleDao extends GenericDao<Integer, RoleEntity> {
    List<RoleEntity> findRolesByRolenames(List<String> roles);
}
