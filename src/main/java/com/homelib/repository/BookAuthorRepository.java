package com.homelib.repository;

import com.homelib.connection.ConnectionFactory;
import com.homelib.entities.Author;
import com.homelib.entities.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    private PreparedStatement createPreparedStatementBookAuthorLink(Connection conn) throws SQLException {
        String sql = "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)";
        return conn.prepareStatement(sql);
    }

}
