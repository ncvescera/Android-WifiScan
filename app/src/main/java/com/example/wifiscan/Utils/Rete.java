package com.example.wifiscan.Utils;

public class Rete {
    private String SSID;
    private String dettagli;
    private String level;
    private String password;
    private Double lon;
    private Double lat;

    public Rete(String SSID, String dettagli, String level) {
        this.SSID = SSID;
        this.dettagli = dettagli;
        this.level = level;
        this.password = "";
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getSSID() {
        return SSID;
    }

    public String getDettagli() {
        return dettagli;
    }

    public String getLevel() {
        return level;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }
    @Override
    public String toString() {
        return this.SSID + " | " + this.dettagli + " | " + this.level + " | " + this.password + " | " + Double.toString(this.lon) + " | " + Double.toString(this.lat) ;
    }
}
