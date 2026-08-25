package com.sk.skala.shopapi.common;

import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "페이징된 목록 응답 포맷")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagedList<T> {

    @Schema(description = "현재 페이지의 데이터 목록")
    private List<T> content;

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int page;

    @Schema(description = "페이지 크기", example = "10")
    private int size;

    @Schema(description = "전체 데이터 개수", example = "8")
    private long totalElements;

    @Schema(description = "전체 페이지 수", example = "1")
    private int totalPages;

    @Schema(description = "첫 페이지 여부")
    private boolean first;

    @Schema(description = "마지막 페이지 여부")
    private boolean last;

    public static <T> PagedList<T> of(Page<T> page) {
        return new PagedList<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
    }
}
