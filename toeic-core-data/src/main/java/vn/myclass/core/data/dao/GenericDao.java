package vn.myclass.core.data.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericDao<ID extends Serializable, T> {
    List<T> findAll();
    T update(T entity);
    T save(T entity);
    T findById(ID id);
    Object[] findByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, String subClause);     // offset và limit để phân trang, subClause trong trường hợp cần thêm điều kiện
    Integer delete(List<ID> ids);
    T findEqualUnique(String property, Object value);   // hàm chỉ tìm theo 1 cột trong bảng và chỉ trả về 1 object
}
