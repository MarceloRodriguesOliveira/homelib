package com.homelib.repository;


import com.homelib.connection.ConnectionFactory;
import com.homelib.entities.Book;
import com.homelib.entities.GlobalStore;
import com.homelib.service.BookService;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
public class BookRepository {
    public static void saveToDatabase(Book book){
        GlobalStore.getInstance().getData().add(book);
    }

    public Book save(Book book){
        try(Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = createPrepareStatementSave(conn, book);) {
            ps.execute();

            try( ResultSet rs = ps.getGeneratedKeys();){
                if(rs.next()){
                    book.setId(rs.getInt(1));
                }
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static PreparedStatement createPrepareStatementSave(Connection conn, Book book ) throws SQLException {
        String sql = "INSERT INTO `book_store` (`title`, `firstnameauthor`, `lastnameauthor`, `year`, `edition`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getFirstNameAuthor());
        ps.setString(3, book.getLastNameAuthor());
        ps.setInt(4, book.getYear());
        ps.setInt(5, book.getEdition());
        return ps;
    }

    public List<Book> findAllBooks(String title){
        List<Book> booksFound = new ArrayList<Book>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementFindByTitle(conn, title);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                Book book = Book.BookBuilder
                        .builder()
                        .title(rs.getString("title"))
                        .firstNameAuthor(rs.getString("firstnameauthor"))
                        .lastNameAuthor(rs.getString("lastnameauthor"))
                        .year(rs.getInt("year"))
                        .edition(rs.getInt("edition"))
                        .id(rs.getInt("id"))
                        .build();
                booksFound.add(book);
            }
        }catch (SQLException e){
            log.error("Error while retrieving data");
        }
        return booksFound;
    }

    public static PreparedStatement createPreparedStatementFindByTitle(Connection conn, String title) throws SQLException {
        String sql = """
                     SELECT title, firstnameauthor, lastnameauthor, year, edition, id FROM book_store WHERE title LIKE ?;
                     """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", title));
        return ps;
    }



    public Optional<Book> findById(int targetID){
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementFindById(conn, targetID);
            ResultSet rs = ps.executeQuery();
        ){
            if(!rs.next()) return Optional.empty();
            return Optional.of(Book.BookBuilder
                    .builder()
                    .title(rs.getString("title"))
                    .firstNameAuthor(rs.getString("firstnameauthor"))
                    .lastNameAuthor(rs.getString("lastnameauthor"))
                    .year(rs.getInt("year"))
                    .edition(rs.getInt("edition"))
                    .id(rs.getInt("id"))
                    .build());
        }catch (SQLException e){
            log.error("Error while searching for this id");
        }
        return Optional.empty();
    }

    public static PreparedStatement createPreparedStatementFindById(Connection conn, int id) throws SQLException{
        String sql = "SELECT * FROM `book_store` WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, id);
        return ps;
    }

    public void deleteBookById(int searchId){
        Optional<Book> bookFromDb = findById(searchId);
        if (bookFromDb.isPresent()){
            Book book = bookFromDb.get();
            GlobalStore.getInstance().getData().removeIf(b -> b.getId()==(searchId));
            log.info("'{}' by {}, {} is being deleted", book.getTitle(), book.getFirstNameAuthor(), book.getLastNameAuthor());
            System.out.println("Esse registro foi apagado");
            return;
        }
        log.info("Id is not present on any book register");

    }

    /*public static PreparedStatement createPreparedStatementDeleteById(){

    }*/




}
