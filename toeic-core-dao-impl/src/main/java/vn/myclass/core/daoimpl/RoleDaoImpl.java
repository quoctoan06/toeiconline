package vn.myclass.core.daoimpl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import vn.myclass.core.common.utils.HibernateUtil;
import vn.myclass.core.dao.RoleDao;
import vn.myclass.core.data.daoimpl.AbstractDao;
import vn.myclass.core.persistence.entity.RoleEntity;

import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl extends AbstractDao<Integer, RoleEntity> implements RoleDao {

    // hàm tìm các RoleEntity bằng các roleName
    public List<RoleEntity> findRolesByRolenames(List<String> roles) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        List<RoleEntity> roleEntities = new ArrayList<RoleEntity>();
        try {
            //HQL
            StringBuilder sql = new StringBuilder(" FROM RoleEntity re WHERE re.name IN(:roles) ");
            Query query = session.createQuery(sql.toString());
            query.setParameterList("roles", roles);
            roleEntities = query.list();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
        return roleEntities;
    }
}
