package com.kosmoastronauta.newsletter.domain;

import java.util.List;

public class Message
{
    private String subject;
    private String body;
    private List<String> groups;

    public List<String> getGroups() {
        return groups;
    }

    public String getSubject() {
        return subject;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
