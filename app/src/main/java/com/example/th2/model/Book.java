package com.example.th2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;
    private String date;
    private List<String> types;

    public Book() {
        types = new ArrayList<>();
    }

    public Book(int id, String title, String author, String date) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.types = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void addType(String type) {
        if (!types.contains(type)) {
            types.add(type);
        }
    }

    public String getTypesString() {
        if (types.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String type : types) {
            sb.append(type).append(", ");
        }

        // Remove last comma and space
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }
}