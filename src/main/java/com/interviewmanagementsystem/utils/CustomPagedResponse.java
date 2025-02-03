package com.interviewmanagementsystem.utils;

import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel.PageMetadata;

import java.util.Collection;

public class CustomPagedResponse<T> {
    private Collection<T> data;

    private PageMetadata page;

    private Links links;

    //#region Getter and Setter

    public Collection<T> getData() {
        return data;
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    public PageMetadata getPage() {
        return page;
    }

    public void setPage(PageMetadata page) {
        this.page = page;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    //#endregion

    public CustomPagedResponse(Collection<T> data, PageMetadata page, Links links) {
        this.data = data;
        this.page = page;
        this.links = links;
    }
}
