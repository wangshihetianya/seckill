package com.zhl.sportplay.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QueryInfo {
    private String query;   //查询的信息，可以是username或者是seckillitem
    private int pageNum;    //当前页
    private int pageSize;

    public String getQuery() {
        return query;
    }

    public int getpageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }
}
