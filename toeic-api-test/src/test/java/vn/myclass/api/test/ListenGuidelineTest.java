package vn.myclass.api.test;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import vn.myclass.core.dao.ListenGuidelineDao;
import vn.myclass.core.daoimpl.ListenGuidelineDaoImpl;

import java.util.HashMap;
import java.util.Map;

public class ListenGuidelineTest {
    ListenGuidelineDao listenGuidelineDao;
    @BeforeTest
    // hàm với annotation này luôn chạy trước để khởi tạo dữ liệu, tránh việc khởi tạo lặp lại ở mỗi hàm test
    public void initData() {
        listenGuidelineDao = new ListenGuidelineDaoImpl();
    }

    @Test
    public void checkFindByProperty() {
//        ListenGuidelineDao listenGuidelineDao = new ListenGuidelineDaoImpl();
//        Object[] result = listenGuidelineDao.findByProperty(null, null, null, null,0, 2);
    }

    @Test
    public void checkAPIfindByProperty() {
        Map<String, Object> property = new HashMap<String, Object>();
        property.put("title", "Bai huong dan 8");
        property.put("content", "Noi dung bai huong dan 8");
        Object[] objects = listenGuidelineDao.findByProperty(property, null, null, null, null);
    }
}
