package com.antont.petclinic.v2.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class PageableUtils {
    private static final Integer DEFAULT_START_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 5;
    private static final String DEFAULT_SORT = "id";
    private static final String DEFAULT_DIRECTION = "asc";

    public static Pageable getPageable(Optional<Integer> page, Optional<Integer> size, Optional<String> sort, Optional<String> direction){
        Integer p = page.orElse(DEFAULT_START_PAGE);
        Integer s = size.orElse(DEFAULT_PAGE_SIZE);
        String f = sort.orElse(DEFAULT_SORT);
        Sort.Direction d = Sort.Direction.fromOptionalString(direction.orElse(DEFAULT_DIRECTION)).orElse(Sort.Direction.ASC);
        return  PageRequest.of(p, s, d, f);
    }
}
