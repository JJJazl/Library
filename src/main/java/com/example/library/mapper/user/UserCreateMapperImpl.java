package com.example.library.mapper.user;

import com.example.library.dto.user.UserCreateDto;
import com.example.library.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserCreateMapperImpl implements UserCreateMapper {

    @Override
    public User mapToModel(UserCreateDto userDto){
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
}
