package com.geekuz.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Examples of integration testing with JUnit 5
 */
public class IntegrationTesting {

    // Classes to be tested
    static class UserRepository {
        private final File storageFile;
        
        public UserRepository(File storageFile) {
            this.storageFile = storageFile;
        }
        
        public void saveUser(String username, String email) throws IOException {
            try (FileWriter writer = new FileWriter(storageFile, true)) {
                writer.write(username + "," + email + "\n");
            }
        }
        
        public List<String> findAllUsernames() throws IOException {
            return Files.lines(storageFile.toPath())
                    .map(line -> line.split(",")[0])
                    .collect(Collectors.toList());
        }
    }
    
    static class UserService {
        private final UserRepository repository;
        
        public UserService(UserRepository repository) {
            this.repository = repository;
        }
        
        public void registerUser(String username, String email) throws IOException {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            
            repository.saveUser(username, email);
        }
        
        public List<String> getAllUsernames() throws IOException {
            return repository.findAllUsernames();
        }
    }
    
    @TempDir
    Path tempDir; // JUnit 5 will create and manage a temporary directory
    
    private UserRepository userRepository;
    private UserService userService;
    private File userFile;
    
    @BeforeEach
    void setUp() throws IOException {
        userFile = tempDir.resolve("users.csv").toFile();
        userFile.createNewFile();
        
        userRepository = new UserRepository(userFile);
        userService = new UserService(userRepository);
    }
    
    @Test
    @DisplayName("Register and retrieve users")
    void testRegisterAndRetrieveUsers() throws IOException {
        // Register users
        userService.registerUser("john", "john@example.com");
        userService.registerUser("alice", "alice@example.com");
        
        // Retrieve all usernames
        List<String> usernames = userService.getAllUsernames();
        
        // Verify results
        assertEquals(2, usernames.size());
        assertTrue(usernames.contains("john"));
        assertTrue(usernames.contains("alice"));
    }
    
    @Test
    @DisplayName("Register user with invalid email")
    void testRegisterUserWithInvalidEmail() {
        // Attempt to register user with invalid email
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser("john", "invalid-email");
        });
        
        // Verify exception message
        assertEquals("Invalid email format", exception.getMessage());
    }
    
    @Test
    @DisplayName("File persistence works correctly")
    void testFilePersistence() throws IOException {
        // Register a user
        userService.registerUser("testuser", "test@example.com");
        
        // Read file contents directly to verify persistence
        List<String> fileLines = Files.readAllLines(userFile.toPath());
        
        // Verify file contents
        assertEquals(1, fileLines.size());
        assertEquals("testuser,test@example.com", fileLines.get(0));
    }
}