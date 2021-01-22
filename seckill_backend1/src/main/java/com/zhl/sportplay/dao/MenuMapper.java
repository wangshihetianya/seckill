package com.zhl.sportplay.dao;

import com.zhl.sportplay.pojo.MainMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<MainMenu> getMenus(List<String> permissions);
}
