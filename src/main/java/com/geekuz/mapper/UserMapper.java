package com.geekuz.mapper;

import com.geekuz.dto.UserDTO;
import com.geekuz.entity.User;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "default", uses = {AddressMapper.class})
public interface UserMapper {
    
    // Basic mapping
    @Mapping(target = "password", ignore = true) // Ignore sensitive fields
    @Mapping(target = "roleNames", source = "roles") // Custom mapping
    @Mapping(target = "age", expression = "java(calculateAge(user.getBirthDate()))") // Expression for calculated field
    UserDTO userToUserDTO(User user);
    
    // Custom mapping method for roles to roleNames
    default List<String> rolesToRoleNames(List<com.geekuz.entity.Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(com.geekuz.entity.Role::getName)
                .collect(Collectors.toList());
    }
    
    // Method to calculate age from birthDate
    default int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    // Mapping from DTO to entity
    @Mapping(target = "roles", ignore = true) // We'll handle this separately
    User userDTOToUser(UserDTO userDTO);
    
    // Update existing entity with DTO values
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(UserDTO userDTO, @MappingTarget User user);
}