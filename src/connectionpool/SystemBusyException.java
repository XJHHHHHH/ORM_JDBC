package connectionpool;

public class SystemBusyException extends  RuntimeException {
    public SystemBusyException(){}
    public SystemBusyException(String message){
        super(message);
    }
}
