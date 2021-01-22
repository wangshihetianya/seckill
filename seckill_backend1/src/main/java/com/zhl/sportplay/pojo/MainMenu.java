package com.zhl.sportplay.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MainMenu {
    private int id;
    private String title;
    private String path;
    private List<SubMenu> subMenuList;
}
