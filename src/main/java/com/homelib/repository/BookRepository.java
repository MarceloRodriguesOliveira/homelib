package com.homelib.repository;


import com.homelib.connection.ConnectionFactory;
import com.homelib.entities.Author;
import com.homelib.entities.Book;
import com.homelib.enums.PublisherLocale;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class BookRepository {
    AuthorRepository authorRepository = new AuthorRepository();
    BookAuthorRepository bookAuthorRepository = new BookAuthorRepository();

    public Book SaveWithAuthors(Book book){
        try(Connection conn = ConnectionFactory.getConnection()){
            conn.setAutoCommit(false);

            try{
                List<Long> authorIds = authorRepository.SaveAuthors(conn, book.getAuthors());
                Book savedBook = save(conn, book);
                bookAuthorRepository.saveBookAuthorLink(conn, savedBook.getId(), authorIds);

                conn.commit();
                return savedBook;

            }catch (SQLException e){
                conn.rollback();
                throw new RuntimeException("Failed to save book alongside authors", e);
            }

        }catch (SQLException e){
            throw new RuntimeException("Connection failed", e);
        }

    }


    public Book save(Connection conn,Book book){
        try(PreparedStatement ps = createPrepareStatementSave(conn, book);) {
            ps.execute();


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
        Map<Long, Book> bookMap = new LinkedHashMap<>();
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementFindByTitle(conn, title);
            ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                long bookId = rs.getLong("book_id");

                Book book = bookMap.get(bookId);
                if(book == null){
                    book = Book.BookBuilder
                            .builder()
                            .id(bookId)
                            .title(rs.getString("title"))
                            .year(rs.getInt("year"))
                            .edition(rs.getInt("edition"))
                            .publisher(rs.getString("publisher"))
                            .locale(PublisherLocale.valueOf(rs.getString("locale")))
                            .authors(new ArrayList<>())
                            .build();
                    bookMap.put(bookId, book);
                }

                Author author = new Author(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getLong("author_id")
                );
                book.getAuthors().add(author);
            }
        }catch (SQLException e){
            log.error("Error while retrieving data");
            e.printStackTrace();
        }
        return new ArrayList<>(bookMap.values());
    }

    public static PreparedStatement createPreparedStatementFindByTitle(Connection conn, String title) throws SQLException {
        String sql = """
        SELECT
            b.id AS book_id,
            b.title,
            b.year,
            b.edition,
            b.publisher,
            b.locale,
            a.id AS author_id,
            a.first_name,
            a.last_name
        FROM book_store b
        LEFT JOIN book_author ba ON b.id = ba.book_id
        LEFT JOIN author_store a ON ba.author_id = a.id
        WHERE b.title LIKE ?;
    """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + title + "%");
        return ps;
    }



    public Optional<Book> findById(Long targetID){
        Book book = null;
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementFindById(conn, targetID);
            ResultSet rs = ps.executeQuery();
        ){
            while (rs.next()){
                if (book == null){
                    book = Book.BookBuilder
                            .builder()
                            .title(rs.getString("title"))
                            .year(rs.getInt("year"))
                            .edition(rs.getInt("edition"))
                            .publisher(rs.getString("publisher"))
                            .locale(PublisherLocale.valueOf(rs.getString("locale")))
                            .authors(new ArrayList<>())
                            .id(rs.getLong("book_id"))
                            .build();
                }
                long authorId = rs.getLong("author_id");
                if (authorId != 0) {
                    Author author = new Author(
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            authorId
                    );
                    book.getAuthors().add(author);
                }
            }
            return Optional.ofNullable(book);
        }catch (SQLException e){
            log.error("Error while searching for this id");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static PreparedStatement createPreparedStatementFindById(Connection conn, Long id) throws SQLException{
        String sql = """
                SELECT
                    b.id AS book_id,
                    b.title,
                    b.year,
                    b.publisher,
                    b.edition,
                    b.locale,
                    a.id AS author_id,
                    a.first_name,
                    a.last_name
                FROM book_store b
                LEFT JOIN book_author ba ON b.id = ba.book_id
                LEFT JOIN author_store a ON ba.author_id = a.id
                WHERE b.id = ?
                """;
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, id);
        return ps;
    }

    public void deleteBookById(Long id){

       try(Connection conn = ConnectionFactory.getConnection()){
           conn.setAutoCommit(false);

           try (PreparedStatement ps1 = createPreparedStatementDeleteById(conn, id);
                PreparedStatement ps2 = createPreparedStatementDeleteBookByIdFromAuthor(conn, id)){

               ps2.executeUpdate();
               ps1.executeUpdate();
               conn.commit();
               log.info("Book and author link deleted successfully");
           }catch (SQLException e){
               conn.rollback();
               log.error("rollback on deletebookById");

           }


       }catch (SQLException e){
           log.error("Error while deleting register");
           e.printStackTrace();
       }

    }

    public static PreparedStatement createPreparedStatementDeleteById(Connection conn, Long id) throws SQLException{
        String sql = "DELETE FROM book_store WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, id);
        return ps;
    }

    public PreparedStatement createPreparedStatementDeleteBookByIdFromAuthor(Connection conn, long bookId) throws SQLException {
        String sql = "DELETE FROM book_author WHERE book_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, bookId);
        return ps;

    }

    public void update(Book updatedBook){
        try (Connection conn = ConnectionFactory.getConnection()){
            conn.setAutoCommit(false);
            try(PreparedStatement psDeleteAuthorBookLink = createPreparedStatementDeleteBookByIdFromAuthor(conn, updatedBook.getId());
                PreparedStatement psUpdateBookInfo = createPreparedStatementUpdate(conn, updatedBook)){
                psUpdateBookInfo.executeUpdate();
                psDeleteAuthorBookLink.executeUpdate();

                List<Long> authorIds = authorRepository.SaveAuthors(conn, updatedBook.getAuthors());
                bookAuthorRepository.saveBookAuthorLink(conn, updatedBook.getId(), authorIds);

                conn.commit();
            }catch (SQLException e){
                conn.rollback();
                throw new RuntimeException("Failed to update book with authors", e);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    /*public void update(Book book){
        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = createPreparedStatementUpdate(conn, book)){
            ps.execute();

        }catch (SQLException e){
            log.error("Erro ao atualizar o livro");
        }
    }*/


    public static PreparedStatement createPreparedStatementUpdate(Connection conn, Book book) throws SQLException{
        String sql = "UPDATE book_store SET title = ?, publisher = ?, locale = ?, year = ?, edition = ?, updated_at = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getPublisher());
        ps.setString(3, String.valueOf(book.getLocale()));
        ps.setInt(4, book.getYear());
        ps.setInt(5, book.getEdition());
        LocalDateTime now = LocalDateTime.now();
        ps.setTimestamp(6, Timestamp.valueOf(now));
        ps.setLong(7, book.getId());

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
        String sql = "INSERT INTO `book_store` (`title`, `publisher`, `locale`, `year`, `edition`) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);

        for(Book book: bookList_csv){
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getPublisher());
            ps.setString(3, String.valueOf(book.getLocale()));
            ps.setInt(4, book.getYear());
            ps.setInt(5, book.getEdition());
            ps.addBatch();
        }

        return ps;

    }




}
