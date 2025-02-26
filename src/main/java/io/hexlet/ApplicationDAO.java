package io.hexlet;


import java.sql.DriverManager;
import java.sql.SQLException;

public class ApplicationDAO {
    public static void main(String[] args) throws SQLException {
        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var dao = new UserDAO(conn);
            var user = new User("Maria", "+7937123");
            user.getId(); // null
            dao.save(user);
            System.out.println(user.getId() + " " + user.getName()); // Здесь уже выводится какой-то id

            var user1 = new User("Tom", "+7927456");
            user1.getId(); // null
            dao.save(user1);
            System.out.println(user1.getId() + " " + user1.getName()); // Здесь уже выводится какой-то id

            var user2 = new User("Gek", "+7917789");
            user2.getId(); // null
            dao.save(user2);
            System.out.println(user2.getId() + " " + user2.getName()); // Здесь уже выводится какой-то id

            // Delete user id = 2
            dao.delete(2L);

            // Print SELECT *
            dao.show();

            // Возвращается Optional<User>
            /*
            Метод find() возвращает Optional, а не просто найденный объект.
            Это помогает избежать возврата null в тех случаях, когда запись не найдена.
            Возврат null требовал бы постоянно помнить о проверке на существование объекта
            и выполнять ее
             */
            var user8 = dao.find(user.getId()).get();
            if (user8.getId() == user.getId()) { //true
                System.out.println("true");
            }

            var t = dao.getEntities();
            int i = 0;
        }

    }
}
