package BMS.managment.reservationManager;

public class InvalidActionException extends Exception {
    InvalidActionException(){
        super("Action Not Supported");
    }
}
