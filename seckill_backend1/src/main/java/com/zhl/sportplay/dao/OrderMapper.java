package com.zhl.sportplay.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    int checkOrder(@Param("activityID") int activityID, @Param("userID") int userID);

}
