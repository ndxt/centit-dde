package com.centit.test.datafile;

import java.sql.*;


public class TestJdbc {

    public static void main(String[] args) {
        Connection connt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connt = DriverManager.getConnection("jdbc:mysql://192.168.128.32:3306/dde?useUnicode=true&characterEncoding=utf-8", "framework", "framework");
            //String sql = "insert into d_task_log (Task_ID,task_type,run_type) values (4,2,1)";
            // "delete from d_task_log where id = ?"
            // update d_task_log set task _id ,task_type ... wehere id =
            String sql = "select * from d_task_log";
            ps = connt.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String otherMessage = rs.getString("other_message");
                System.out.printf("otherMessage:%s\n", otherMessage);
                String runBeginTime = rs.getString("run_begin_time");
                System.out.printf("runBeginTime:%s\n", runBeginTime);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("类找不到");
        } catch (SQLException e) {
            System.out.println("连接数据库出错");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.out.println("关闭resultSet失败");
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException e) {
                    System.out.println("关闭statement失败");
                } finally {
                    try {
                        if (connt != null) {
                            connt.close();
                        }
                    } catch (SQLException e) {
                        System.out.println("关闭connection失败");
                    }
                }
            }
        }
    }
}

