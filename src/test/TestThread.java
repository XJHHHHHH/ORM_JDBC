package test;

import connectionpool.ConnectionPool;
import connectionpool.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestThread extends Thread {
    public void run() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        System.out.println(connection);
        try {
            Thread.sleep(2000);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
