package com.techcourse.dao;

import com.techcourse.domain.User;
import com.interface21.jdbc.core.JdbcTemplate;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final UserMaker maker;

    public UserDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = jdbcTemplate.getDataSource();
        this.maker = new UserMaker();
    }

    public void insert(final User user) {
        final var sql = "insert into users (account, password, email) values (?, ?, ?)";

        List<Object> paramList = new ArrayList<>();
        paramList.add(user.getAccount());
        paramList.add(user.getPassword());
        paramList.add(user.getEmail());

        jdbcTemplate.executeQuery(sql, paramList);
    }

    public void update(final User user) {
        final var sql = "update users set account = ?, password = ?, email = ? where id = ?";

        List<Object> paramList = new ArrayList<>();
        paramList.add(user.getAccount());
        paramList.add(user.getPassword());
        paramList.add(user.getEmail());
        paramList.add(user.getId());

        jdbcTemplate.executeQuery(sql, paramList);
    }

    public List<User> findAll() {
        final var sql = "select id, account, password, email from users";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            log.debug("query : {}", sql);

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignored) {}

            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ignored) {}

            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ignored) {}
        }

    }

    public User findById(final Long id) {
        final var sql = "select id, account, password, email from users where id = ?";

        List<Object> paramList = new ArrayList<>();
        paramList.add(id);

        return (User) jdbcTemplate.executeQueryForObject(sql, paramList, maker);
    }

    public User findByAccount(final String account) {
        final var sql = "select id, account, password, email from users where account = ?";

        List<Object> paramList = new ArrayList<>();
        paramList.add(account);

        return (User) jdbcTemplate.executeQueryForObject(sql, paramList, maker);
    }
}
