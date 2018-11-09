package com.tlabs.blockchain.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by admin on 2018/7/25.
 */
public class JDBCExample {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/miao?autoReconnect=true&amp;characterEncoding=utf8&amp;failOverReadOnly=false&amp;maxReconnects=10";

    static final String USER = "root";
    static final String PASS = "root";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("链接数据库");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("链接成功");
            System.out.println("开始创建表");
            stmt = conn.createStatement();
            String sql = "create table iii (id int not null,author_id int not null,title varchar(255), primary key (id))";
            int b = stmt.executeUpdate(sql);
            System.out.println("创建表结束===="+b);
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){

            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }
}
