package com.learningprojects.jmarkman.books;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jmarkman on 3/7/2018.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>
{
    ArrayList<Book> books;

    public BooksAdapter(ArrayList<Book> books)
    {
        this.books = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.book_list_item, parent, false);

        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position)
    {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView tvTitle;
        TextView tvAuthors;
        TextView tvDate;
        TextView tvPublisher;

        public BookViewHolder(View itemView)
        {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthors = (TextView) itemView.findViewById(R.id.tv_authors);
            tvDate = (TextView) itemView.findViewById(R.id.tv_published_date);
            tvPublisher = (TextView) itemView.findViewById(R.id.tv_publisher);
        }
        
        public void bind (Book book)
        {
            tvTitle.setText(book.title);
            String authors = "";

            for (int i = 0; i < book.authors.length; i++)
            {
                authors += book.authors[i];
                if (i < book.authors.length)
                    authors += ", ";
            }

            tvAuthors.setText(authors);
            tvDate.setText(book.publishedDate);
            tvPublisher.setText(book.publisher);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
