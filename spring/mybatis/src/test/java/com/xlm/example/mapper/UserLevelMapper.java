package com.xlm.example.mapper;

import org.apache.ibatis.annotations.Update;


public interface UserLevelMapper  {
    @Update("insert into userlevel(id) value(#{param1})")
    void insertLevel(Integer[] array);
}
