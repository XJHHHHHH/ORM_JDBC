package cartest.newdao;

import cartest.domain.Car;
import orm.annotation.Delete;
import orm.annotation.Insert;
import orm.annotation.Select;
import orm.annotation.Update;
import java.util.List;

public interface CarDao {
    @Insert("INSERT INTO CAR VALUES(#{cid},#{cname},#{colour},#{cprice})")
    public void insert(Car car);

    @Delete("DELETE FROM CAR WHERE CID =#{cid}")
    public void delete(Integer cid);

    @Update("UPDATE CAR SET CNAME = #{cname},COLOUR = #{colour},CPRICE = #{cprice} WHERE CID = #{cid}")
    public void update(Integer cid);

    @Select("SELECT * FROM CAR WHERE CID = #{cid}")
    public Car selectOne(Integer cid);

    @Select("SELECT * FROM CAR")
    public List<Car> selectList();

}
