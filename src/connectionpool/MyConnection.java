package connectionpool;

import java.sql.*;

public class MyConnection extends AdapterConnection {//继承connection适配器
    private static String className = ConfigReader.getPropertiesValue("className");
    private static String url = ConfigReader.getPropertiesValue("url");
    private static String name = ConfigReader.getPropertiesValue("name");
    private static String password = ConfigReader.getPropertiesValue("password");

    //创建一个自己连接的对象
    //有连接使用状态 连接 属性
    private Boolean connectionUsed = false;//false代表没有被使用初始状态都是false
    private Connection connection;
    //创建一个静态块让mysql驱动之加载一次
    static {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //创建一个块 每次给连接赋值 Connection
    {
        try {
            connection = DriverManager.getConnection(url,name,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Boolean isConnectionUsed (){
        return connectionUsed;
    }
    public void setConnectionUsed(Boolean b){
        this.connectionUsed = b;
    }
    public Connection getConnection(){
        return connection;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement pstat = this.connection.prepareStatement(sql);
        return pstat;
    }
    @Override
    public void close() throws SQLException {
        this.connectionUsed = false;
    }
}
