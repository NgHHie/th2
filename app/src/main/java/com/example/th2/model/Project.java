package com.example.th2.model;

import java.util.Date;

public class Project {
    private String id;
    private String name;
    private Date start;
    private Date end;
    private boolean done;

    public Project() {
    }

    public Project(String id, String name, Date start, Date end, boolean done) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.done = done;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}