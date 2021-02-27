package BMS.server;

import BMS.boutHouse.form.exceptions.WrongTypeException;

public enum BoutHouseDataType {
    BOATS("Storage"),
    MEMBERS("Member"),
    TIME_WINDOW("Time Window"),
    RESERVATION("Reservation");

    String nameOfManager;

    BoutHouseDataType(String managerName) {
        this.nameOfManager = managerName;
    }

    public String getNameOfManager() {
        return nameOfManager;
    }

    public static BoutHouseDataType createBoutHouseDataType(String managerName) throws WrongTypeException {
        BoutHouseDataType dataTypeToReturn;

        if (managerName.equals(BOATS.nameOfManager)){
            dataTypeToReturn = BoutHouseDataType.BOATS;
        }
        else if (managerName.equals(MEMBERS.nameOfManager)){
            dataTypeToReturn = BoutHouseDataType.MEMBERS;
        }
        else if (managerName.equals(RESERVATION.nameOfManager)){
            dataTypeToReturn = BoutHouseDataType.RESERVATION;
        }
        else if (managerName.equals(TIME_WINDOW.nameOfManager)){
            dataTypeToReturn = BoutHouseDataType.TIME_WINDOW;
        }
        else{
            throw new WrongTypeException(managerName);
        }

        return dataTypeToReturn;
    }

    @Override
    public String toString() {
        return "nameOfManager = " + nameOfManager;
    }


}
