package vn.myclass.core.service;

import java.util.Map;

public interface ExerciseService {
    Object[] findExerciseByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit);
}
