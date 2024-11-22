package com.example.demo.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QueryResult<T> implements Serializable {
    private List<T> entities;
    private long total;
}
