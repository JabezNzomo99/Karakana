package com.jabezmagomere.karakanaics.Model;

public class Recovery {
    private String RecoveryName, PhoneNumber, Email, PhotoURL,Rating,Location;

    public Recovery(String recoveryName, String phoneNumber, String email, String photoURL, String rating, String location) {
        RecoveryName = recoveryName;
        PhoneNumber = phoneNumber;
        Email = email;
        PhotoURL = photoURL;
        Rating = rating;
        Location = location;
    }

    public String getRecoveryName() {
        return RecoveryName;
    }

    public void setRecoveryName(String recoveryName) {
        RecoveryName = recoveryName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
