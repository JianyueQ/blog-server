package com.blog.business.domain.dto;

/**
 * 文章列表查询条件
 * @author 31373
 */
public class ArticleListDto {

    /**
     * 按热度排序 默认false
     */
    private Boolean hotSort = false;

    /**
     * 按时间排序 默认true
     */
    private Boolean timeSort = true;

    public Boolean getHotSort() {
        return hotSort;
    }

    public void setHotSort(Boolean hotSort) {
        this.hotSort = hotSort;
    }

    public Boolean getTimeSort() {
        return timeSort;
    }

    public void setTimeSort(Boolean timeSort) {
        this.timeSort = timeSort;
    }
}
