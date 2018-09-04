package vn.myclass.core.service.impl;

import vn.myclass.core.dto.ExerciseDTO;
import vn.myclass.core.persistence.entity.ExerciseEntity;
import vn.myclass.core.service.ExerciseService;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.ExerciseBeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExerciseServiceImpl implements ExerciseService {

    public Object[] findExerciseByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit) {
        List<ExerciseDTO> result = new ArrayList<ExerciseDTO>();
        Object[] objects = SingletonDaoUtil.getExerciseDaoInstance().findByProperty(property, sortExpression, sortDirection, offset, limit, null);
        for(ExerciseEntity item : (List<ExerciseEntity>)objects[1]) {     // phần tử thứ 2 trong objects là 1 List các Entity
            // chuyển List<Entity> sang List<DTO> để chuyển lên tầng web sử dụng
            ExerciseDTO dto = ExerciseBeanUtil.entityToDTO(item);
            result.add(dto);
        }
        objects[1] = result;
        return objects;
    }
}
