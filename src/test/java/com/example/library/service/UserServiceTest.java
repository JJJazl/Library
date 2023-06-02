package com.example.library.service;

import com.example.library.dto.user.UserCreateDto;
import com.example.library.mapper.user.UserCreateMapperImpl;
import com.example.library.model.User;
import com.example.library.model.enums.Role;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCreateMapperImpl userCreateMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testAddUser() {
        // Mock dependencies
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@example.com");
        userCreateDto.setPassword("password");

        User user = new User();
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userCreateMapper.mapToModel(userCreateDto)).thenReturn(user);

        // Call the method under test
        userService.addUser(userCreateDto);

        // Verify the interactions
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
        verify(userCreateMapper).mapToModel(userCreateDto);
        assertEquals("encodedPassword", userCreateDto.getPassword());
        assertEquals(LocalDate.now(), userCreateDto.getRegistrationDate());
        assertEquals(Role.READER, userCreateDto.getRole());
    }

    @Test
    void testFindUserByEmail() {
        // Mock dependencies
        String email = "test@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Call the method under test
        Optional<User> result = userService.findUserByEmail(email);

        // Verify the interactions
        verify(userRepository).findByEmail(email);
        assertEquals(Optional.of(user), result);
    }
}