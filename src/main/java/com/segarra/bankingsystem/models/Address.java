package com.segarra.bankingsystem.models;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Embeddable;
import javax.validation.constraints.*;

@Embeddable
public class Address {
    @NotBlank(message = "Country is required")
    private String country;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "Street name is required")
    private String street;
    @Digits(integer = 3, fraction = 0, message = "Valid street number is required")
    @Min(1)
    @ColumnDefault(value = "0")
    private int number;
    @NotBlank(message = "Zip code is required")
    @Pattern(regexp="(\\d{5})", message = "Zip code must be numerical and have a length of 5")
    private String zipCode;

    public Address() {
    }

    public Address(String country, String city, String street, int number, String zipCode) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
