package com.zhl.sportplay.service;

import com.zhl.sportplay.pojo.MainMenu;

import java.util.List;

public interface MenuService {

    List<MainMenu> getMenus(String role);
}
