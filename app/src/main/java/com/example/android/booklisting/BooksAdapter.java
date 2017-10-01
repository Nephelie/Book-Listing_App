package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class BooksAdapter extends ArrayAdapter<Book> {

    public BooksAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.books_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentBook.getmTitle());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        authorTextView.setText(currentBook.getmAuthor());

        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.description);
        descriptionTextView.setText(currentBook.getmDescription());

        return listItemView;
    }
}
