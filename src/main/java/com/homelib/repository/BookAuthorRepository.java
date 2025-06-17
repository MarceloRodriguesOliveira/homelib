package com.homelib.repository;

import com.homelib.connection.ConnectionFactory;
import com.homelib.entities.Author;
import com.homelib.entities.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BookAuthorRepository {

    public void saveBookAuthorLink(Book book, List<Long> authorIds) throws SQLException {
        if(authorIds == null || authorIds.isEmpty()) return;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementBookAuthorLink(conn)){
            for (Long authorId: authorIds ){
                ps.setLong(1, book.getId());
                ps.setLong(2, authorId );
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private PreparedStatement createPreparedStatementBookAuthorLink(Connection conn) throws SQLException {
        String sql = "INSERT INTO book_authors (book_id, author_id) VALUES (?, ?)";
        return conn.prepareStatement(sql);
    }
}
