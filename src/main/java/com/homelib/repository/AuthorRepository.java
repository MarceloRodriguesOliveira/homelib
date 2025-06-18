package com.homelib.repository;

import com.homelib.connection.ConnectionFactory;
import com.homelib.entities.Author;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
public class AuthorRepository {


    public List<Long> SaveAuthors(Connection conn, List<Author> authors) throws SQLException {
        if(authors.size() == 1 ){
            return List.of(saveSingleAuthor(conn,authors.getFirst()));
        }

        return saveAuthorsBatch(conn,authors);
    }

    public List<Long> saveAuthorsBatch(Connection conn, List<Author> authors) throws SQLException {
        try(PreparedStatement ps =createPreparedStatementSaveAuthorBatch(conn)){
            for (Author author : authors){
                ps.setString(1, author.getFirstName());
                ps.setString(2, author.getLastName());
                ps.addBatch();
            }
            ps.executeBatch();
        }
        List<Long> ids = new ArrayList<>();
        for (Author author : authors){
            findAuthorByName(conn, author).ifPresent(ids::add);
        }
        return ids;
    }

    public Long saveSingleAuthor(Connection conn, Author author) throws SQLException {
        Optional<Long> existingId = findAuthorByName(conn, author);
        if(existingId.isPresent()){
            log.info("Author already exists on database. Associating book...");
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
            log.error("Could not retrieve info on author", e);
        }
        return Optional.empty();
    }

    private static PreparedStatement createPreparedStatementSaveSingleAuthor(Connection conn, Author author) throws SQLException {
        String sql = "INSERT OR IGNORE INTO author_store (first_name, last_name) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, author.getFirstName());
        ps.setString(2, author.getLastName());
        return ps;
    }

    private static PreparedStatement createPreparedStatementSaveAuthorBatch(Connection conn) throws SQLException{
        String sql = "INSERT OR IGNORE INTO author_store (first_name, last_name) VALUES (?, ?)";
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
