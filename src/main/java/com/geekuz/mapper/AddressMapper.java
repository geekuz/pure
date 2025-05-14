package com.geekuz.mapper;

import com.geekuz.dto.AddressDTO;
import com.geekuz.entity.Address;
import org.mapstruct.*;

@Mapper(componentModel = "default")
public interface AddressMapper {
    
    // Entity to DTO with custom mapping for fullAddress
    @Mapping(target = "fullAddress", expression = "java(address.getStreet() + \", \" + address.getCountry())")
    AddressDTO addressToAddressDTO(Address address);
    
    // DTO to entity with custom mapping for street and country
    @Mapping(target = "street", expression = "java(extractStreet(addressDTO.getFullAddress()))")
    @Mapping(target = "country", expression = "java(extractCountry(addressDTO.getFullAddress()))")
    Address addressDTOToAddress(AddressDTO addressDTO);
    
    // Helper methods for custom mappings
    default String extractStreet(String fullAddress) {
        if (fullAddress == null || !fullAddress.contains(",")) {
            return null;
        }
        return fullAddress.split(",")[0].trim();
    }
    
    default String extractCountry(String fullAddress) {
        if (fullAddress == null || !fullAddress.contains(",")) {
            return null;
        }
        return fullAddress.split(",")[1].trim();
    }
}