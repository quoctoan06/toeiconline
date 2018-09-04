package vn.myclass.core.service;

import java.util.Map;

public interface ExerciseQuestionService {
    Object[] findExerciseQuestionByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, Integer exerciseId);
}
