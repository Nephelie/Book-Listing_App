package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Query {

    private static final String LOG_TAG = Query.class.getSimpleName();

    private Query() {
    }

    public static final List<Book> fetchData(String insertWord) {
        URL url = createUrl(insertWord);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> books = extractFromJson(jsonResponse);

        return books;
    }

    private static URL createUrl(String insertWord) {
        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/books/v1/volumes?q=programming" + insertWord);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;

    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream stream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                stream = urlConnection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (stream != null) {

                stream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();
    }

    private static List<Book> extractFromJson(String responseJSON) {
        if (TextUtils.isEmpty(responseJSON)) {
            return null;
        }
        List<Book> books = new ArrayList<>();
        try {
            JSONObject volumes = new JSONObject(responseJSON);
            JSONArray booksArray ;

            if(volumes.has("items")) {
                booksArray = volumes.getJSONArray("items");

            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject book = booksArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                String authors;
                if (volumeInfo.has("authors")) {
                    authors = volumeInfo.getString("authors");
                } else {
                    authors = "Author N/A";
                }

                String title ;
                if (volumeInfo.has("title")){
                    title = volumeInfo.getString("title");
                }else{
                    title = "Title N/A";
                }
                String description ;

                if (volumeInfo.has("description")){
                    description = volumeInfo.getString("description");
                }else{
                    description = "Description N/A";
                }

                String url = volumeInfo.getString("infoLink");


                Book book1 = new Book(title, authors, description, url);
                books.add(book1);
            }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Can't extract the data from JSON response", e);
        }
        return books;
    }
}
