package com.jnu.student.data;

import java.io.Serializable;

public class Book implements Serializable {
    public int getCoverResourceId() {
        return imageResourceId;
    }

    private int imageResourceId;

    public String getName() {
        return name;
    }

    private String name;

    public Book(String name_, int imageResourceId_) {
        this.name=name_;
        this.imageResourceId =imageResourceId_;
    }

    public void setName(String name) {
        this.name = name;
    }
}
