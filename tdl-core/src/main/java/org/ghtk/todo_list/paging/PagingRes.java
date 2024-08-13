package org.ghtk.todo_list.paging;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PagingRes<T> {
    private List<T> pageData;
    private int pageNo;
    private int pageSize;
    private long total;

    public PagingRes(Page<T> page) {
        this.pageData = page.getContent();
        this.total = page.getTotalElements();
        // Pageable is 0 based but PageReq and PageRes is 1 based
        this.pageNo = page.getNumber() + 1;
        this.pageSize = page.getSize();
    }

    public static <H> PagingRes<H> of(Page<H> page) {
        return new PagingRes<>(page);
    }
}
