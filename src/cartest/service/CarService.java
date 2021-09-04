package cartest.service;

import cartest.domain.Car;
import cartest.newdao.CarDao;
import orm.SqlSession;

import java.util.List;
import java.util.Map;

public class CarService {

    private CarDao dao = new SqlSession().getMapper(CarDao.class);


    public void insert(Car car){
        dao.insert(car);
    }
    public Car selectOne(Integer cid){
        return dao.selectOne(cid);
    }

    public List<Car> selectList(){
        return  dao.selectList();
    }

    public void delete(Integer cid){
        dao.delete(cid);
    }
    public void update(Integer cid){
        dao.update(cid);
    }



//    private CarDao dao = new CarDao();
//
//    public void insert(Car car){
//        dao.insert(car);
//    }
//    public void insert(Map map){
//        dao.insert(map);
//    }
//    public void delete(Integer cid){
//        dao.delete(cid);
//    }
//    public void update(Car car){
//        dao.update(car);
//    }
//
//    public Car selectOne(Integer cid){
//        return dao.selectOne(cid);
//    }
//    public List<Car> selectList(){
//        return null;
//    }

}
