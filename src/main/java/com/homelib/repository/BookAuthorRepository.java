package com.homelib.repository;

import com.homelib.connection.ConnectionFactory;
import com.homelib.entities.Author;
import com.homelib.entities.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookAuthorRepository {

    public void saveBookAuthorLink(Connection conn, Long bookId, List<Long> authorIds) throws SQLException {
        if(authorIds == null || authorIds.isEmpty()) return;

        try (PreparedStatement ps = createPreparedStatementBookAuthorLink(conn)){
            for (Long authorId: authorIds ){
                ps.setLong(1, bookId);
                ps.setLong(2, authorId );
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public List<Long> findAuthorByBookId(Connection conn, long bookId){
        List<Long> bookAuthorIds = new ArrayList<>();
        try (PreparedStatement ps = createPreparedStatementFindAuthorByBookId(conn, bookId);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()){
                bookAuthorIds.add(rs.getLong("author_id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return bookAuthorIds;
    }

    private PreparedStatement createPreparedStatementBookAuthorLink(Connection conn) throws SQLException {
        String sql = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)";
        return conn.prepareStatement(sql);
    }

    private PreparedStatement createPreparedStatementFindAuthorByBookId(Connection conn, long bookId) throws SQLException {
        String sql = "SELECT author_id FROM book_author WHERE book_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, bookId);
        return ps;
    }

}
