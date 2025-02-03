package com.interviewmanagementsystem.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;

@Data
@AllArgsConstructor
public class CustomPagedResponse<T> {
    private Collection<T> data;

    private PagedModel.PageMetadata page;

    private Links links;
}
