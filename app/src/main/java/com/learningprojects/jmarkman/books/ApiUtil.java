package com.learningprojects.jmarkman.books;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by jmarkman on 3/2/2018.
 */

public class ApiUtil
{
    private ApiUtil() { }

    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";

    private static final String QUERY_PARAMETER_KEY = "q";
    public static final String KEY = "key";
    public static final String API_KEY = "AIzaSyAz_uuFALaLoqgH_kwKBYuyMrWO4s9mzFw";

    public static URL buildURL(String title)
    {
        // Not the recommended approach to building a api query
/*        String fullUrl = BASE_API_URL + "?q=" + title;
        URL url = null;

        try
        {
            url = new URL(fullUrl);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return url;*/

        // The recommended approach
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                .appendQueryParameter(KEY, API_KEY)
                .build();

        try
        {
            url = new URL(uri.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return url;
    }

    public static String getJSON(URL url) throws IOException
    {
        // Establish connection to the api
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Create a stream to read the data from the api
        InputStream stream = connection.getInputStream();
        // Instantiate a scanner to read the contents of the stream,
        // allowing us to parse the stream
        Scanner scanner = new Scanner(stream);
        scanner.useDelimiter("\\A");

        // true if data is present in the stream, else, false
        boolean hasData = scanner.hasNext();

        try
        {
            if (hasData)
            {
                return scanner.next();
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            Log.d("Error", e.toString());
            return null;
        }
        finally
        {
            // Close up the connection to the api to prevent memory leaks and other issues
            connection.disconnect();
        }
    }

    public static ArrayList<Book> getBooksFromJson(String json)
    {
        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";

        ArrayList<Book> books = new ArrayList<>();

        try
        {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();

            for (int i = 0; i < numberOfBooks; i++)
            {
                JSONObject bookJSON = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJSON = bookJSON.getJSONObject(VOLUME_INFO);
                int authorNum = volumeInfoJSON.getJSONArray(AUTHORS).length();
                String[] authors = new String[authorNum];
                for (int j = 0; j < authorNum; j++)
                {
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();
                }

                Book book = new Book(
                        bookJSON.getString(ID),
                        volumeInfoJSON.getString(TITLE),
                        (volumeInfoJSON.isNull(SUBTITLE) ? "" : volumeInfoJSON.getString(SUBTITLE)),
                        authors,
                        volumeInfoJSON.getString(PUBLISHER),
                        volumeInfoJSON.getString(PUBLISHED_DATE)
                );

                books.add(book);
            }
        }
        catch (JSONException jse)
        {
            jse.printStackTrace();
        }


        return books;
    }
}
