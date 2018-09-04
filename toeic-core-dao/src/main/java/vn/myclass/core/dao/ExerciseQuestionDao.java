package vn.myclass.core.dao;

import vn.myclass.core.data.dao.GenericDao;
import vn.myclass.core.persistence.entity.ExerciseQuestionEntity;

import java.util.Map;

public interface ExerciseQuestionDao extends GenericDao<Integer, ExerciseQuestionEntity> {
    Object[] findByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, Integer exerciseId);
}
