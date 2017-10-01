package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class BooksLoader extends AsyncTaskLoader<List<Book>> {

    private String mInsertText;

    public BooksLoader(Context context, String insertText) {
        super(context);
        mInsertText = insertText;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mInsertText == null) {
            return null;
        }

        List<Book> books = Query.fetchData(mInsertText);
        return books;
    }
}
