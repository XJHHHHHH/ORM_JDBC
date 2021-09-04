package orm;

import connectionpool.ConnectionPool;
import orm.annotation.Delete;
import orm.annotation.Insert;
import orm.annotation.Select;
import orm.annotation.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SqlSession {
    private Handler handler = new Handler();

    //这个方法封装jdbc重复的代码
    //方案一
    public void update(String sql,Object...params){
        try {
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn = pool.getConnection();
            PreparedStatement pstat = conn.prepareStatement(sql);
            for(int i=0;i<params.length;i++){
                pstat.setObject(i+1,params[i]);
            }
            pstat.executeUpdate();
            pstat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //查询方法
    //查询一条记录
    public <T>T selectOne(String sql,RowMapper rm,Object...objs){
        return (T)this.selectList(sql,rm,objs).get(0);

//        Object obj = null;
//        try {
//            ConnectionPool pool = ConnectionPool.getInstance();
//            Connection conn = pool.getConnection();
//            PreparedStatement pstat = conn.prepareStatement(sql);
//            for(int i=0;i<objs.length;i++){
//                pstat.setObject(i+1,objs[i]);
//            }
//            ResultSet rs = pstat.executeQuery();
//            //以上的流程都是一样的，只有rs结果集中数据不一样，所以返回的对象不一样
//            //用到策略模式
//            if(rs.next()) {
//                obj = rm.mapperRow(rs);
//            }
//            rs.close();
//            pstat.close();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return (T)obj;
    }
    //查询多条记录，返回一个集合
    public <T>List<T> selectList(String sql,RowMapper rm,Object...objs){
        List<T> list = new ArrayList();
        try {
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn = pool.getConnection();
            PreparedStatement pstat = conn.prepareStatement(sql);
            for(int i=0;i<objs.length;i++){
                pstat.setObject(i+1,objs[i]);
            }
            ResultSet rs = pstat.executeQuery();
            //以上的流程都是一样的，只有rs结果集中数据不一样，所以返回的对象不一样
            //用到策略模式
            while (rs.next()) {
                T obj = (T)rm.mapperRow(rs);
                list.add(obj);
            }
            rs.close();
            pstat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //方案二
    //可以解析一条sql语句，我们规定的sql格式
    //查询
    public <T>T selectOne(String sql,Object obj,Class resultType){
        Object result = null;
        try {
            SQLAndKey sqlAndKey = handler.parseSQL(sql);
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection  conn = pool.getConnection();
            PreparedStatement pstat = conn.prepareStatement(sqlAndKey.getSQL());
            //进行sql语句中的？问号拼接
            handler.handleParameter(pstat,obj,sqlAndKey.getKeyList());
            ResultSet rs = pstat.executeQuery();
            //然后将rs结果集中数据，包装成相应的对象
            if(rs.next()){
                result = handler.handleResult(rs,resultType);
            }

            rs.close();
            pstat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (T)result;
    }
    public <T> List<T> selectList(String sql,Object obj,Class resultType){
        List list = new ArrayList<T>();
        try {
            SQLAndKey sqlAndKey = handler.parseSQL(sql);
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection  conn = pool.getConnection();
            PreparedStatement pstat = conn.prepareStatement(sqlAndKey.getSQL());
            //进行sql语句中的？问号拼接
            handler.handleParameter(pstat,obj,sqlAndKey.getKeyList());
            ResultSet rs = pstat.executeQuery();
            //然后将rs结果集中数据，包装成相应的对象
            while (rs.next()){
                list.add((T)handler.handleResult(rs,resultType));
            }
            rs.close();
            pstat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public void update(String sql,Object obj){
        try {
            SQLAndKey sqlAndKey = handler.parseSQL(sql);

            ConnectionPool pool = ConnectionPool.getInstance();
            Connection conn = pool.getConnection();
            PreparedStatement pstat = conn.prepareStatement(sqlAndKey.getSQL());
            if(obj!=null) {
                handler.handleParameter(pstat, obj, sqlAndKey.getKeyList());
            }
            pstat.executeUpdate();
            pstat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insert(String sql,Object obj){
        this.update(sql,obj);
    }
    public void delete(String sql,Object obj ){
        this.update(sql,obj);
    }


    public void update(String sql){
        this.update(sql,null);
    }
    public void insert(String sql){
        this.insert(sql,null);
    }
    public void delete(String sql){
        this.delete(sql,null);
    }
    public <T> T selectOne(String sql,Class resultType){
        return this.selectOne(sql,null,resultType);
    }
    public <T>List<T> selectList(String sql,Class resultType){
        return this.selectList(sql,null,resultType);
    }
    //=====================================================================================================

    //创建一个代理方法，代理dao层做事
    //返回 所代理的对象
    //参数 被代理的类（dao）
    public <T> T getMapper(Class clazz){
        //newProxyInstance创建代理类需要三个参数
        //1.被代理类的 类加载器（classLoader）
        //2.加载的类 class[] 通常就是一个类
        //3.代理所做的事情 InvocationHandler是个接口 需要具体实现
        ClassLoader classLoader = clazz.getClassLoader();
        Class[] classes = new Class[]{clazz};
        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //帮助被代理的dao，代用增删改查方法
                //三个参数 proxy 代理对象，method 被代理方法，args 被代理方法需要的参数

                //需要代理dao调用哪个方法，取决于注解
                //得到这个方法上的注解
                Annotation an = method.getAnnotations()[0];
                //获取这个注解的类型
                Class type = an.annotationType();
                 //要解析注解，得到里面的sql，
                //找寻注解里面的value方法
                Method valueMethod = type.getDeclaredMethod("value");
                //获取value方法中搬运过来的value值
                String sql = (String)valueMethod.invoke(an);
                //获取代理方法需要的参数
                Object param = args == null?null:args[0];
                //然后根据注解的类型type判断调用哪个对应的方法
                if(type== Insert.class){
                    //这匿名内部类，调用外部方法需要，SqlSession.this.方法
                    SqlSession.this.insert(sql,param);
                }else if(type== Delete.class){
                    SqlSession.this.delete(sql,param);
                }else if(type== Update.class){
                    SqlSession.this.update(sql,param);
                }else if (type== Select.class){
                    //因为查询方法有两个，一个查询单条记录，一个查询多条
                    //所以可以获取查询方法的返回值类型
                    Class methodReturnType = method.getReturnType();
                    if(methodReturnType==List.class){
                        //Class类无法操作泛型，其父类Type可以
                        Type returnType = method.getGenericReturnType();//获取返回值的具体类型，例如，java.util.List.
                        //将Type还原可以操作泛型的类，这个类可以
                        ParameterizedType realReturnType = (ParameterizedType) returnType;
                        //获取List结合中的泛型类
                        Type[] patternTypes = realReturnType.getActualTypeArguments();
                        //获取这个泛型数组中的第一个元素
                        Type patternType = patternTypes[0];
                        Class resultType = (Class) patternType;
                        return SqlSession.this.selectList(sql,param,resultType);
                    }else{
                        //selectOne方法所需要的 参数 resultType就是methodReturnType
                        return SqlSession.this.selectOne(sql,param,methodReturnType);
                    }
                }else {
                    System.out.println("没有这个注解！");
                }
                return null;
            }
        };
        return (T) Proxy.newProxyInstance(classLoader,classes,h);
    }
}



