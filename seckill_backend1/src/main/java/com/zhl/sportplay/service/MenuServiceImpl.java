package com.zhl.sportplay.service;

import com.zhl.sportplay.dao.MenuMapper;
import com.zhl.sportplay.dao.UserMapper;
import com.zhl.sportplay.pojo.MainMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MenuMapper menuMapper;

    @Override
    public List<MainMenu> getMenus(String role) {
        //先获得角色的权限
        List<String> permissions = userMapper.getPermissionByRole(role);

        //再获取对应角色可取得的菜单
        return menuMapper.getMenus(permissions);

    }
}
