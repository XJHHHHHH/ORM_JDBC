package cartest.domain;

public class Car {
    private Integer cid;
    private String cname;
    private String colour;
    private Integer cprice;


    public Car(){}
    public Car(int cid,String cname,String colour,int cprice){
        this.cid= cid;
        this.cname = cname;
        this.colour = colour;
        this.cprice = cprice;
    }

    //重写toString方法
    public String toString() {
        StringBuilder sb= new StringBuilder();
        sb.append("Car");
        sb.append("[");
        sb.append(this.cid);
        sb.append(",");
        sb.append(this.cname);
        sb.append(",");
        sb.append(this.colour);
        sb.append(",");
        sb.append(this.cprice);
        sb.append("]");
        return new String(sb);
    }

    public void setCid(int cid){
        this.cid = cid;
    }
    public int getCid(){
        return this.cid;
    }
    public void setCname(String cname){
        this.cname = cname;
    }
    public String getCname(){
        return this.cname;
    }
    public void setColour(String colour){
        this.colour = colour;
    }
    public String getColour(){
        return this.colour;
    }
    public void setCprice(int cprice){
        this.cprice = cprice;
    }
    public int getCprice(){
        return this.cprice;
    }
}
