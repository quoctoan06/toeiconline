package vn.myclass.api.test;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import vn.myclass.core.dao.UserDao;
import vn.myclass.core.daoimpl.UserDaoImpl;
import vn.myclass.core.persistence.entity.UserEntity;

public class LoginTest {
    private final Logger log = Logger.getLogger(this.getClass());
//    @Test
//    public void checkIsUserExist() {
//        UserDao userDao = new UserDaoImpl();
//        String name = "admin";
//        String password = "123456";
//        UserEntity entity = userDao.findUserByUsernameAndPassword(name, password);
//        if(entity != null) {
//            log.error("Login success");
//        } else {
//            log.error("Login failed");
//        }
//    }
//
//    @Test
//    public void checkFindRoleByUser() {
//        UserDao userDao = new UserDaoImpl();
//        String name = "admin";
//        String password = "123456";
//        UserEntity entity = userDao.findUserByUsernameAndPassword(name, password);
//        log.error(entity.getRoleEntity().getRoleId() + "-" + entity.getRoleEntity().getName());
//    }
}
