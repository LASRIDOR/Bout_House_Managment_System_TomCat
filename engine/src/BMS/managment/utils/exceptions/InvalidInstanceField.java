package BMS.managment.utils.exceptions;

public class InvalidInstanceField extends Exception {
    public InvalidInstanceField(){
        super("One or More field are Invalid or Missing (from the must have fields)");
    }
}
