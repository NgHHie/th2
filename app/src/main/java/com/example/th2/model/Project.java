package com.example.th2.model;

import java.io.Serializable;
import java.util.Date;

public class Project implements Serializable {
    private String id; // Auto-generated 2-digit ID
    private String name;
    private Date startDate;
    private Date endDate;
    private boolean completed;

    public Project() {
    }

    public Project(String id, String name, Date startDate, Date endDate, boolean completed) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}