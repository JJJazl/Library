package com.example.library.mapper.user;

import com.example.library.dto.user.UserReadDto;
import com.example.library.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapperImpl implements UserReadMapper {

    public UserReadDto mapToDto(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
