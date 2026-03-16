package com.blog.business.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 前台用户文章归档VO
 *
 * @author 31373
 */
public class FrontArticlesArchivesVo {

    private Integer year;
    private Integer month;

    private List<FrontArticlesVo> articlesList;

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public List<FrontArticlesVo> getArticlesList() {
        return articlesList;
    }

    public void setArticlesList(List<FrontArticlesVo> articlesList) {
        this.articlesList = articlesList;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE)
                .append("year", getYear())
                .append("month", getMonth())
                .append("articlesList", getArticlesList())
                .toString();
    }
}
