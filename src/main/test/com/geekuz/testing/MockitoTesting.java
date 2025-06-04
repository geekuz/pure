package com.geekuz.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MockitoTesting {

    // Service to be tested
    static class UserService {
        private final UserRepository repository;
        
        public UserService(UserRepository repository) {
            this.repository = repository;
        }
        
        public String getUsernameById(int id) {
            // Business logic
            if (id <= 0) {
                throw new IllegalArgumentException("ID must be positive");
            }
            
            // Call to repository
            return repository.findUsernameById(id);
        }
    }
    
    // Dependency to be mocked
    interface UserRepository {
        String findUsernameById(int id);
    }
    
    @Mock
    private UserRepository mockRepository;
    
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        userService = new UserService(mockRepository);
    }
    
    @Test
    @DisplayName("Test getUsernameById with mocked repository")
    void testGetUsernameById() {
        // Arrange - set up the mock behavior
        when(mockRepository.findUsernameById(1)).thenReturn("john.doe");
        
        // Act - call the method being tested
        String result = userService.getUsernameById(1);
        
        // Assert - verify the result
        assertEquals("john.doe", result);
        
        // Verify - ensure the mock was called as expected
        verify(mockRepository).findUsernameById(1);
    }
    
    @Test
    @DisplayName("Test getUsernameById with invalid ID")
    void testGetUsernameByIdInvalidId() {
        // Assert that exception is thrown
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUsernameById(0);
        });
        
        // Verify repository was never called
        verify(mockRepository, never()).findUsernameById(anyInt());
    }
    
    @Test
    @DisplayName("Test with argument matchers")
    void testWithArgumentMatchers() {
        // Setup mock to return value for any positive integer
        when(mockRepository.findUsernameById(anyInt())).thenReturn("generic.user");
        
        String result = userService.getUsernameById(42);
        
        assertEquals("generic.user", result);
    }
}