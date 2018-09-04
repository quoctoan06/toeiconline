package vn.myclass.command;

import vn.myclass.core.dto.ExerciseQuestionDTO;
import vn.myclass.core.web.command.AbstractCommand;

/**
 * Created by Admin on 26/11/2017.
 */
public class ExerciseQuestionCommand extends AbstractCommand<ExerciseQuestionDTO> {
	public ExerciseQuestionCommand() {
		this.pojo = new ExerciseQuestionDTO();
	}
	private Integer exerciseId;		// để map với tham số truyền vào từ web/exercise/list.jsp
	private String answerOfUser;	// để map với tham số truyền vào từ web/exercise/detail.jsp
	private boolean checkAnswer;	// để so sánh đáp án của người dùng với đáp án đúng

	public Integer getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Integer exerciseId) {
		this.exerciseId = exerciseId;
	}

	public String getAnswerOfUser() {
		return answerOfUser;
	}

	public void setAnswerOfUser(String answerOfUser) {
		this.answerOfUser = answerOfUser;
	}

	public boolean isCheckAnswer() {
		return checkAnswer;
	}

	public void setCheckAnswer(boolean checkAnswer) {
		this.checkAnswer = checkAnswer;
	}
}
