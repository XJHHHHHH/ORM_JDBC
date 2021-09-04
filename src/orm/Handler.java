package orm;

import java.lang.module.ResolvedModule;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler {
    //这是一个小弟，专门解析sql语句
    //返回我们的SQLAndKey对象
    SQLAndKey parseSQL(String sql){
        StringBuilder newSql = new StringBuilder();
        List<String> keyList = new ArrayList<>();
        //按我们规则定义一条sql语句
        //例如 insert into car values(#{cid},#{cname},#{colour},#{cprice})
        while (true){
            int left = sql.indexOf("#{");
            int right = sql.indexOf("}");
            if(left!=-1&&right!=-1&&left<right){
                newSql.append(sql.substring(0,left));
                newSql.append("?");
                keyList.add(sql.substring(left+2,right));
            }
            else {
                newSql.append(sql);
                break;
            }
            sql = sql.substring(right+1);
        }
        return new SQLAndKey(newSql,keyList);
    }

    //在设计两个方法，用来对map和domain类型的参数，pstat拼接
    private void setMap(PreparedStatement pstat, Object obj, List<String> keyList) throws SQLException {
        Map map = (Map)obj;
        for(int i=0;i<keyList.size();i++){
            pstat.setObject(i+1,map.get(keyList.get(i)));
        }
    }

    private void setDomain(PreparedStatement pstat,Object obj,List<String> keyList) {
        try {
            Class clazz = obj.getClass();
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                pstat.setObject(i + 1, field.get(obj));
            }
        }catch(Exception e){

        }
    }

    //这个方法负责将sql中问号的值传给pstat
    void handleParameter(PreparedStatement pstat,Object obj,List<String> keyList) throws SQLException {
        if (obj!=null) {
            Class clazz = obj.getClass();
            if (clazz == int.class || clazz == Integer.class) {
                pstat.setInt(1, (Integer) obj);
            } else if (clazz == float.class || clazz == Float.class) {
                pstat.setFloat(1, (Float) obj);
            } else if (clazz == double.class || clazz == Double.class) {
                pstat.setDouble(1, (Double) obj);
            } else if (clazz == String.class) {
                pstat.setString(1, (String) obj);
            } else if (clazz.isArray()) {

            } else {
                if (obj instanceof Map) {
                    this.setMap(pstat, obj, keyList);
                } else {
                    this.setDomain(pstat, obj, keyList);
                }
            }
        }
    }

//===============================================================================
    private Map getMap(ResultSet rs) throws SQLException {
        Map map = new HashMap<String,Object>();
         ResultSetMetaData resultSetMetaData = rs.getMetaData();
         for(int i =1;i<=resultSetMetaData.getColumnCount();i++){
             String columnName = resultSetMetaData.getColumnName(1);
             Object obj = rs.getObject(columnName);
             map.put(columnName,obj);
         }
         return map;
    }
    private Object getDomain(ResultSet rs,Class resultType){
        Object obj = null;
        try {
            obj = resultType.newInstance();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for(int i=1;i<=resultSetMetaData.getColumnCount();i++){
                String columnName = resultSetMetaData.getColumnName(i);
                //通过反射找到对应名字的属性
                Field field = resultType.getDeclaredField(columnName);
                //操作私有属性
                field.setAccessible(true);
                //给属性赋值
                field.set(obj,rs.getObject(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return  obj;
    }

    //这个方法将查询到的结果集中数据，包装成相应对象
    Object handleResult(ResultSet rs,Class resultType) throws SQLException {
        Object result = null;
        if(resultType == int.class||resultType ==Integer.class){
            result = rs.getInt(1);
        }else if(resultType == float.class||resultType==Float.class){
            result= rs.getFloat(1);
        }else if(resultType ==double.class||resultType==Double.class){
            result = rs.getDouble(1);
        }else if(resultType==String.class){
            result = rs.getString(1);
        }else {
            if(resultType == Map.class){
                result = this.getMap(rs);
            }else {
                result = this.getDomain(rs,resultType);
            }
        }
        return result;
    }
}
