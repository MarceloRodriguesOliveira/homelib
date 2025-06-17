package com.homelib.repository;

import com.homelib.connection.ConnectionFactory;
import com.homelib.entities.Author;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepository {

    private static final Logger log = LogManager.getLogger(AuthorRepository.class);

    public List<Long> SaveAuthors(Connection conn, List<Author> authors) throws SQLException {
        if(authors.size() == 1 ){
            return List.of(saveSingleAuthor(conn,authors.getFirst()));
        }

        return saveAuthorsBatch(conn,authors);
    }

    public List<Long> saveAuthorsBatch(Connection conn, List<Author> authors) throws SQLException {
        if(authors == null || authors.isEmpty()) return List.of();
        List<Long> generatedIds = new ArrayList<>();
        List<Author> authorsToInsert = new ArrayList<>();

        for (Author author:authors){
            Optional<Long> existingId = findAuthorByName(conn, author);
            if(existingId.isPresent()){
                generatedIds.add(existingId.get());
            }else authorsToInsert.add(author);
        }

        try(PreparedStatement ps = createPreparedStatementSaveAuthorBatch(conn)){
            for (Author author: authorsToInsert){
                ps.setString(1, author.getFirstName());
                ps.setString(2, author.getLastName());
                ps.addBatch();
            }
            ps.executeBatch();

            try(ResultSet rs = ps.getGeneratedKeys()){
                while (rs.next()){
                    generatedIds.add(rs.getLong(1));
                }
            }
        }
        return generatedIds;
    }

    public Long saveSingleAuthor(Connection conn, Author author) throws SQLException {
        Optional<Long> existingId = findAuthorByName(conn, author);
        if(existingId.isPresent()){
            log.info("Author already exists on database. Aborting insert...");
            return existingId.get();
        }
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


    public Optional<Long> findAuthorByName(Connection conn, Author author){
        try(PreparedStatement ps = createPreparedStatementFindAuthorByName(conn, author);
            ResultSet rs = ps.executeQuery()){
            if(!rs.next()) return Optional.empty();
            return Optional.of(rs.getLong(1));
        }catch (SQLException e){
            log.error("Could not retrieve info on author");
        }
        return Optional.empty();
    }

    private static PreparedStatement createPreparedStatementSaveSingleAuthor(Connection conn, Author author) throws SQLException {
        String sql = "INSERT INTO author_store (first_name, last_name) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, author.getFirstName());
        ps.setString(2, author.getLastName());
        return ps;
    }

    private static PreparedStatement createPreparedStatementSaveAuthorBatch(Connection conn) throws SQLException{
        String sql = "INSERT INTO author_store (first_name, last_name) VALUES (?, ?)";
        return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    private static PreparedStatement createPreparedStatementFindAuthorByName(Connection conn, Author author) throws SQLException{
        String sql = "SELECT id, first_name, last_name FROM author_store WHERE first_name = ? AND last_name = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, author.getFirstName());
        ps.setString(2, author.getLastName());
        return ps;
    }

}
