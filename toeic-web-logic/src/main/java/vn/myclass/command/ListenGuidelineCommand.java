package vn.myclass.command;

import vn.myclass.core.dto.ListenGuidelineDTO;
import vn.myclass.core.web.command.AbstractCommand;

public class ListenGuidelineCommand extends AbstractCommand<ListenGuidelineDTO> {
    public ListenGuidelineCommand() {
        this.pojo = new ListenGuidelineDTO();
    }
}
