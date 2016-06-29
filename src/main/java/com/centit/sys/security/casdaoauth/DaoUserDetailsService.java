package com.centit.sys.security.casdaoauth;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoUserDetailsService implements
        UserDetailsService {
    private BasicDataSource dataSource;

    public void setDataSource(BasicDataSource ds) {
        dataSource = ds;
    }

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        FDaoUserDetails user = null;
        try {
            Connection conn = dataSource.getConnection();
            String sSql = " select USERCODE,USERPIN,ISVALID,LOGINNAME,USERNAME " +
                    "from f_userinfo where loginname = ? or usercode = ? ";
            PreparedStatement pStmt = conn.prepareStatement(sSql);
            pStmt.setString(1, username);
            pStmt.setString(2, username);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                user = new FDaoUserDetails(rs.getString("USERCODE"),
                        rs.getString("USERPIN"),
                        rs.getString("ISVALID"),
                        rs.getString("LOGINNAME"),
                        rs.getString("USERNAME"));
                //log.debug("我的数据库验证方案： " + rs.getString("USERCODE"));
            }
            rs.close();
            pStmt.close();
            //conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

}
