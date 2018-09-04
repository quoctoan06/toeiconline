package vn.myclass.core.common.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Map;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            //load and create sessionfactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();   //Configuration() in library org.hibernate.cfg.Configuration
        } catch (Throwable e) {
            System.out.println("Initialize session factory failed");
            throw new ExceptionInInitializerError(e);
        }
    }

    //để các lớp và hàm ngoài lớp này có thể gọi đến SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // hàm ghép tên các trường muốn tìm kiếm vào câu truy vấn để dùng trong hàm findByProperty
    public static Object[] buildNameQuery(Map<String, Object> property) {
        StringBuilder nameQuery = new StringBuilder();
        if(property != null && property.size() > 0) {
            String[] params = new String[property.size()];  // key
            Object[] values = new Object[property.size()];  // value
            int i = 0;
            // lấy ra tên các trường muốn tìm kiếm cùng với giá trị
            for(Map.Entry item : property.entrySet()) {
                params[i] = (String) item.getKey();
                values[i] = item.getValue();
                i++;
            }
            // ghép tên vào câu truy vấn
            for(int i1 = 0; i1 < params.length; i1++) {
                nameQuery.append(" and lower(").append(params[i1]).append(") like :" + params[i1] + "");
            }
            // trả về câu truy vấn, tập các trường muốn tìm kiếm cùng tập giá trị
            return new Object[]{nameQuery, params, values};
        }
        // nếu không có trường nào, trả về chuỗi rỗng
        return new Object[]{nameQuery.toString()};
    }
}
