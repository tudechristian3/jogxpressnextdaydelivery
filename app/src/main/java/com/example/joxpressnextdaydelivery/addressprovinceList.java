package com.example.joxpressnextdaydelivery;

public class addressprovinceList {

    private String id,psgcCode,provDesc,regCode,provCode;

    public addressprovinceList(String id, String psgcCode, String provDesc, String regCode, String provCode) {
        this.id = id;
        this.psgcCode = psgcCode;
        this.provDesc = provDesc;
        this.regCode = regCode;
        this.provCode = provCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPsgcCode() {
        return psgcCode;
    }

    public void setPsgcCode(String psgcCode) {
        this.psgcCode = psgcCode;
    }

    public String getProvDesc() {
        return provDesc;
    }

    public void setProvDesc(String provDesc) {
        this.provDesc = provDesc;
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
}
