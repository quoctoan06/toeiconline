package vn.myclass.core.service.impl;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import vn.myclass.core.dao.ListenGuidelineDao;
import vn.myclass.core.daoimpl.ListenGuidelineDaoImpl;
import vn.myclass.core.dto.ListenGuidelineDTO;
import vn.myclass.core.persistence.entity.ListenGuidelineEntity;
import vn.myclass.core.service.ListenGuidelineService;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.ListenGuidelineBeanUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListenGuidelineServiceImpl implements ListenGuidelineService {
    public Object[] findListenGuidelineByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit) {
        List<ListenGuidelineDTO> result = new ArrayList<ListenGuidelineDTO>();
        Object[] objects = SingletonDaoUtil.getListenGuidelineDaoInstance().findByProperty(property, sortExpression, sortDirection, offset, limit,  null);
        for(ListenGuidelineEntity item : (List<ListenGuidelineEntity>)objects[1]) {     // phần tử thứ 2 trong objects là 1 List các Entity
            // chuyển List<Entity> sang List<DTO> để chuyển lên tầng web sử dụng
            ListenGuidelineDTO dto = ListenGuidelineBeanUtil.entityToDTO(item);
            result.add(dto);
        }
        objects[1] = result;
        return objects;
    }

    public ListenGuidelineDTO findByListenGuidelineId(Integer listenGuidelineId) throws ObjectNotFoundException {
        ListenGuidelineEntity entity = SingletonDaoUtil.getListenGuidelineDaoInstance().findById(listenGuidelineId);
        ListenGuidelineDTO dto = ListenGuidelineBeanUtil.entityToDTO(entity);
        return dto;
    }

    public void saveListenGuideline(ListenGuidelineDTO dto) throws ConstraintViolationException {   // bắt ngoại lệ từ tầng DAO và ném tiếp lên tầng Controller
        // thêm mới thì chỉ cần createddate, bỏ qua modifieddate
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        dto.setCreatedDate(timestamp);
        ListenGuidelineEntity entity = ListenGuidelineBeanUtil.dtoToEntity(dto);
        SingletonDaoUtil.getListenGuidelineDaoInstance().save(entity);
    }

    public ListenGuidelineDTO updateListenGuideline(ListenGuidelineDTO dto) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        dto.setModifiedDate(timestamp);
        ListenGuidelineEntity entity = ListenGuidelineBeanUtil.dtoToEntity(dto);
        entity = SingletonDaoUtil.getListenGuidelineDaoInstance().update(entity);
        return ListenGuidelineBeanUtil.entityToDTO(entity);
    }

    public Integer delete(List<Integer> ids) {
        Integer result = SingletonDaoUtil.getListenGuidelineDaoInstance().delete(ids);
        return result;  // trả về số lượng phần tử đã xoá
    }
}
