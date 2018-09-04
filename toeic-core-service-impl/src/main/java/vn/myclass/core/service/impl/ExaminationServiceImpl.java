package vn.myclass.core.service.impl;

import vn.myclass.core.dto.ExaminationDTO;
import vn.myclass.core.persistence.entity.ExaminationEntity;
import vn.myclass.core.service.ExaminationService;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.ExaminationBeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExaminationServiceImpl implements ExaminationService {
    public Object[] findExaminationByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit) {
        List<ExaminationDTO> result = new ArrayList<ExaminationDTO>();
        Object[] objects = SingletonDaoUtil.getExaminationDaoInstance().findByProperty(property, sortExpression, sortDirection, offset, limit, null);
        for(ExaminationEntity item : (List<ExaminationEntity>)objects[1]) {     // phần tử thứ 2 trong objects là 1 List các Entity
            // chuyển List<Entity> sang List<DTO> để chuyển lên tầng web sử dụng
            ExaminationDTO dto = ExaminationBeanUtil.entityToDTO(item);
            result.add(dto);
        }
        objects[1] = result;
        return objects;
    }
}
