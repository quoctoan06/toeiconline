package vn.myclass.controller.web;

import vn.myclass.command.ExaminationQuestionCommand;
import vn.myclass.core.common.utils.SessionUtil;
import vn.myclass.core.dto.ExaminationQuestionDTO;
import vn.myclass.core.dto.ResultDTO;
import vn.myclass.core.web.common.WebConstant;
import vn.myclass.core.web.utils.FormUtil;
import vn.myclass.core.web.utils.SingletonServiceUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/bai-thi-thuc-hanh.html", "/bai-thi-dap-an.html"})
public class ExaminationQuestionController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ExaminationQuestionCommand command = FormUtil.populate(ExaminationQuestionCommand.class, request);
        getExaminationQuestions(command);
        request.setAttribute(WebConstant.LIST_ITEMS, command);
        RequestDispatcher rd = request.getRequestDispatcher("/views/web/examination/detail.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // detail.jsp gửi về examinationId và các tham số có dạng answerOfUser[ ${item.examinationQuestionId} ] (vd: answerOfUser[1], answerOfUser[2],...)
        ExaminationQuestionCommand command = new ExaminationQuestionCommand();  // FormUtil không lấy được tham số answerOfUser[${item.examinationQuestionId}] trong detail.jsp nên phải lấy thủ công
        Integer examinationId = Integer.parseInt(request.getParameter("examinationId"));
        command.setExaminationId(examinationId);    // có examinationId để tìm được user đang làm bài thi nào
        getExaminationQuestions(command);       // lấy ra danh sách các câu hỏi trong bài thi đó

        for(ExaminationQuestionDTO item : command.getListResult()) {    // với mỗi câu hỏi
            if (request.getParameter("answerOfUser[" + item.getExaminationQuestionId() + "]") != null) {    // nếu người dùng đã chọn đáp án
                item.setAnswerOfUser(request.getParameter("answerOfUser[" + item.getExaminationQuestionId() + "]"));    // thì set đáp án của user ứng với câu hỏi
            }
        }
        String userName = (String) SessionUtil.getInstance().getValue(request, WebConstant.LOGIN_NAME);
        // lưu kết quả làm bài của user vào bảng result trong DB
        ResultDTO resultDTO = SingletonServiceUtil.getResultServiceInstance().saveResult(userName, examinationId, command.getListResult());
        // lấy kết quả điểm đã xử lý ở tầng Service --> set vào command --> hiển thị ở trang result.jsp
        command.setListenScore(resultDTO.getListenScore());
        command.setReadingScore(resultDTO.getReadingScore());
        request.setAttribute(WebConstant.LIST_ITEMS, command);
        RequestDispatcher rd = request.getRequestDispatcher("/views/web/examination/result.jsp");
        rd.forward(request, response);
    }

    private void getExaminationQuestions(ExaminationQuestionCommand command) {
        Object[] objects = SingletonServiceUtil.getExaminationQuestionServiceInstance().findExaminationQuestionByProperty(null, command.getSortExpression(),
                command.getSortDirection(), null, null, command.getExaminationId());
        command.setListResult((List<ExaminationQuestionDTO>) objects[1]);
    }
}
