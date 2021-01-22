package com.zhl.sportplay2.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    int addOrder(@Param("activityID") int activityID, @Param("userID") int userID);

}
