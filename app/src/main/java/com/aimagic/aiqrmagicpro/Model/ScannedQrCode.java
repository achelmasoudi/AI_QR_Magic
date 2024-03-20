package com.aimagic.aiqrmagicpro.Model;

public class ScannedQrCode {

    private int id;
    private String scanned_QrCode_Type;
    private String scanned_QrCode_Link;
    private byte[] scanned_QrCode_Image;

    private int scanned_QrCode_ImageType;
    private String scanned_QrCode_DateTime;

    public ScannedQrCode() {

    }

    public ScannedQrCode(String scanned_QrCode_Type, String scanned_QrCode_Link, byte[] scanned_QrCode_Image, int scanned_QrCode_ImageType) {
        this.scanned_QrCode_Type = scanned_QrCode_Type;
        this.scanned_QrCode_Link = scanned_QrCode_Link;
        this.scanned_QrCode_Image = scanned_QrCode_Image;
        this.scanned_QrCode_ImageType = scanned_QrCode_ImageType;
    }

    public ScannedQrCode(int id, String scanned_QrCode_Type, String scanned_QrCode_Link, byte[] scanned_QrCode_Image, int scanned_QrCode_ImageType) {
        this.id = id;
        this.scanned_QrCode_Type = scanned_QrCode_Type;
        this.scanned_QrCode_Link = scanned_QrCode_Link;
        this.scanned_QrCode_Image = scanned_QrCode_Image;
        this.scanned_QrCode_ImageType = scanned_QrCode_ImageType;
    }

    public ScannedQrCode(String scanned_QrCode_Type, String scanned_QrCode_Link, byte[] scanned_QrCode_Image, int scanned_QrCode_ImageType, String scanned_QrCode_DateTime) {
        this.scanned_QrCode_Type = scanned_QrCode_Type;
        this.scanned_QrCode_Link = scanned_QrCode_Link;
        this.scanned_QrCode_Image = scanned_QrCode_Image;
        this.scanned_QrCode_ImageType = scanned_QrCode_ImageType;
        this.scanned_QrCode_DateTime = scanned_QrCode_DateTime;
    }

    public ScannedQrCode(int id, String scanned_QrCode_Type, String scanned_QrCode_Link, byte[] scanned_QrCode_Image, int scanned_QrCode_ImageType, String scanned_QrCode_DateTime) {
        this.id = id;
        this.scanned_QrCode_Type = scanned_QrCode_Type;
        this.scanned_QrCode_Link = scanned_QrCode_Link;
        this.scanned_QrCode_Image = scanned_QrCode_Image;
        this.scanned_QrCode_ImageType = scanned_QrCode_ImageType;
        this.scanned_QrCode_DateTime = scanned_QrCode_DateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScanned_QrCode_Type() {
        return scanned_QrCode_Type;
    }

    public void setScanned_QrCode_Type(String scanned_QrCode_Type) {
        this.scanned_QrCode_Type = scanned_QrCode_Type;
    }

    public String getScanned_QrCode_Link() {
        return scanned_QrCode_Link;
    }

    public void setScanned_QrCode_Link(String scanned_QrCode_Link) {
        this.scanned_QrCode_Link = scanned_QrCode_Link;
    }

    public byte[] getScanned_QrCode_Image() {
        return scanned_QrCode_Image;
    }

    public void setScanned_QrCode_Image(byte[] scanned_QrCode_Image) {
        this.scanned_QrCode_Image = scanned_QrCode_Image;
    }

    public int getScanned_QrCode_ImageType() {
        return scanned_QrCode_ImageType;
    }

    public void setScanned_QrCode_ImageType(int scanned_QrCode_ImageType) {
        this.scanned_QrCode_ImageType = scanned_QrCode_ImageType;
    }

    public String getScanned_QrCode_DateTime() {
        return scanned_QrCode_DateTime;
    }

    public void setScanned_QrCode_DateTime(String scanned_QrCode_DateTime) {
        this.scanned_QrCode_DateTime = scanned_QrCode_DateTime;
    }
}
