package com.homelib.entities;



import com.homelib.enums.PublisherLocale;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Book {
    private String title;
    private List<Author> authors;
    private int year;
    private int edition;
    private Long id;
    private String publisher;
    private PublisherLocale locale;

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                ", year=" + year +
                ", edition=" + edition +
                ", id=" + id +
                ", publisher='" + publisher + '\'' +
                ", locale=" + locale +
                '}';
    }

    public Book(String title, List<Author> authors, int year, int edition, String publisher, PublisherLocale locale) {
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.edition = edition;
        this.publisher = publisher;
        this.locale = locale;
    }

    public Book(String title, List<Author> authors, int year, int edition, Long id, String publisher, PublisherLocale locale) {
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.edition = edition;
        this.id = id;
        this.publisher = publisher;
        this.locale = locale;
    }

    public String getFullName(Author author){

        return author.getFirstName() + " " + author.getLastName();
    }

    public String getAuthorsFullName(List<Author> authors){
        return authors.stream()
                .map(a -> a.getFirstName() + " " + a.getLastName())
                .collect(Collectors.joining(", "));

    }

    public static final class BookBuilder{
        private String title;
        private List<Author> authors;
        private int year;
        private int edition;
        private Long id;
        private String publisher;
        private PublisherLocale locale;

        public BookBuilder() {
        }

        public static BookBuilder builder(){
            return new BookBuilder();
        }

        public BookBuilder title(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder authors(List<Author> authors){
            this.authors = authors;
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

        public BookBuilder id(Long id){
            this.id = id;
            return this;
        }

        public BookBuilder publisher(String publisher){
            this.publisher = publisher;
            return this;
        }
        public BookBuilder locale(PublisherLocale locale){
            this.locale = locale;
            return this;
        }

        public Book build(){
            return new Book(title, authors, year, edition, id, publisher, locale);
        }


    }


}
