package com.example.library.mapper.user;

import com.example.library.dto.user.UserReadDto;
import com.example.library.model.User;

public interface UserReadMapper {
    UserReadDto mapToDto(User user);
}
