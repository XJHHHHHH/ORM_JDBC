package orm;

import java.util.ArrayList;
import java.util.List;

public class SQLAndKey {
    //将解析后的带问号的sql语句和解析出来的？对应的key包装起来
    private StringBuilder sql = new StringBuilder();
    private List<String> keyList = new ArrayList<>();

    public SQLAndKey(StringBuilder sql,List<String> keyList){
        this.sql = sql;
        this.keyList = keyList;
    }

    public String getSQL(){
        return this.sql.toString();
    }
    public List<String> getKeyList(){
        return this.keyList;
    }

}
