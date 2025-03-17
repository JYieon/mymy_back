package com.trip.mymy.mybatis;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.trip.mymy.dto.MyBoardDTO;

public interface MyBoardMapper {
	List<MyBoardDTO> findMyPosts(@Param("userId") String userId);
    List<MyBoardDTO> findMyComments(@Param("userId") String userId);
}
