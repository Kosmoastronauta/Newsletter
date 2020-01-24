package com.kosmoastronauta.newsletter.domain;

import javax.persistence.*;

@Entity
public class EmailAddress
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public EmailAddress() {}

    public EmailAddress(String address)
    {
        this.address = address;
    }

    public EmailAddress(String address, long groupId)
    {
        this.address = address;
        this.groupId = groupId;
    }

    private String address;
    private long groupId;
    private String pubKey;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
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
