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
    private UUID id;

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

    public Book(String name, String firstNameAuthor, String lastNameAuthor, int year, int edition) {
        this.title = name;
        this.firstNameAuthor = firstNameAuthor;
        this.lastNameAuthor = lastNameAuthor;
        this.year = year;
        this.edition = edition;
        this.id = UUID.randomUUID();
    }

    public static final class BookBuilder{
        private String title;
        private String firstNameAuthor;
        private String lastNameAuthor;
        private int year;
        private int edition;

        public BookBuilder() {
        }

        public static BookBuilder builder(){
            return new BookBuilder();
        }

        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder FirstNameAuthor(String firstNameAuthor) {
            this.firstNameAuthor = firstNameAuthor;
            return this;
        }

        public BookBuilder LastNameAuthor(String lastNameAuthor) {
            this.lastNameAuthor = lastNameAuthor;
            return this;
        }

        public BookBuilder Year(int year) {
            this.year = year;
            return this;
        }

        public BookBuilder Edition(int edition) {
            this.edition = edition;
            return this;
        }

        public Book build(){
            return new Book(title, firstNameAuthor, lastNameAuthor, year, edition);
        }


    }


}
