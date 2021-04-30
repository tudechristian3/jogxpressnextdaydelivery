package com.example.joxpressnextdaydelivery;

public class addressbarangayList {

    private String id,brgyCode,brgyDesc,regCode,provCode,citymunCode;

    public addressbarangayList(String id, String brgyCode, String brgyDesc, String regCode, String provCode, String citymunCode) {
        this.id = id;
        this.brgyCode = brgyCode;
        this.brgyDesc = brgyDesc;
        this.regCode = regCode;
        this.provCode = provCode;
        this.citymunCode = citymunCode;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrgyCode() {
        return brgyCode;
    }

    public void setBrgyCode(String brgyCode) {
        this.brgyCode = brgyCode;
    }

    public String getBrgyDesc() {
        return brgyDesc;
    }

    public void setBrgyDesc(String brgyDesc) {
        this.brgyDesc = brgyDesc;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getCitymunCode() {
        return citymunCode;
    }

    public void setCitymunCode(String citymunCode) {
        this.citymunCode = citymunCode;
    }

    @Override
    public String toString() {
        return brgyDesc;
    }
}
