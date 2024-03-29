package com.bassure.applicantservice.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantAddressResponse {

    private int addressId;

    private String doorNo;

    private String street;

    private String city;

    private String state;

    private String country;

    private String pinCode;
}
