package connectionpool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    //创建一个map集合存储从配置文件中读过来的的数据
    private static Map<String,String> configMap;
    static{
        properties = new Properties();
        configMap = new HashMap<>();
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration.properties");
            //properties类加载一个输入流
            properties.load(inputStream);
            //properties文件的结构是一个map集合的结构
            //获取properties中全部的key
            Enumeration en = properties.propertyNames();//相当于Set = map.keySet（）；
            while(en.hasMoreElements()){//相当于Set--Iteration迭代器          it.hasNext()
                String key =(String) en.nextElement();//it.next()
                String value = properties.getProperty(key);//map.get(key)
                configMap.put(key,value);
            }
            //关闭InputStream流
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //创建一个方法获取configMap中的value值
    public static String getPropertiesValue(String key){
        return configMap.get(key);
    }


}
