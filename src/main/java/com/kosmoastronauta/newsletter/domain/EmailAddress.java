package com.kosmoastronauta.newsletter.domain;

import javax.persistence.*;

@Entity
public class EmailAddress
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String address;
    private String groupEmail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroup() {
        return groupEmail;
    }

    public void setGroup(String groupEmail) {
        this.groupEmail = groupEmail;
    }
}
