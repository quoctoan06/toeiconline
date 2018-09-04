package vn.myclass.api.test;

import org.testng.annotations.Test;
import vn.myclass.core.dao.RoleDao;
import vn.myclass.core.daoimpl.RoleDaoImpl;
import vn.myclass.core.persistence.entity.RoleEntity;

public class ImportTest {

    @Test
    public void checkFunctionFindEqualUnique() {
        RoleDao roleDao = new RoleDaoImpl();
        RoleEntity roleEntity = roleDao.findEqualUnique("name", "USER");
        System.out.println(roleEntity.getName());
    }
}
