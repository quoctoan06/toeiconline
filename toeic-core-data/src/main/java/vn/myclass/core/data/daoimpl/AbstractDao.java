package vn.myclass.core.data.daoimpl;

import org.apache.log4j.Logger;
import org.hibernate.*;
import vn.myclass.core.common.constant.CoreConstant;
import vn.myclass.core.common.utils.HibernateUtil;
import vn.myclass.core.data.dao.GenericDao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractDao<ID extends Serializable, T> implements GenericDao<ID, T> {
    private final Logger log = Logger.getLogger(this.getClass());
    private Class<T> persistenceClass;  //biến để lưu kiểu Entity

    //constructor
    public AbstractDao() {
        this.persistenceClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];   //trả về kiểu của T
    }

    //hàm để lấy ra tên của class Entity (UserEntity->user) để truyền vào câu lệnh SQL
    public String getPersistenceClassName() {
        return persistenceClass.getSimpleName();
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<T>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            //HQL
            StringBuilder sql = new StringBuilder("from ");
            sql.append(this.getPersistenceClassName()); //vd HQL: "from UserEntity"
            Query query = session.createQuery(sql.toString());
            list = query.list();
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }

        return list;
    }

    public T update(T entity) {
        T result = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Object object = session.merge(entity);  //merge() - update the data, return Object type
            result = (T) object;
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }

        return result;
    }

    public T save(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);  //persist() - save the data, return void
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw e;    // ném ngoại lệ
        } finally {
            session.close();
        }
        return entity;
    }

    public T findById(ID id) {
        T result = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            result = (T) session.get(persistenceClass, id); //get() - retrieve data, receive (Entity, id), return Object type
            if(result == null) {
                throw new ObjectNotFoundException("NOT FOUND " + id, null);
            }
            transaction.commit();
        } catch(HibernateException e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
        return result;
    }

    // tìm kiếm theo nhiều trường
    public Object[] findByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, String subClause) {
        List<T> list = new ArrayList<T>();
        Object totalItem = 0;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Object[] nameQuery = HibernateUtil.buildNameQuery(property);    // câu truy vấn ghép bởi tên các trường muốn tìm kiếm

        try {
            // truy vấn đưa ra danh sách các phần tử
            StringBuilder sql1 = new StringBuilder("from ");
            sql1.append(getPersistenceClassName()).append(" where 1=1").append(nameQuery[0]);
            if(subClause != null) {
                sql1.append(subClause);
            }
            if(sortExpression != null && sortDirection != null) {
                sql1.append(" order by ").append(sortExpression);
                sql1.append(" " + (sortDirection.equals(CoreConstant.SORT_ASC) ? "asc" : "desc"));   //1 for ASC, 2 for DESC
            }
            Query query1 = session.createQuery(sql1.toString());
            setParameterToQuery(nameQuery, query1);
            // phân trang
            if(offset != null && offset >= 0) {
                query1.setFirstResult(offset);
            }
            if(limit != null && limit > 0) {
                query1.setMaxResults(limit);
            }
            list = query1.list();

            //truy vấn đếm số lượng các phần tử
            StringBuilder sql2 = new StringBuilder("select count(*) from ");
            sql2.append(getPersistenceClassName()).append(" where 1=1").append(nameQuery[0]);
            if(subClause != null) {
                sql2.append(subClause);
            }
            Query query2 = session.createQuery(sql2.toString());
            setParameterToQuery(nameQuery, query2);
            totalItem = query2.list().get(0);   //trả về phần tử đầu tiên trong list có được từ truy vấn (tức số lượng phần tử)
            transaction.commit();
        } catch(HibernateException e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }

        return new Object[]{totalItem, list};
    }

    // hàm set giá trị tương ứng với tên các trường để hoàn thành câu truy vấn
    private void setParameterToQuery(Object[] nameQuery, Query query) {
        if(nameQuery.length == 3) {     // nếu property != null --> hàm buildNameQuery() trả về đủ 3 tham số
            String[] params = (String[]) nameQuery[1];  // key
            Object[] values = (Object[]) nameQuery[2];  // value
            for(int i2 = 0; i2 < params.length; i2++) {
                query.setParameter(params[i2], "%" + values[i2] + "%");
            }
        }
    }

    public Integer delete(List<ID> ids) {
        Integer count = 0;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            for(ID id : ids) {
                T t = (T) session.get(persistenceClass, id);
                session.delete(t);
                count++;
            }
            transaction.commit();
        } catch(HibernateException e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
        return count;
    }

    // hàm chỉ tìm theo 1 cột trong bảng và chỉ trả về 1 object
    public T findEqualUnique(String property, Object value) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        T result = null;
        try {
            String sql = " from " + getPersistenceClassName() + " model where model." + property + "= :value";
            Query query = session.createQuery(sql.toString());
            query.setParameter("value", value);
            result = (T) query.uniqueResult();
            transaction.commit();
        } catch(HibernateException e) {
            transaction.rollback();
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
        return result;
    }
}
