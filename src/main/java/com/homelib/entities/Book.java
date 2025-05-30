package com.homelib.entities;



import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Book {
    private String title;
    private String firstNameAuthor;
    private String lastNameAuthor;
    private int year;
    private int edition;
    private int id;

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", firstNameAuthor='" + firstNameAuthor + '\'' +
                ", lastNameAuthor='" + lastNameAuthor + '\'' +
                ", year=" + year +
                ", edition=" + edition +
                ", id=" + id +
                '}';
    }

    private Book(String name, String firstNameAuthor, String lastNameAuthor, int year, int edition, int id) {
        this.title = name;
        this.firstNameAuthor = firstNameAuthor;
        this.lastNameAuthor = lastNameAuthor;
        this.year = year;
        this.edition = edition;
        this.id = id;
    }

    private Book(String title, String firstNameAuthor, String lastNameAuthor, int year, int edition) {
        this.title = title;
        this.firstNameAuthor = firstNameAuthor;
        this.lastNameAuthor = lastNameAuthor;
        this.year = year;
        this.edition = edition;
    }

    public String getAuthor() {
        return firstNameAuthor + " " + lastNameAuthor;
    }


    public static final class BookBuilder{
        private String title;
        private String firstNameAuthor;
        private String lastNameAuthor;
        private int year;
        private int edition;
        private int id;

        public BookBuilder() {
        }

        public static BookBuilder builder(){
            return new BookBuilder();
        }

        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder firstNameAuthor(String firstNameAuthor) {
            this.firstNameAuthor = firstNameAuthor;
            return this;
        }

        public BookBuilder lastNameAuthor(String lastNameAuthor) {
            this.lastNameAuthor = lastNameAuthor;
            return this;
        }

        public BookBuilder year(int year) {
            this.year = year;
            return this;
        }

        public BookBuilder edition(int edition) {
            this.edition = edition;
            return this;
        }

        public BookBuilder id(int id){
            this.id = id;
            return this;
        }



        public Book build(){
            return new Book(title, firstNameAuthor, lastNameAuthor, year, edition, id);
        }


    }


}
