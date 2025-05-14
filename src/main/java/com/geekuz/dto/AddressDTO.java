package com.geekuz.dto;

public class AddressDTO {
    private String fullAddress; // Combined field
    private String city;
    private String zipCode;
    
    // Getters and setters
    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}