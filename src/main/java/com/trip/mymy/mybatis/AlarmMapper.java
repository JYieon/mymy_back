package com.trip.mymy.mybatis;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.trip.mymy.dto.AlarmDTO;

@Mapper
public interface AlarmMapper {
	
	//알람 목록 조회
	List<AlarmDTO> getUserAlarms(String memberId);
	//알림 저장
	void insertAlarm(AlarmDTO alarm);
	//알림 삭제
	void deleteUserAlarms(String memberId);
	
	

	
}
