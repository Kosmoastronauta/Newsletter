package com.kosmoastronauta.newsletter.domain;

import javax.persistence.*;
import java.security.Key;

@Entity
public class EmailAddress
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String address;
    private int groupId;
    private boolean active;
    private Key pubKey;
    private Key privKey;

    public Key getPrivKey() {
        return privKey;
    }

    public void setPrivKey(Key privKey) {
        this.privKey = privKey;
    }

    public Key getPubKey() {
        return pubKey;
    }

    public void setPubKey(Key pubKey) {
        this.pubKey = pubKey;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
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
