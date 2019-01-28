
package com.example.user.taxi.models;

import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("type")
    private String type;

    @SerializedName("contact")
    private String contact;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
