package com.es.core.model;

public class ParamsForSearch {
    private String search;
    private String sortField;
    private String order;
    private int offset;
    private int limit;

    public ParamsForSearch(String search, String sortField, String order, int offset, int limit) {
        this.search = search;
        this.sortField = sortField;
        this.order = order;
        this.offset = offset;
        this.limit = limit;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
