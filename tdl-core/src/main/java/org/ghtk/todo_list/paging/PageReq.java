package org.ghtk.todo_list.paging;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
public class PageReq {

    @Min(1)
    private Integer pageSize;

    public Pageable makePageable(int pageNumber) {
        return PageRequest.of(pageNumber-1,getPageSize());
    }
}
