package com.example.th2.model;

public class Tour {
    private int image;
    private String tuyen;
    private String time;
    private String phuongtien;

    public Tour(int image, String tuyen, String time, String phuongtien) {
        this.image = image;
        this.tuyen = tuyen;
        this.time = time;
        this.phuongtien = phuongtien;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTuyen() {
        return tuyen;
    }

    public void setTuyen(String tuyen) {
        this.tuyen = tuyen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhuongtien() {
        return phuongtien;
    }

    public void setPhuongtien(String phuongtien) {
        this.phuongtien = phuongtien;
    }
}
