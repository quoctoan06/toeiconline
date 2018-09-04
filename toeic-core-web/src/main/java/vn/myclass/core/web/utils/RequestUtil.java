package vn.myclass.core.web.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import vn.myclass.core.web.command.AbstractCommand;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    // hàm nhận các parameter từ view trả về (trong request) để xử lý sort và phân trang - sử dụng cho <display:table> trong phần admin
    public static void initSearchBean(HttpServletRequest request, AbstractCommand bean) {
        if(bean != null) {
            /* Sort */
            // sort theo cột nào
            String sortExpression = request.getParameter(new ParamEncoder(bean.getTableId()).encodeParameterName(TableTagParameters.PARAMETER_SORT));
            // sort theo chiều nào
            String sortDirection = request.getParameter(new ParamEncoder(bean.getTableId()).encodeParameterName(TableTagParameters.PARAMETER_ORDER));

            /* Phân trang */
            // page số mấy
            String pageStr = request.getParameter(new ParamEncoder(bean.getTableId()).encodeParameterName(TableTagParameters.PARAMETER_PAGE));
            Integer page = 1;   // ban đầu hiển thị lên chưa có giá trị nên mặc định là trang 1
            if(StringUtils.isNotBlank(pageStr)) {
                try {
                    page = Integer.valueOf(pageStr);
                } catch (Exception e) {
                    // ignore
                }
            }
            bean.setPage(page);
            bean.setSortExpression(sortExpression);
            bean.setSortDirection(sortDirection);

            // item đầu tiên trong 1 trang bất kỳ
            bean.setFirstItem((bean.getPage() - 1) * bean.getMaxPageItems());
        }
    }

    // hàm tính các tham số bằng tay để phân trang, sử dụng với twbs-pagination
    public  static void initSearchBeanManual(AbstractCommand command) {
        if(command != null) {
            Integer page = 1;
            if(command.getPage() != 0) {
                page = command.getPage();
            }
            command.setPage(page);
            command.setFirstItem((command.getPage() - 1) * command.getMaxPageItems());
        }
    }
}
