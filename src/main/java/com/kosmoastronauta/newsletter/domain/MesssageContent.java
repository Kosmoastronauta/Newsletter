package com.kosmoastronauta.newsletter.domain;

import java.util.List;

public class MesssageContent
{
    private String subject;
    private String content;
    private List<Integer> groups;

    public List<Integer> getGroups() {
        return groups;
    }

    public String getSubject() {
        return subject;
    }

    public void setGroups(List<Integer> groups) {
        this.groups = groups;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return content;
    }

    public void setBody(String body) {
        this.content = body;
    }
}
