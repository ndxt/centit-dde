import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestSqlite {
    public static void main(String[] args) {
        try (Connection connection = JDBC.createConnection("jdbc:sqlite:/Users/codefan/projects/RunData/temp/example2.db", new Properties())){

            // 创建一个表
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS hadwn_tempId_tab (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT)");

            // 插入数据
            statement.executeUpdate("INSERT INTO hadwn_tempId_tab (name, email) VALUES ('Alice', 'alice@example.com')");
            statement.executeUpdate("INSERT INTO hadwn_tempId_tab (name, email) VALUES ('codefan', 'alice@example.com')");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS units (id INTEGER PRIMARY KEY, name TEXT, email TEXT)");

            // 插入数据
            statement.executeUpdate("INSERT INTO units (name, email) VALUES ('Alice', 'alice@example.com')");

            // 查询数据
            ResultSet resultSet = statement.executeQuery("SELECT * FROM hadwn_tempId_tab");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
