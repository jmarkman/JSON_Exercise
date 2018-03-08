package com.learningprojects.jmarkman.books;

import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
// Use the following instead of android.widget.SearchView
// See: https://stackoverflow.com/questions/24522696/android-widget-searchview-cannot-be-cast-to-android-support-v7-widget-searchview
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ProgressBar mLoadingProgress;
    private RecyclerView rvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        mLoadingProgress = (ProgressBar) findViewById(R.id.pb_loading);

        try
        {
            URL bookUrl = ApiUtil.buildURL("cooking");
            new BooksQueryTask().execute(bookUrl);
        }
        catch (Exception e)
        {
            Log.d("Error", e.getMessage());
            e.printStackTrace();
        }

        LinearLayoutManager booksLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        try
        {
            URL bookUrl = ApiUtil.buildURL(query);
            new BooksQueryTask().execute(bookUrl);
        }
        catch (Exception e)
        {
            Log.d("Error", e.getMessage());
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String>
    {
        @Override
        protected String doInBackground(URL... urls)
        {
            URL searchUrl = urls[0];
            String result = null;

            try
            {
                result = ApiUtil.getJSON(searchUrl);
            }
            catch (IOException e)
            {
                Log.d("Error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            TextView tvError = (TextView) findViewById(R.id.tv_error);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if (result == null)
            {
                rvBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);
            }
            else
            {
                rvBooks.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
            }

            ArrayList<Book> books = ApiUtil.getBooksFromJson(result);

            BooksAdapter adapter = new BooksAdapter(books);
            rvBooks.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
