package com.example.library.mapper.user;

import com.example.library.dto.user.UserReadDto;
import com.example.library.model.User;
public class UserReadMapper {
    public static User mapToModel(UserReadDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }

    public static UserReadDto mapToDto(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
