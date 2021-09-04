package connectionpool;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    //设计一个单例模式
    //让本类的构造方法为私有的，不能随便使用
    private ConnectionPool(){}
    //用一个私有的属性接受单例对象
    private volatile static ConnectionPool connectionPool;//加上volatile防止指令重排
    //获取单例对象的方法
    public static ConnectionPool getInstance(){
        if(connectionPool==null) {
            //考虑到线程安全问题
            synchronized (ConnectionPool.class) {
                //双重检测
                if (connectionPool == null) {
                    connectionPool = new ConnectionPool();//给connectionPool属性赋值的时候可能会产生指令重排的问题
                }
            }
        }
        return connectionPool;
    }

    private int minConnectionCount = new Integer(ConfigReader.getPropertiesValue("minConnectionCount"));
    private int waitTime = new Integer(ConfigReader.getPropertiesValue("waitTime"));
    //这是一个连接池 放着很多连接
    //首先有一个属性是一个池子容器
    private List<Connection> connections = new ArrayList<>();
    //使用一个块 在类被加载时，往连接池里存放 连接 MyConnection
    {
        for(int i = 1;i<=minConnectionCount;i++){
            connections.add(new MyConnection());
        }
    }

    //还要一个获取池子中连接的方法
    private Connection getMC(){
        Connection result = null;
        for(Connection conn : connections){
            MyConnection mc = (MyConnection)conn;
            if(!mc.isConnectionUsed()){//如果状态为false则说明未使用
                synchronized (this) {//锁this连接池对象 或者 锁 连接池集合connectionPool 或者锁 mc连接
                    if (!mc.isConnectionUsed()) {
                        mc.setConnectionUsed(true);
                        result = mc;
                        break;
                    }
                }
            }
        }
        return result;
    }

    //创建一个新的方法，也是获取MyConnection连接，但加了一个用户等待的机制
    public Connection getConnection(){
        Connection result = this.getMC();
        int count = 0;
        //判断result是否为空
        while(result==null && count<waitTime*10){
            //设置一个等待时间
            try {
                Thread.sleep(100);
                //最多可以等待50次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //如果等于空就在调用一次getMC方法
            result = this.getMC();
            count++;
        }
        //如果result还等于null，没拿到连接
        if(result==null){
            throw new SystemBusyException("系统繁忙！请稍后再试！");
        }
        return result;
    }

}
