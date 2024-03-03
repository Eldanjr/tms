package com.bassure.applicantservice.model.tenentemployeemodel;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tenantemployeeaddress {

    private int addressId;

    private int doorNo;

    private String city;

    private String streetName;

    private int postalCode;

    private String state;

    private String country;

    private Timestamp modifiedAt;
}
