package vn.myclass.command;

import vn.myclass.core.dto.ExaminationDTO;
import vn.myclass.core.web.command.AbstractCommand;

public class ExaminationCommand extends AbstractCommand<ExaminationDTO> {
    public ExaminationCommand() {
        this.pojo = new ExaminationDTO();
    }
}
