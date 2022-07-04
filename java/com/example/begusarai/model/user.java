package com.example.begusarai.model;

public class user {
    private String name;
    private String id;
    private String imageUrl;
    private String bio;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public user(){

    }

    public user(String name, String email, String id, String imageUrl, String bio, String password) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}

