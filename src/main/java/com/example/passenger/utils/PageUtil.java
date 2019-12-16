package com.example.passenger.utils;

import java.util.List;

public class PageUtil {
    // 每页数据条数
    private Integer pageSize;

    // 当前页数
    private Integer pageNum;

    // 总行数
    private Integer rowCount;

    // 下一页
    private Integer nextPage;

    // 上一页
    private Integer previousPage;

    // 最大页数
    private Integer maxPage;

    // 数据
    private List<?> pageData;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Integer getNextPage() {
        return (this.pageNum + 1) > getMaxPage() ? getMaxPage() : this.pageNum + 1;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getPreviousPage() {
        return (this.pageNum - 1) < 1 ? 1 : this.pageNum - 1;
    }

    public void setPreviousPage(Integer previousPage) {
        this.previousPage = previousPage;
    }

    public Integer getMaxPage() {
        return (int) Math.ceil((double) rowCount / pageSize);
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public List<?> getPageData() {
        return pageData;
    }

    public void setPageData(List<?> pageData) {
        this.pageData = pageData;
    }
}
