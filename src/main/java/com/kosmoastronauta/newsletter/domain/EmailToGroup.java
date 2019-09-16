package com.kosmoastronauta.newsletter.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmailToGroup
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long emailId;
    private long groupId;
    private boolean active;

    public EmailToGroup() {}

    public EmailToGroup(long emailId, long groupId)
    {
        this.emailId = emailId;
        this.groupId = groupId;
        this.active = true;
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

    public long getEmailId() {
        return emailId;
    }

    public void setEmailId(long emailId) {
        this.emailId = emailId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
