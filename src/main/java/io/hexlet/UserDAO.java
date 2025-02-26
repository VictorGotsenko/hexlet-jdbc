package io.hexlet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection conn) {
        connection = conn;
    }

    public void save(User user) throws SQLException {
        // Если пользователь новый, выполняем вставку
        // Иначе обновляем
        if (user.getId() == null) {
            var sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();
                var generatedKeys = preparedStatement.getGeneratedKeys();
                // Если идентификатор сгенерирован, извлекаем его и добавляем в сохраненный объект
                if (generatedKeys.next()) {
                    // Обязательно устанавливаем id в сохраненный объект
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving an entity");
                }
            }
        } else {
            // Здесь код обновления существующей записи
        }
    }

    // Возвращается Optional<User>
    // Это упрощает обработку ситуаций, когда в базе ничего не найдено
    public Optional<User> find(Long id) throws SQLException {
        var sql = "SELECT * FROM users WHERE id = ?";
        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var username = resultSet.getString("username");
                var phone = resultSet.getString("phone");
                var user = new User(username, phone);
                user.setId(id);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }

    // Delete record by id
    public void delete(Long id) throws SQLException {
         var sql = "DELETE FROM users WHERE id = ?";
          try (var stmt = connection.prepareStatement(sql)) {
              stmt.setLong(1, id);
              stmt.executeUpdate();
              System.out.println("Delete record with id = " + id);
          }
    }

    public void show() throws SQLException {
        var sql = "SELECT * FROM users";
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);
            System.out.println("Print table Users:");
            while (resultSet.next()) {
                System.out.print(resultSet.getString("username") + "  ");
                System.out.println(resultSet.getString("phone"));
            }
            System.out.println();
        }

    }

    public List<User> getEntities() throws SQLException {
        List<User> result = new ArrayList();
        long id;
        String username;
        String phone;
        var sql = "SELECT * FROM users";
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);
           // if (resultSet.next()) {
            while (resultSet.next()) {
                id = resultSet.getLong("id");
                username = resultSet.getString("username");
                phone = resultSet.getString("phone");
                result.add(new User(username, phone, id));
            }
                return result;
            //}
        }
       // return result;
    }

}