package com.example.library.mapper.user;

import com.example.library.dto.user.UserCreateDto;
import com.example.library.model.User;

public interface UserCreateMapper {
    User mapToModel(UserCreateDto userCreateDto);
}
