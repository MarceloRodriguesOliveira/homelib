package com.homelib.repository;


import com.homelib.connection.ConnectionFactory;
import com.homelib.entities.Author;
import com.homelib.entities.Book;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class BookRepository {
    AuthorRepository authorRepository = new AuthorRepository();
    BookAuthorRepository bookAuthorRepository = new BookAuthorRepository();
    public Book save(Book book){
        try(Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = createPrepareStatementSave(conn, book);) {
            ps.execute();
            authorRepository.SaveAuthors(conn, book.getAuthors());

            try( ResultSet rs = ps.getGeneratedKeys();){
                if(rs.next()){
                    book.setId(rs.getLong(1));
                }
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long saveSingleBook(Connection conn, Book book) throws SQLException{
        try(PreparedStatement ps = createPrepareStatementSave(conn, book)){
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) return rs.getLong(1);
            }
        }
        throw new SQLException("Failed to save book");
    }

    private static PreparedStatement createPrepareStatementSave(Connection conn, Book book ) throws SQLException {
        String sql = "INSERT INTO `book_store` (`title`, `year`, `edition`, `publisher`, `locale`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, book.getTitle());
        ps.setInt(2, book.getYear());
        ps.setInt(3, book.getEdition());
        ps.setString(4, book.getPublisher());
        ps.setString(5, book.getLocale().toString());

        LocalDateTime now = LocalDateTime.now();
        ps.setTimestamp(6, Timestamp.valueOf(now));
        ps.setTimestamp(7, Timestamp.valueOf(now));

        return ps;
    }

    public List<Book> findAllBooks(String title){
        List<Book> booksFound = new ArrayList<Book>();
        List<Author> bookAuthors = new ArrayList<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementFindByTitle(conn, title);
            ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                Book book = Book.BookBuilder
                        .builder()
                        .title(rs.getString("title"))
                        .authors(List.of(new Author(rs.getString("firstnameauthor"), rs.getString("lastnameauthor"))))
                        .year(rs.getInt("year"))
                        .edition(rs.getInt("edition"))
                        .id(rs.getLong("id"))
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
                     SELECT title, year, edition, id, publisher, locale FROM book_store WHERE title LIKE ?;
                     """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", title));
        return ps;
    }



    public Optional<Book> findById(Long targetID){
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementFindById(conn, targetID);
            ResultSet rs = ps.executeQuery();
        ){
            if(!rs.next()) return Optional.empty();
            return Optional.of(Book.BookBuilder
                    .builder()
                    .title(rs.getString("title"))
                    .authors(List.of(new Author(rs.getString("firstnameauthor"), rs.getString("lastnameauthor"))))
                    .year(rs.getInt("year"))
                    .edition(rs.getInt("edition"))
                    .id(rs.getLong("id"))
                    .build());
        }catch (SQLException e){
            log.error("Error while searching for this id");
        }
        return Optional.empty();
    }

    public static PreparedStatement createPreparedStatementFindById(Connection conn, Long id) throws SQLException{
        String sql = "SELECT * FROM `book_store` WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, id);
        return ps;
    }

    public void deleteBookById(Long id){
       try(Connection conn = ConnectionFactory.getConnection();
           PreparedStatement ps = createPreparedStatementDeleteById(conn, id)
          ){
           log.info("Deleting...");
           ps.execute();
       }catch (SQLException e){
           log.error("Error while deleting register");
       }

    }

    public static PreparedStatement createPreparedStatementDeleteById(Connection conn, Long id) throws SQLException{
        String sql = "DELETE FROM `book_store` WHERE (`id` = ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        return ps;
    }

    public void update(Book book){
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementUpdate(conn, book)){
            ps.execute();

        }catch (SQLException e){
            log.error("Erro ao atualizar o livro");
        }
    }


    public static PreparedStatement createPreparedStatementUpdate(Connection conn, Book book) throws SQLException{
        String sql = "UPDATE `book_store` SET `title` = ?, `firstnameauthor` = ?, `lastnameauthor` = ?, `year` = ?, `edition` = ? WHERE (`id` = ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getFirstNameAuthor());
        ps.setString(3, book.getLastNameAuthor());
        ps.setInt(4, book.getYear());
        ps.setInt(5, book.getEdition());
        ps.setLong(6, book.getId());

        return ps;
    }

    public void saveFromImportedList(List<Book> importedBookList){
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementSaveBatchFromFile(conn, importedBookList)){
            conn.setAutoCommit(false);
            ps.executeBatch();
            conn.commit();
        }catch (SQLException e){
            System.err.println(e);
        }

    }

    private static PreparedStatement createPreparedStatementSaveBatchFromFile(Connection conn, List<Book> bookList_csv) throws SQLException {
        String sql = "INSERT INTO `book_store` (`title`, `firstnameauthor`, `lastnameauthor`, `year`, `edition`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for(Book book: bookList_csv){
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getFirstNameAuthor());
            ps.setString(3, book.getLastNameAuthor());
            ps.setInt(4, book.getYear());
            ps.setInt(5, book.getEdition());
            ps.addBatch();
        }

        return ps;

    }




}
