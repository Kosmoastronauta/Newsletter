package com.kosmoastronauta.newsletter.domain;

import javax.persistence.*;

@Entity
public class EmailAddress
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String address;
    private int groupId;
    private boolean active;

    public int getGroupEmail() {
        return groupId;
    }

    public void setGroupEmail(int groupId) {
        this.groupId = groupId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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
}
