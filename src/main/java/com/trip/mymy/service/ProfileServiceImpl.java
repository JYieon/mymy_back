package com.trip.mymy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.mymy.dto.ProfileDTO;
import com.trip.mymy.mybatis.ProfileMapper;

@Service
public class ProfileServiceImpl implements ProfileService{

   @Autowired
    private ProfileMapper profileMapper;
   
   @Override
   public void updateProfileImage(String id, String imageUrl) {
      profileMapper.updateProfileImage(id, imageUrl);
      
   }

   @Override
   public ProfileDTO getProfileById(String id) {
      
      return profileMapper.getProfileById(id);
   }
}
   
   
   
