package vn.myclass.core.daoimpl;

import vn.myclass.core.dao.ExerciseQuestionDao;
import vn.myclass.core.data.daoimpl.AbstractDao;
import vn.myclass.core.persistence.entity.ExerciseQuestionEntity;

import java.util.Map;

public class ExerciseQuestionDaoImpl extends AbstractDao<Integer, ExerciseQuestionEntity> implements ExerciseQuestionDao {
    public Object[] findByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, Integer exerciseId) {
        String subClause = null;
        if (exerciseId != null) {
            subClause = " AND exerciseEntity.exerciseId = " + exerciseId + "";
        }
        return super.findByProperty(property, sortExpression, sortDirection, offset, limit, subClause);
    }
}
