package BMS.managment.utils.exceptions;

public class OnlyManagerAccessException extends Exception {
    public OnlyManagerAccessException(){
        super("only manager Can Access those kind of action");
    }
}
