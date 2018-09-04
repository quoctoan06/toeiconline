package vn.myclass.core.service;

import vn.myclass.core.dto.ExaminationQuestionDTO;
import vn.myclass.core.dto.ResultDTO;

import java.util.List;

public interface ResultService {
    ResultDTO saveResult(String userName, Integer examinationId, List<ExaminationQuestionDTO> examinationQuestions);
}
