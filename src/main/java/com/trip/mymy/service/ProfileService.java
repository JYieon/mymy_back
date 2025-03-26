package com.trip.mymy.service;

import com.trip.mymy.dto.ProfileDTO;

public interface ProfileService {

    void updateProfileImage(String id, String imageUrl);

    ProfileDTO getProfileById(String id);


}