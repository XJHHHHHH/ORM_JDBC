package cartest.dao;

import cartest.domain.Car;
import orm.RowMapper;
import orm.SqlSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CarDao {

    private SqlSession sqlSession = new SqlSession();
    //JDBC连接car数据库
    //新增一条记录
    public void insert(Car car){
//        String sql = "INSERT INTO CAR VALUES(?,?,?,?)";
//        sqlSession.update(sql,car.getCid(),car.getCname(),car.getColour(),car.getCprice());
        String sql = "INSERT INTO CAR VALUES(#{cid},#{cname},#{colour},#{cprice})";
        sqlSession.update(sql,car);
    }

    public void insert(Map map){
        String sql = "INSERT INTO CAR VALUES(#{cid},#{cname},#{colour},#{cprice})";
        sqlSession.update(sql,map);
    }
    //删除一条记录
    public void delete(Integer cid){
        String sql = "DELETE FROM CAR WHERE CID = #{cid}";
        sqlSession.update(sql,cid);
    }

    //修改一条数据
    public void update(Car car){
        String sql= "UPDATE CAR SET CNAME = #{cname},COLOUR = #{colour},CPRICE = #{cprice} WHERE CID = #{cid}";
        sqlSession.update(sql,car);
    }

    //查询所有的数据
//    public Car selectOne(Integer cid){
//        String sql = "SELECT * FROM CAR WHERE cid =?";
//        RowMapper rm = new RowMapper() {
//            @Override
//            public Object mapperRow(ResultSet rs) throws SQLException {
//                Car car = new Car();
//                car.setCid(rs.getInt("cid"));
//                car.setCname(rs.getString("cname"));
//                car.setColour(rs.getString("colour'"));
//                car.setCprice(rs.getInt("cprice"));
//                return car;
//            }
//        };
//        return sqlSession.selectOne(sql,rm,cid);
//    }

    public Car selectOne(int cid){
        String sql = "SELECT * FROM CAR WHERE CID = #{cid}";
        return sqlSession.selectOne(sql,cid,Car.class);
    }

    //根据ID查询一条数据
    public List<Car> selectList(){

        return  null;
    }
}
