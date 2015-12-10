package ru.frohenk.ptlhomeworks;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by User on 05.09.2015.
 */
public class Homework  {
    private Integer id;
    private String  subject;
    private Calendar date;
    private String body;
    private Calendar createdOn;

    public Homework(Integer id,
                    String subject,
                    Calendar date,
                    String body,
                    Calendar createdOn) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.body = body;
        this.createdOn = createdOn;
    }

    public Calendar getCreatedOn() {
        return createdOn;
    }

    public String getSubject() {
        return subject;
    }

    public Calendar getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatedOn(Calendar createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return subject + ":\n" + body;
    }

    public Integer getId() {
        return id;
    }
}
