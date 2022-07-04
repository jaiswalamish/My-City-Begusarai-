package com.example.begusarai.model;

public class data {

    private String name;
    private String imageUrl;
    private String address;
    private String description;
    private String phone;
    private String email;
    private String link;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    public data(){

    }

    public data(String name, String imageUrl, String address, String description, String phone, String email, String link, String userId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.description = description;
        this.phone = phone;
        this.email = email;
        this.link = link;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
