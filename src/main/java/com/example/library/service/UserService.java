package com.example.library.service;

import com.example.library.dto.user.UserCreateDto;
import com.example.library.mapper.user.UserCreateMapper;
import com.example.library.model.User;
import com.example.library.model.enums.Role;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(UserCreateDto userCreateDto) {
        userCreateDto.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        userCreateDto.setRegistrationDate(LocalDate.now());
        userCreateDto.setRole(Role.READER);
        User user = UserCreateMapper.mapToModel(userCreateDto);
        userRepository.save(user);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
