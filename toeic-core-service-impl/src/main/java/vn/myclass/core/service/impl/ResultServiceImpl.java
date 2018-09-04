package vn.myclass.core.service.impl;

import vn.myclass.core.dto.ExaminationQuestionDTO;
import vn.myclass.core.dto.ResultDTO;
import vn.myclass.core.persistence.entity.ExaminationEntity;
import vn.myclass.core.persistence.entity.ResultEntity;
import vn.myclass.core.persistence.entity.UserEntity;
import vn.myclass.core.service.ResultService;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.ResultBeanUtil;

import java.sql.Timestamp;
import java.util.List;

public class ResultServiceImpl implements ResultService {
    // hàm lưu kết quả làm bài của người dùng vào bảng result trong DB
    // cần có: listenScore, readingScore, userEntity, examinationEntity, createdDate
    public ResultDTO saveResult(String userName, Integer examinationId, List<ExaminationQuestionDTO> examinationQuestions) {
        ResultDTO resultDTO = new ResultDTO();
        if(userName != null && examinationId != null && examinationQuestions != null) {
            UserEntity user = SingletonDaoUtil.getUserDaoInstance().findEqualUnique("name", userName);
            ExaminationEntity examination = SingletonDaoUtil.getExaminationDaoInstance().findById(examinationId);
            ResultEntity resultEntity = new ResultEntity();     // biến để lưu dữ liệu trả về của hàm save trong AbstractDao
            calculateListenAndReadingScore(resultEntity, examinationQuestions);
            initDataToResultEntity(resultEntity, user, examination);
            resultEntity = SingletonDaoUtil.getResultDaoInstance().save(resultEntity);  // lưu dữ liệu trả về của hàm save
            resultDTO = ResultBeanUtil.entityToDTO(resultEntity);   // chuyển về DTO để gửi lên controller
        }
        return resultDTO;
    }

    // hàm set userEntity, examinationEntity và createdDate vào resultEntity
    private void initDataToResultEntity(ResultEntity resultEntity, UserEntity user, ExaminationEntity examination) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        resultEntity.setCreatedDate(timestamp);
        resultEntity.setUser(user);
        resultEntity.setExamination(examination);
    }

    // hàm tính điểm listening và reading rồi lưu vào resultEntity
    private void calculateListenAndReadingScore(ResultEntity resultEntity, List<ExaminationQuestionDTO> examinationQuestions) {
        int listenScore = 0;
        int readingScore = 0;
        for (ExaminationQuestionDTO item : examinationQuestions) {
            if (item.getAnswerOfUser() != null) {   // nếu người dùng có chọn đáp án cho câu hỏi này
                if (item.getAnswerOfUser().equals(item.getCorrectAnswer())) {   // và nếu đáp án đó đúng thì tăng điểm lên
                    if (item.getNumber() <= 4) {    // mặc định các câu nghe có số thứ tự từ 1-4
                        listenScore++;
                    } else {
                        readingScore++;             // còn lại là các câu đọc
                    }
                }
            }
        }
        resultEntity.setListenScore(listenScore);
        resultEntity.setReadingScore(readingScore);
    }
}
