package vn.myclass.controller.web;

import vn.myclass.command.ExerciseQuestionCommand;
import vn.myclass.core.dto.ExerciseQuestionDTO;
import vn.myclass.core.web.common.WebConstant;
import vn.myclass.core.web.utils.FormUtil;
import vn.myclass.core.web.utils.RequestUtil;
import vn.myclass.core.web.utils.SingletonServiceUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet(urlPatterns = {"/bai-tap-nghe.html", "/ajax-bai-tap-dap-an.html"})
public class ExerciseQuestionController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ExerciseQuestionCommand command = FormUtil.populate(ExerciseQuestionCommand.class, request);
        getListenExerciseQuestion(command);
        request.setAttribute(WebConstant.LIST_ITEMS, command);
        RequestDispatcher rd = request.getRequestDispatcher("/views/web/exercise/detail.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ExerciseQuestionCommand command = FormUtil.populate(ExerciseQuestionCommand.class, request);
        getListenExerciseQuestion(command);
        for(ExerciseQuestionDTO item : command.getListResult()) {
            if(!command.getAnswerOfUser().equals(item.getCorrectAnswer())) {    // nếu trả lời khác với đáp án đúng
                command.setCheckAnswer(true);
            }
        }
        request.setAttribute(WebConstant.LIST_ITEMS, command);
        RequestDispatcher rd = request.getRequestDispatcher("/views/web/exercise/result.jsp");
        rd.forward(request, response);
    }

    // việc phân trang và trả về được ListDTO cần tìm cần 3 tham số: firstItem, maxPageItem, exerciseId
    // trong đó để tính được firstItem cần thêm tham số page
    // nên từ view cần truyền vào 2 tham số là số page và exerciseId
    // ở đây setMaxPageItem(1) nên mỗi lần bấm nextPage đều query và hiển thị chỉ một exerciseQuestionDTO
    private void getListenExerciseQuestion(ExerciseQuestionCommand command) {
        command.setMaxPageItems(1);
        RequestUtil.initSearchBeanManual(command);
        Object[] objects = SingletonServiceUtil.getExerciseQuestionServiceInstance().findExerciseQuestionByProperty(new HashMap<String, Object>(), command.getSortExpression(),
                command.getSortDirection(), command.getFirstItem(), command.getMaxPageItems(), command.getExerciseId());    // exerciseId được truyền từ list.jsp của phần exercise, dùng để tìm kiếm và show ra các ExerciseQuestion để làm bài tập
        command.setListResult((List<ExerciseQuestionDTO>) objects[1]);
        command.setTotalItems(Integer.parseInt(objects[0].toString()));
        command.setTotalPages((int) Math.ceil((double) command.getTotalItems() / command.getMaxPageItems()));
    }
}
