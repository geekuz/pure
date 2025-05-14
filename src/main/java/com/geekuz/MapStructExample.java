package com.geekuz;

import com.geekuz.dto.UserDTO;
import com.geekuz.entity.Address;
import com.geekuz.entity.Role;
import com.geekuz.entity.User;
import com.geekuz.mapper.UserMapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Arrays;

public class MapStructExample {
    
    public static void main(String[] args) {
        // Create a user entity
        User user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("secret123");
        user.setBirthDate(LocalDate.of(1990, 5, 15));
        
        // Create and set address
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("New York");
        address.setZipCode("10001");
        address.setCountry("USA");
        user.setAddress(address);
        
        // Create and set roles
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ADMIN");
        
        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("USER");
        
        user.setRoles(Arrays.asList(adminRole, userRole));
        
        // Get the mapper
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        
        // Convert entity to DTO
        UserDTO userDTO = userMapper.userToUserDTO(user);
        
        // Print the DTO
        System.out.println("User DTO:");
        System.out.println("ID: " + userDTO.getId());
        System.out.println("Username: " + userDTO.getUsername());
        System.out.println("Email: " + userDTO.getEmail());
        System.out.println("Birth Date: " + userDTO.getBirthDate());
        System.out.println("Age: " + userDTO.getAge());
        System.out.println("Address: " + userDTO.getAddress().getFullAddress());
        System.out.println("City: " + userDTO.getAddress().getCity());
        System.out.println("Zip Code: " + userDTO.getAddress().getZipCode());
        System.out.println("Roles: " + userDTO.getRoleNames());
        
        // Convert DTO back to entity
        User convertedUser = userMapper.userDTOToUser(userDTO);
        System.out.println("\nConverted User:");
        System.out.println("ID: " + convertedUser.getId());
        System.out.println("Username: " + convertedUser.getUsername());
        System.out.println("Email: " + convertedUser.getEmail());
        System.out.println("Password: " + convertedUser.getPassword()); // Should be null
        
        // Update existing entity
        UserDTO updateDTO = new UserDTO();
        updateDTO.setEmail("john.updated@example.com");
        
        userMapper.updateUserFromDTO(updateDTO, user);
        System.out.println("\nUpdated User Email: " + user.getEmail());
    }
}