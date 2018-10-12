package com.xlm.example.mapper;

import org.apache.ibatis.annotations.Update;

/**
 * 
 * 
 * 
 *
 * Created by chars on 2015 下午4:16:22.
 *
 * Copyright © mizhuanglicai
 */
public interface UserLevelMapper  {
    @Update("insert into userlevel(id) value(#{param1})")
    void insertLevel(Integer[] array);
}
