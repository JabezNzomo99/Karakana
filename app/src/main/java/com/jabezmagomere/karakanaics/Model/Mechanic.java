package com.jabezmagomere.karakanaics.Model;

public class Mechanic {
    private String FirstName, LastName, Email, PhoneNumber, Rating, Location,OpenTill,PhotoURL, Speciallity;

    public String getSpeciallity() {
        return Speciallity;
    }

    public void setSpeciallity(String speciallity) {
        Speciallity = speciallity;
    }

    public Mechanic(String firstName, String lastName, String email, String phoneNumber, String rating, String location, String openTill, String photoURL, String speciallity) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        PhoneNumber = phoneNumber;
        Rating = rating;
        Location = location;
        OpenTill = openTill;
        PhotoURL = photoURL;
        Speciallity = speciallity;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
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

    public String getOpenTill() {
        return OpenTill;
    }

    public void setOpenTill(String openTill) {
        OpenTill = openTill;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }
}
