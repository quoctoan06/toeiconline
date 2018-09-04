package vn.myclass.core.service;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import vn.myclass.core.dto.ListenGuidelineDTO;

import java.util.List;
import java.util.Map;

public interface ListenGuidelineService {
    Object[] findListenGuidelineByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit);
    ListenGuidelineDTO findByListenGuidelineId(Integer listenGuidelineId) throws ObjectNotFoundException;
    void saveListenGuideline(ListenGuidelineDTO dto) throws ConstraintViolationException;   // ngoại lệ này bắt lỗi khi vi phạm những constraints đã ràng buộc trong DB (như NOT NULL, UNIQUE,...)
    ListenGuidelineDTO updateListenGuideline(ListenGuidelineDTO dto);
    Integer delete(List<Integer> ids);
}
