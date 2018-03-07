package com.learningprojects.jmarkman.books;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
}
