package com.learningprojects.jmarkman.books;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jmarkman on 3/7/2018.
 */

public class Book implements Parcelable
{
    public String id;
    public String title;
    public String subtitle;
    public String[] authors;
    public String publisher;
    public String publishedDate;


    public Book(String id, String title, String subtitle, String[] authors, String publisher, String publishedDate)
    {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
    }


    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        authors = in.createStringArray();
        publisher = in.readString();
        publishedDate = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeStringArray(authors);
        parcel.writeString(publisher);
        parcel.writeString(publishedDate);
    }
}
