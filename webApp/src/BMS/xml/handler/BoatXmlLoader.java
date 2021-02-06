package BMS.xml.handler;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.BoatInfoFieldType;
import BMS.boutHouse.form.field.type.Informable;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.schema.generated.boats.Boat;
import BMS.xml.schema.generated.boats.BoatType;
import BMS.xml.schema.generated.boats.Boats;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoatXmlLoader extends BoutHouseXMLLoader {
    private static final String JAXB_XML_PACKAGE_NAME_BOATS = getMasterJaxbXmlPackageName() + ".boats";
    @Override
    protected String getJaxbXmlPackageName() {
        return JAXB_XML_PACKAGE_NAME_BOATS;
    }

    @Override
    public ArrayList<ArrayList<InfoField>> fromXMLFileToListOfInstancesArgs(Path path) throws JAXBException, FileNotFoundException, ExtensionException {
        InputStream inputStream = getInputStreamFromPath(path);
        Boats boats = (Boats) deserializeFrom(inputStream);

        return fromBoatsJaxbToArrayBoatInstanceArgs(boats);
    }

    private ArrayList<ArrayList<InfoField>> fromBoatsJaxbToArrayBoatInstanceArgs(Boats boats) {
        List<Boat> listOfBoats = boats.getBoat();

        return fromListOfBoatsJaxbToArrayOfBoatInstanceArgs(listOfBoats);
    }

    private ArrayList<ArrayList<InfoField>> fromListOfBoatsJaxbToArrayOfBoatInstanceArgs(List<Boat> listOfBoats) {
        ArrayList<ArrayList<InfoField>> allBoats = new ArrayList<>();

        for(Boat b:listOfBoats){
            allBoats.add(fromBoatJaxbToArrayBoatInstanceArgsInstanceArgs(b));
        }

        return allBoats;
    }

    private ArrayList<InfoField> fromBoatJaxbToArrayBoatInstanceArgsInstanceArgs(Boat boat) {
        Map<Informable, String> stringValues = new HashMap<>();
        String boatString = createBoatTypeFromJaxb(boat.getType(), boat.isCostal(), boat.isWide());

        stringValues.put(BoatInfoFieldType.SERIAL_NUMBER, boat.getId());
        stringValues.put(BoatInfoFieldType.BOAT_NAME, boat.getName());
        stringValues.put(BoatInfoFieldType.BOAT_TYPE, boatString);
        stringValues.put(BoatInfoFieldType.BOAT_PRIVATE, fromJaxbBooleanTOBooleanInput(boat.isPrivate()));
        stringValues.put(BoatInfoFieldType.BOAT_DISABLED, fromJaxbBooleanTOBooleanInput(boat.isOutOfOrder()));

        return fromListOfStringJaxbInstanceToArrayOfBoutHouseInstanceArgs(stringValues);
    }

    private String fromJaxbBooleanTOBooleanInput(Boolean booleanJaxb) {
        String booleanInput;

        if (booleanJaxb != null) {
            if (booleanJaxb) {
                booleanInput = "Yes";
            } else {
                booleanInput = "No";
            }
        }
        else{
            booleanInput = null;
        }

        return booleanInput;
    }

    private String createBoatTypeFromJaxb(BoatType boatType, Boolean coastal, Boolean wide) {
        StringBuilder boatTypeBuilder = new StringBuilder();

        if(boatType == BoatType.SINGLE){
            boatTypeBuilder.append("1X");
        }
        else if (boatType == BoatType.DOUBLE){
            boatTypeBuilder.append("2X");
        }
        else if (boatType == BoatType.COXED_PAIR){
            boatTypeBuilder.append("2+");
        }
        else if (boatType == BoatType.COXED_DOUBLE){
            boatTypeBuilder.append("2X+");
        }
        else if (boatType == BoatType.PAIR){
            boatTypeBuilder.append("2-");
        }
        else if (boatType == BoatType.COXED_QUAD){
            boatTypeBuilder.append("4X+");
        }
        else if (boatType == BoatType.COXED_FOUR){
            boatTypeBuilder.append("4+");
        }
        else if (boatType == BoatType.QUAD){
            boatTypeBuilder.append("4X");
        }
        else if (boatType == BoatType.FOUR){
            boatTypeBuilder.append("4-");
        }
        else if (boatType == BoatType.OCTUPLE){
            boatTypeBuilder.append("8X+");
        }
        else if (boatType == BoatType.EIGHT) {
            boatTypeBuilder.append("8+");
        }

        if (wide != null) {
            if (wide) {
                boatTypeBuilder.append(" Wide");
            } else if (!wide) {
                boatTypeBuilder.append(" Narrow");
            }
        }

        if(coastal != null) {
            if (coastal) {
                boatTypeBuilder.append(" Coastal");
            } else if (!coastal) {
                boatTypeBuilder.append(" FlatWater");
            }
        }

        return boatTypeBuilder.toString();
    }
}
