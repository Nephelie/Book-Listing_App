package com.example.android.booklisting;


public class Book {

    private String mTitle;
    private String mAuthor;
    private String mDescription;
    private String mUrl;

    public Book(String title, String author, String description, String url) {
        mTitle = title;
        mAuthor = author;
        mDescription = description;
        mUrl = url;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmUrl() {
        return mUrl;
    }
}
