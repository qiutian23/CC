package com.myself.CC.analyze.model;

import lombok.Data;

//作者名称和对应创作诗文数量
@Data
public class AuthorCount {
    private String author;
    private Integer count;
}