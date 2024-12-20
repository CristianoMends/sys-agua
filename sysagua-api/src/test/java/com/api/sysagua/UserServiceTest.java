package com.api.sysagua;

import com.api.sysagua.dto.user.CreateUserDto;
import com.api.sysagua.enumeration.UserAccess;
import com.api.sysagua.enumeration.UserStatus;
import com.api.sysagua.exception.BusinessException;
import com.api.sysagua.model.User;
import com.api.sysagua.repository.UserRepository;
import com.api.sysagua.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_WithUniqueEmailAndPhone() {
        // Arrange
        CreateUserDto userDto = new CreateUserDto(
                "Test",
                "User",
                "88900000000",
                "test@gmail.com",
                "password123",
                UserAccess.EMPLOYEE);

        when(userRepository.findByEmail(eq(userDto.getEmail()))).thenReturn(Optional.empty());
        when(userRepository.findByPhone(eq(userDto.getPhone()))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(UUID.randomUUID());
            return user;
        });

        // Act
        User createdUser = userService.registerUser(userDto);

        // Assert
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertTrue(new BCryptPasswordEncoder().matches("password123", createdUser.getPassword()));
        assertEquals(UserStatus.ACTIVE, createdUser.getStatus());

        verify(userRepository).findByEmail(eq(userDto.getEmail()));
        verify(userRepository).findByPhone(eq(userDto.getPhone()));
        verify(userRepository).save(any(User.class));
    }



    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        CreateUserDto userDto = new CreateUserDto(
                "Test",
                "User",
                "88900000000",
                "test@gmail.com",
                "password123",
                UserAccess.EMPLOYEE);

        User userAlreadySaved = new User(
                userDto.getName(),
                userDto.getSurname(),
                userDto.getPhone(),
                userDto.getEmail(),
                userDto.getPassword(),
                UserStatus.ACTIVE,
                UserAccess.EMPLOYEE
        );
        when(userRepository.findByEmail(eq(userDto.getEmail()))).thenReturn(Optional.of(userAlreadySaved));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> userService.registerUser(userDto));
        assertEquals("There is already a user with the e-mail test@gmail.com", exception.getMessage());

        verify(userRepository).findByEmail(eq(userDto.getEmail()));
        verify(userRepository, never()).findByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_ShouldThrowException_WhenPhoneAlreadyExists() {
        // Arrange
        CreateUserDto userDto = new CreateUserDto(
                "Test",
                "User",
                "88900000000",
                "test@gmail.com",
                "password123",
                UserAccess.EMPLOYEE);

        User userAlreadySaved = new User(
                userDto.getName(),
                userDto.getSurname(),
                userDto.getPhone(),
                userDto.getEmail(),
                userDto.getPassword(),
                UserStatus.ACTIVE,
                UserAccess.EMPLOYEE
        );
        when(userRepository.findByEmail(eq(userDto.getEmail()))).thenReturn(Optional.empty());
        when(userRepository.findByPhone(eq(userDto.getPhone()))).thenReturn(Optional.of(userAlreadySaved));

        // Act & Assert

        BusinessException exception = assertThrows(BusinessException.class, () -> userService.registerUser(userDto));
        assertEquals("There is already a user with the phone 88900000000", exception.getMessage());

        verify(userRepository).findByEmail(eq(userDto.getEmail()));
        verify(userRepository).findByPhone(eq(userDto.getPhone()));
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailIsAssociatedWithInactiveUser() {
        // Arrange
        CreateUserDto userDto = new CreateUserDto(
                "Test",
                "User",
                "88900000000",
                "test@gmail.com",
                "password123",
                UserAccess.EMPLOYEE);

        User inactiveUser = new User();
        inactiveUser.setStatus(UserStatus.INACTIVE);
        when(userRepository.findByEmail(eq(userDto.getEmail()))).thenReturn(Optional.of(inactiveUser));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> userService.registerUser(userDto));
        assertEquals("The email provided is already associated with a user who has been deleted.", exception.getMessage());

        verify(userRepository).findByEmail(eq(userDto.getEmail()));
        verify(userRepository, never()).findByPhone(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_ShouldThrowException_WhenPhoneIsAssociatedWithInactiveUser() {
        // Arrange
        CreateUserDto userDto = new CreateUserDto(
                "Test",
                "User",
                "88900000000",
                "test@gmail.com",
                "password123",
                UserAccess.EMPLOYEE);

        User inactiveUser = new User();
        inactiveUser.setStatus(UserStatus.INACTIVE);
        when(userRepository.findByEmail(eq(userDto.getEmail()))).thenReturn(Optional.empty());
        when(userRepository.findByPhone(eq(userDto.getPhone()))).thenReturn(Optional.of(inactiveUser));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> userService.registerUser(userDto));
        assertEquals("The phone provided is already associated with a user who has been deleted.", exception.getMessage());

        verify(userRepository).findByEmail(eq(userDto.getEmail()));
        verify(userRepository).findByPhone(eq(userDto.getPhone()));
        verify(userRepository, never()).save(any());
    }
}
