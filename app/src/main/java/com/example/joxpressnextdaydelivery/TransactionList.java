package com.example.joxpressnextdaydelivery;

public class TransactionList {

    private String id,track_code,track_fee;

    public TransactionList(String id, String track_code, String track_fee) {
        this.id = id;
        this.track_code = track_code;
        this.track_fee = track_fee;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrack_code() {
        return track_code;
    }

    public void setTrack_code(String track_code) {
        this.track_code = track_code;
    }

    public String getTrack_fee() {
        return track_fee;
    }

    public void setTrack_fee(String track_fee) {
        this.track_fee = track_fee;
    }
}
