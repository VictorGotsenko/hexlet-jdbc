package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws SQLException {
        // Соединение с базой данных нужно отслеживать воизбежании возникновения ошибок
        // Создаем соединение с базой в памяти
        // База создается прямо во время выполнения этой строчки
        // Здесь mem означает, что подключение происходит к базе данных в памяти,
        // а hexlet_test — это имя базы данных
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql2)) {
                preparedStatement.setString(1, "Tommy");
                preparedStatement.setString(2, "+792145");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Maria");
                preparedStatement.setString(2, "+792578");
                preparedStatement.executeUpdate();
            }

            var sql3 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql3, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Sarah");
                preparedStatement.setString(2, "+792791");
                preparedStatement.executeUpdate();
                // Если ключ составной, значений может быть несколько
                // В нашем случае, ключ всего один
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    System.out.println("added Record number = " + generatedKeys.getLong(1) + "\n");
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }


            var sql5 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql5);
                System.out.println("Print table Users:");
                while (resultSet.next()) {
                    System.out.print(resultSet.getString("username") + "  ");
                    System.out.println(resultSet.getString("phone"));
                }
                System.out.println();
            }


            var sql4 = "DELETE FROM users WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(sql4)) {
                String userName = "Maria";
                preparedStatement.setString(1, userName);
                preparedStatement.executeUpdate();
                System.out.println("Delete user " + userName + "\n");
            }

            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql5);
                System.out.println("Print table Users:");
                while (resultSet.next()) {
                    System.out.print(resultSet.getString("username") + "  ");
                    System.out.println(resultSet.getString("phone"));
                }
            }

        }
    }
}