package com.techcourse.dao;

import com.interface21.jdbc.core.ObjectMaker;
import com.techcourse.domain.User;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMaker implements ObjectMaker<User> {

    @Override
    public User make(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4));
    }
}
