package com.example.captureimage.Classes;

import java.io.Serializable;

public class VendorDetails implements Serializable
{
    private String materialtype;
    private String materialunit;
    private String price;
    private String city;
    private String name;
    private String phone;
    private String adinfo;
    public String imageURL;

    public VendorDetails(String materialtype, String materialunit, String price, String city, String name, String phone, String adinfo, String imageURL) {
        this.materialtype = materialtype;
        this.materialunit = materialunit;
        this.price = price;
        this.city = city;
        this.name = name;
        this.phone = phone;
        this.adinfo = adinfo;
        this.imageURL = imageURL;
    }

    public VendorDetails()
    {
        // This is default constructor.
    }

    public void setMaterialType(String materialtype) { this.materialtype = materialtype; }
    public void setMaterialUnit(String materialunit) { this.materialunit = materialunit; }
    public void setPrice(String price) { this.price = price; }
    public void setAdInfo(String adinfo) { this.adinfo = adinfo; }
    public void setCity(String city) { this.city = city; }
    public void setVendorName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public String getImageURL() { return imageURL; }
    public String getMaterialType() { return materialtype; }
    public String getMaterialUnit() { return materialunit; }
    public String getPrice() { return price; }
    public String getAdInfo() { return adinfo; }
    public String getCity() { return city; }
    public String getVendorName() { return name; }
    public String getPhone() { return phone; }
}
