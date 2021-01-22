package com.zhl.sportplay.dao;

import com.zhl.sportplay.pojo.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SeckillMapper {

    int getActivityCount(@Param("title") String title);

    List<SeckillActivity> getAllActivity(@Param("title") String title, @Param("pageStart") int pageStart, @Param("pageSize") int pageSize);

    int addSeckillActivity(SeckillActivity seckillActivity);

    int updateSeckillActivity(SeckillActivity seckillActivity);

    int deleteSeckillActivity(int id);

}

