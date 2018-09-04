package vn.myclass.core.service;

import java.util.Map;

public interface ExaminationService {
    Object[] findExaminationByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit);
}
