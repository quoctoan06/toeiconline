package vn.myclass.core.web.utils;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

// Map giữa các thuộc tính trong Form của View (được chứa trong request) với các thuộc tính trong 1 object (thường là pojo)
public class FormUtil {
    public static <T> T populate(Class<T> clazz, HttpServletRequest request)  {
        T object = null;
        try {
            object = (T) clazz.newInstance();
            BeanUtils.populate(object, request.getParameterMap());
        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }
}
