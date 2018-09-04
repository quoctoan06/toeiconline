package vn.myclass.command;

import vn.myclass.core.dto.ResultDTO;
import vn.myclass.core.web.command.AbstractCommand;

public class ResultCommand extends AbstractCommand<ResultDTO> {
    public ResultCommand() {
        this.pojo = new ResultDTO();
    }
}
