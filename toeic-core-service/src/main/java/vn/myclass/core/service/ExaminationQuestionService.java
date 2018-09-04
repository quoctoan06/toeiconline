package vn.myclass.core.service;

import java.util.Map;

public interface ExaminationQuestionService {
    Object[] findExaminationQuestionByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, Integer examinationId);
}
