package com.trip.mymy;

import static org.junit.Assert.*; // ✅ JUnit 4 스타일로 import

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.trip.mymy.controller.MypageController;
import com.trip.mymy.dto.MypageDTO;
import com.trip.mymy.mybatis.FollowMapper;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:testMypage.xml"})
public class TestMypage {

    @Autowired
    private MypageController mypageController;
    @Autowired FollowMapper fm;

    @Autowired
    private HttpSession session;

    @Test
    public void testMypageControllerNotNull() {
        assertNotNull("MypageController가 정상적으로 주입되지 않았습니다.", mypageController);
    }

    @Test
    public void testSetDummyUser() {
        // `setDummyUser` 실행
    	MypageDTO response = mypageController.setDummyUser(session).getBody(); 

        // 응답이 null이 아닌지 확인
        assertNotNull("setDummyUser 결과가 null입니다.", response);
        assertEquals("aaa", response.getId());

        // 세션에 저장되었는지 확인
        MypageDTO sessionUser = (MypageDTO) session.getAttribute("user");
        assertNotNull("세션에 사용자 정보가 저장되지 않았습니다.", sessionUser);
        assertEquals("aaa", sessionUser.getId());
    }

    @Test
    public void testGetCurrentUser_WhenSessionUserExists() {
        // 세션에 사용자 정보 저장
        MypageDTO dummyUser = new MypageDTO();
        dummyUser.setId("aaa");
        session.setAttribute("user", dummyUser);

        // getCurrentUser 실행
        MypageDTO response = mypageController.setDummyUser(session).getBody(); 

        // 응답이 정상인지 확인
        assertNotNull("getCurrentUser 결과가 null입니다.", response);
        assertEquals("aaa", response.getId());
    }

    @Test
    public void testGetCurrentUser_WhenSessionUserNotExists() {
        // 세션 초기화 (사용자가 없는 상태)
        session.invalidate();

        // 예외 발생 여부 확인
        try {
            mypageController.getCurrentUser(session);
            fail("예외가 발생해야 합니다.");
        } catch (RuntimeException e) {
            assertEquals("로그인 필요", e.getMessage());
        }
    }
    
    @Test
    public void testFollowing() {
    	System.out.println("follow");
//    	fm.getFollowingList("aaa");
    }
}
