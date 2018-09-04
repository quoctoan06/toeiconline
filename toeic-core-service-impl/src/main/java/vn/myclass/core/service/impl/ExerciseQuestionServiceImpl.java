package vn.myclass.core.service.impl;

import vn.myclass.core.dto.ExerciseQuestionDTO;
import vn.myclass.core.persistence.entity.ExerciseQuestionEntity;
import vn.myclass.core.service.ExaminationQuestionService;
import vn.myclass.core.service.ExerciseQuestionService;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.ExerciseBeanUtil;
import vn.myclass.core.utils.ExerciseQuestionBeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExerciseQuestionServiceImpl implements ExerciseQuestionService {

    public Object[] findExerciseQuestionByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, Integer exerciseId) {
        List<ExerciseQuestionDTO> result = new ArrayList<ExerciseQuestionDTO>();
        Object[] objects = SingletonDaoUtil.getExerciseQuestionDaoInstance().findByProperty(property, sortExpression, sortDirection, offset, limit, exerciseId);
        for(ExerciseQuestionEntity item : (List<ExerciseQuestionEntity>)objects[1]) {     // phần tử thứ 2 trong objects là 1 List các Entity
            // chuyển List<Entity> sang List<DTO> để chuyển lên tầng web sử dụng
            ExerciseQuestionDTO dto = ExerciseQuestionBeanUtil.entityToDTO(item);
            dto.setExercise(ExerciseBeanUtil.entityToDTO(item.getExerciseEntity()));
            result.add(dto);
        }
        objects[1] = result;
        return objects;
    }
}
