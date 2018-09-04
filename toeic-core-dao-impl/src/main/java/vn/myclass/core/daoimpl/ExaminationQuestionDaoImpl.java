package vn.myclass.core.daoimpl;

import vn.myclass.core.dao.ExaminationQuestionDao;
import vn.myclass.core.data.daoimpl.AbstractDao;
import vn.myclass.core.persistence.entity.ExaminationQuestionEntity;

import java.util.Map;

public class ExaminationQuestionDaoImpl extends AbstractDao<Integer, ExaminationQuestionEntity> implements ExaminationQuestionDao {
    public Object[] findByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, Integer examinationId) {
        String subClause = null;
        if (examinationId != null) {
            subClause = " AND examination.examinationId = " + examinationId + "";
        }
        return super.findByProperty(property, sortExpression, sortDirection, offset, limit, subClause);
    }
}
