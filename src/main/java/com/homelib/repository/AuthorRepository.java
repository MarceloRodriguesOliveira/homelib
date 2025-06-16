package com.homelib.repository;

import com.homelib.entities.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {

    public List<Long> SaveAuthors(Connection conn, List<Author> authors) throws SQLException {
        if(authors.size() == 1 ){
            return List.of(saveSingleAuthor(conn,authors.getFirst()));
        }

        return saveAuthorsBatch(conn,authors);
    }

    public List<Long> saveAuthorsBatch(Connection conn, List<Author> authors) throws SQLException {
        if(authors == null || authors.isEmpty()) return List.of();
        List<Long> generatedIds = new ArrayList<>();
        try(PreparedStatement ps = createPreparedStatementSaveAuthorBatch(conn)){
            for (Author author: authors){
                ps.setString(1, author.getFirstName());
                ps.setString(2, author.getLastName());
                ps.addBatch();
            }
            ps.executeBatch();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    generatedIds.add(rs.getLong(1));
                }
            }
        }
        return generatedIds;
    }

    public Long saveSingleAuthor(Connection conn, Author author) throws SQLException {
        try(PreparedStatement ps = createPreparedStatementSaveSingleAuthor(conn, author)){
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to insert single author");
    }


    private static PreparedStatement createPreparedStatementSaveSingleAuthor(Connection conn, Author author) throws SQLException {
        String sql = "INSERT INTO author_store (firstname, lastname) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, author.getFirstName());
        ps.setString(2, author.getLastName());
        return ps;
    }

    private static PreparedStatement createPreparedStatementSaveAuthorBatch(Connection conn) throws SQLException{
        String sql = "INSERT INTO author_store (first_name, last_name) VALUES (?, ?)";
        return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

}
