import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestSqlite {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // 连接到SQLite数据库（如果数据库不存在，会自动创建）
            connection = JDBC.createConnection("jdbc:sqlite:/Users/codefan/projects/RunData/temp/example2.db", new Properties());

            // 创建一个表
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, email TEXT)");

            // 插入数据
            statement.executeUpdate("INSERT INTO users (name, email) VALUES ('Alice', 'alice@example.com')");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS units (id INTEGER PRIMARY KEY, name TEXT, email TEXT)");

            // 插入数据
            statement.executeUpdate("INSERT INTO units (name, email) VALUES ('Alice', 'alice@example.com')");

            // 查询数据
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
