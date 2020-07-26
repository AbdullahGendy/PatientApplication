package com.company.patientapplication.Models.GetClinicInfoByClinicId;

import java.util.ArrayList;

public class Data {
    private String StreetName;

    private String Floor;

    private String MobileNumber;

    private String BuildingName;

    private String ClinicId;

    private String City;

    private ArrayList<WorkingDays> WorkingDays;

    public String getStreetName() {
        return StreetName;
    }

    public void setStreetName(String StreetName) {
        this.StreetName = StreetName;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String Floor) {
        this.Floor = Floor;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    public String getBuildingName() {
        return BuildingName;
    }

    public void setBuildingName(String BuildingName) {
        this.BuildingName = BuildingName;
    }

    public String getClinicId() {
        return ClinicId;
    }

    public void setClinicId(String ClinicId) {
        this.ClinicId = ClinicId;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public ArrayList<WorkingDays> getWorkingDays() {
        return WorkingDays;
    }

    public void setWorkingDays(ArrayList<WorkingDays> WorkingDays) {
        this.WorkingDays = WorkingDays;
    }
}
