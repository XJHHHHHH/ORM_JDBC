package test;

import cartest.dao.CarDao;
import cartest.domain.Car;
import cartest.service.CarService;

public class TestCar1 {
    public static void main(String [] args){
        CarService cs = new CarService();
        //cs.delete(7);
        //cs.insert(new Car(7,"法拉利","红色",5000000));
        //Car car = cs.selectOne(2);
        System.out.println(cs.selectList());
        //System.out.println(car);


        //=============================================================
        //Car car = cs.selectOne(1);
        //System.out.println(car);
        //cs.insert(new Car(8,"迈凯伦","紫色",3000000));
        //cs.delete(7);
        //cs.update(new Car(7,"特斯拉","蓝色",350000));
    }
}
