package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklisting.R.id.search;

public class BooksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {


    private SearchView mSearch;

    private String mInsertText;

    private BooksAdapter mAdapter;

    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        ListView booksListView = (ListView) findViewById(R.id.list);

        mEmptyTextView = (TextView) findViewById(R.id.empty_textView);
        booksListView.setEmptyView(mEmptyTextView);

        mAdapter = new BooksAdapter(this, new ArrayList<Book>());
        booksListView.setAdapter(mAdapter);

        mSearch = (SearchView) findViewById(search);
        mSearch.setSubmitButtonEnabled(true);


        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = mAdapter.getItem(position);

                Uri bookUri = Uri.parse(currentBook.getmUrl());

                Intent webIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(webIntent);
            }

        });

        ConnectivityManager cManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

            getLoaderManager().initLoader(0, null, this);
        } else {
            mEmptyTextView.setText("No internet connection");
            mEmptyTextView.setGravity(Gravity.CENTER);
        }

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                ConnectivityManager cManager = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

                    getLoaderManager().restartLoader(0, null, BooksActivity.this);

                    return true;

                }

                else {
                    mAdapter.clear();

                    mEmptyTextView.setText("No internet connection.");

                    return false;
                }
            }

        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        mInsertText = mSearch.getQuery().toString();

        BooksLoader loader = new BooksLoader(this, mInsertText);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

        mEmptyTextView.setText("No books found.");
        mEmptyTextView.setGravity(Gravity.CENTER);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("insert text", mInsertText);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInsertText = savedInstanceState.getString("insert text");
    }
}
