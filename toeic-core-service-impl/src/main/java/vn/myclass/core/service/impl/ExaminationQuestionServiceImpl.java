package vn.myclass.core.service.impl;

import vn.myclass.core.dto.ExaminationQuestionDTO;
import vn.myclass.core.persistence.entity.ExaminationQuestionEntity;
import vn.myclass.core.service.ExaminationQuestionService;
import vn.myclass.core.service.utils.SingletonDaoUtil;
import vn.myclass.core.utils.ExaminationBeanUtil;
import vn.myclass.core.utils.ExaminationQuestionBeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExaminationQuestionServiceImpl implements ExaminationQuestionService {
    public Object[] findExaminationQuestionByProperty(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit, Integer examinationId) {
        List<ExaminationQuestionDTO> result = new ArrayList<ExaminationQuestionDTO>();
        Object[] objects = SingletonDaoUtil.getExaminationQuestionDaoInstance().findByProperty(property, sortExpression, sortDirection, offset, limit, examinationId);
        int count = 1;
        for(ExaminationQuestionEntity item : (List<ExaminationQuestionEntity>)objects[1]) {     // phần tử thứ 2 trong objects là 1 List các Entity
            // chuyển List<Entity> sang List<DTO> để chuyển lên tầng web sử dụng
            ExaminationQuestionDTO dto = ExaminationQuestionBeanUtil.entityToDTO(item);
            // gán số thứ tự cho từng câu hỏi (riêng phần paragraph thì không đánh số phần đoạn văn, mà đánh số các câu hỏi sau nó)
            if(item.getParagraph() == null) {
                dto.setNumber(count);
                count++;
            }
            dto.setExamination(ExaminationBeanUtil.entityToDTO(item.getExamination()));
            result.add(dto);
        }
        objects[1] = result;
        return objects;
    }
}
