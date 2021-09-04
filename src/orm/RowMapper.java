package orm;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {
    //这是一个策略，根据自己的方式，返回所查询的对象类型
    //用户自己定义策略
    public Object mapperRow(ResultSet rs) throws SQLException;
}
