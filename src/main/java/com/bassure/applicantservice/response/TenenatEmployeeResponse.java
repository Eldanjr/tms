package com.bassure.applicantservice.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TenenatEmployeeResponse {

    private String statusCode;
    private int employeeId;
    private String firstName;
    private String lastName;
    private String gender;
    private String maritalStatus;
    private String contactNumber;
    private String email;
    private String dateOfJoining;
    private String bloodGroup;
    private String dateOfBirth;
    private int branchId;
    private Address presentAddressId;
    private Address permanentAddressId;
    private int createdBy;
    private String adhaarPath;
    private String panPath;
    private Role[] roles;
    private String designation;
    private String nationality;
    private String status;
    private String code;

    // Constructors, getters, and setters

    @JsonProperty("statusCode")
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("value")
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("maritalStatus")
    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @JsonProperty("contactNumber")
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("dateOfJoining")
    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    @JsonProperty("bloodGroup")
    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    @JsonProperty("dateOfBirth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("branchId")
    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    @JsonProperty("presentAddressId")
    public Address getPresentAddressId() {
        return presentAddressId;
    }

    public void setPresentAddressId(Address presentAddressId) {
        this.presentAddressId = presentAddressId;
    }

    @JsonProperty("permanentAddressId")
    public Address getPermanentAddressId() {
        return permanentAddressId;
    }

    public void setPermanentAddressId(Address permanentAddressId) {
        this.permanentAddressId = permanentAddressId;
    }

    @JsonProperty("createdBy")
    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("adhaarPath")
    public String getAdhaarPath() {
        return adhaarPath;
    }

    public void setAdhaarPath(String adhaarPath) {
        this.adhaarPath = adhaarPath;
    }

    @JsonProperty("panPath")
    public String getPanPath() {
        return panPath;
    }

    public void setPanPath(String panPath) {
        this.panPath = panPath;
    }

    @JsonProperty("roles")
    public Role[] getRoles() {
        return roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    @JsonProperty("designation")
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @JsonProperty("nationality")
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // Inner classes for Address and Role
    public static class Address {
        private int addressId;
        private int doorNo;
        private String city;
        private String streetName;
        private int postalCode;
        private String state;
        private String country;
        private String modifiedAt;

        // Constructors, getters, and setters for Address
    }

    public static class Role {
        private int roleId;
        private String roleName;
        private String description;
        private int createdBy;
        private String createdAt;
        private int modifiedBy;
        private String modifiedAt;
        private boolean active;
        private Permission[] permissions;

        // Constructors, getters, and setters for Role

        public static class Permission {
            private int permissionId;
            private String permissionName;

            // Constructors, getters, and setters for Permission
        }
    }
}

