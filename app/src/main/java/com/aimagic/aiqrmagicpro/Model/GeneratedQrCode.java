package com.aimagic.aiqrmagicpro.Model;

public class GeneratedQrCode {

    private int id;
    private String generated_QrCode_Type;
    private String generated_QrCode_Link;
    private byte[] generated_QrCode_Image;

    private int generated_QrCode_ImageType;
    private String generated_QrCode_DateTime;

    public GeneratedQrCode() {

    }

    public GeneratedQrCode(String generated_QrCode_Type, String generated_QrCode_Link, byte[] generated_QrCode_Image , int generated_QrCode_ImageType) {
        this.generated_QrCode_Type = generated_QrCode_Type;
        this.generated_QrCode_Link = generated_QrCode_Link;
        this.generated_QrCode_Image = generated_QrCode_Image;
        this.generated_QrCode_ImageType = generated_QrCode_ImageType;
    }

    public GeneratedQrCode(int id, String generated_QrCode_Type, String generated_QrCode_Link, byte[] generated_QrCode_Image , int generated_QrCode_ImageType) {
        this.id = id;
        this.generated_QrCode_Type = generated_QrCode_Type;
        this.generated_QrCode_Link = generated_QrCode_Link;
        this.generated_QrCode_Image = generated_QrCode_Image;
        this.generated_QrCode_ImageType = generated_QrCode_ImageType;
    }

    public GeneratedQrCode(String generated_QrCode_Type, String generated_QrCode_Link, byte[] generated_QrCode_Image , int generated_QrCode_ImageType , String generated_QrCode_DateTime) {
        this.generated_QrCode_Type = generated_QrCode_Type;
        this.generated_QrCode_Link = generated_QrCode_Link;
        this.generated_QrCode_Image = generated_QrCode_Image;
        this.generated_QrCode_ImageType = generated_QrCode_ImageType;
        this.generated_QrCode_DateTime = generated_QrCode_DateTime;
    }

    public GeneratedQrCode(int id, String generated_QrCode_Type, String generated_QrCode_Link, byte[] generated_QrCode_Image , int generated_QrCode_ImageType , String generated_QrCode_DateTime) {
        this.id = id;
        this.generated_QrCode_Type = generated_QrCode_Type;
        this.generated_QrCode_Link = generated_QrCode_Link;
        this.generated_QrCode_Image = generated_QrCode_Image;
        this.generated_QrCode_ImageType = generated_QrCode_ImageType;
        this.generated_QrCode_DateTime = generated_QrCode_DateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenerated_QrCode_Type() {
        return generated_QrCode_Type;
    }

    public void setGenerated_QrCode_Type(String generated_QrCode_Type) {
        this.generated_QrCode_Type = generated_QrCode_Type;
    }

    public String getGenerated_QrCode_Link() {
        return generated_QrCode_Link;
    }

    public void setGenerated_QrCode_Link(String generated_QrCode_Link) {
        this.generated_QrCode_Link = generated_QrCode_Link;
    }

    public byte[] getGenerated_QrCode_Image() {
        return generated_QrCode_Image;
    }

    public void setGenerated_QrCode_Image(byte[] generated_QrCode_Image) {
        this.generated_QrCode_Image = generated_QrCode_Image;
    }

    public String getGenerated_QrCode_DateTime() {
        return generated_QrCode_DateTime;
    }

    public void setGenerated_QrCode_DateTime(String generated_QrCode_DateTime) {
        this.generated_QrCode_DateTime = generated_QrCode_DateTime;
    }

    public int getGenerated_QrCode_ImageType() {
        return generated_QrCode_ImageType;
    }

    public void setGenerated_QrCode_ImageType(int generated_QrCode_ImageType) {
        this.generated_QrCode_ImageType = generated_QrCode_ImageType;
    }
}
