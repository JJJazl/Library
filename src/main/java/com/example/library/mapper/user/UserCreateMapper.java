package com.example.library.mapper.user;

import com.example.library.dto.user.UserCreateDto;
import com.example.library.model.User;

public class UserCreateMapper {

    public static User mapToModel(UserCreateDto userDto){
        return User.builder()
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .birthday(userDto.getBirthday())
                .registrationDate(userDto.getRegistrationDate())
                .role(userDto.getRole())
                .build();
    }

    public static UserCreateDto mapToDto(User user){
        return UserCreateDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .birthday(user.getBirthday())
                .registrationDate(user.getRegistrationDate())
                .role(user.getRole())
                .build();
    }
}
