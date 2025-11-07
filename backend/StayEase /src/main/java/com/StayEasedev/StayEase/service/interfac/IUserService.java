package com.StayEasedev.StayEase.service.interfac;


import com.StayEasedev.StayEase.dto.LoginRequest;
import com.StayEasedev.StayEase.dto.Response;
import com.StayEasedev.StayEase.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

}