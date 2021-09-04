package orm.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//放在方法上面使用
@Target(ElementType.METHOD)
//生命周期作用域，内存中，运行时，
@Retention(RetentionPolicy.RUNTIME)
public @interface Insert {
    //注解是一个搬运工，搬运sql语句给别人解析
    String value();
}
