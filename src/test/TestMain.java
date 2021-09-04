package test;

import connectionpool.ConnectionPool;
import connectionpool.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestMain {

    public static void main(String[] args){
//        TestThread t1 = new TestThread();
//        TestThread t2 = new TestThread();
//        TestThread t3 = new TestThread();
//        TestThread t4 = new TestThread();
//        TestThread t5 = new TestThread();
//        TestThread t6 = new TestThread();
//        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
//        t5.start();
//        t6.start();


        //Long l1 = System.currentTimeMillis();
        ConnectionPool connectionPool =ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        //Long l2 = System.currentTimeMillis();
        try {
            PreparedStatement pstat = connection.prepareStatement("SELECT * FROM atm");
            ResultSet rs = pstat.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("aname"));
                System.out.println(rs.getString("apassword"));
            }
            rs.close();
            pstat.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        Long l3 = System.currentTimeMillis();
//        System.out.println(l2-l1);
//        System.out.println(l3-l2);
    }
}
