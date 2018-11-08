package com.aaa.json;

import java.util.LinkedList;
import java.util.List;

public class Library {

    @Transient
    private int visitors;

    private List<Book> list;

    public Library(int visitors) {

        this.visitors = visitors;

        this.list = new LinkedList<Book>();
        list.add(new Book("First", 40));
        list.add(new Book("Second", 30));
        list.add(new Book("Third", 50));
        list.add(new Book("Fourth", 45));

    }
}
