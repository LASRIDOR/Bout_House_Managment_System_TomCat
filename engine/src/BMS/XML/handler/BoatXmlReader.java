package BMS.XML.handler;

import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.BoatInfoFieldType;
import BMS.boutHouse.form.field.type.Informable;
import BMS.server.BoutHouseDataType;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.schema.generated.boats.Boat;
import BMS.xml.schema.generated.boats.BoatType;
import BMS.xml.schema.generated.boats.Boats;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BoatXmlReader extends BoutHouseXMLReader {
    private static final String JAXB_XML_PACKAGE_NAME_BOATS = getMasterJaxbXmlPackageName() + ".boats";
    private static final String BOAT_DATA_BASE_LOCATION = getMasterDataBaseLocation() + "/" + BoutHouseDataType.BOATS.getNameOfManager() + " Records.xml";

    @Override
    public String getJaxbXmlPackageName() {
        return JAXB_XML_PACKAGE_NAME_BOATS;
    }

    @Override
    public String getDataBaseLocation(){return BOAT_DATA_BASE_LOCATION; }

    @Override
    public ArrayList<ArrayList<InfoField>> fromXMLFileToListOfInstancesArgs(Path path) throws JAXBException, FileNotFoundException, ExtensionException {
        InputStream inputStream = getInputStreamFromPath(path);
        Boats boats = (Boats) deserializeFrom(inputStream);
        ArrayList<ArrayList<InfoField>> boatsforms = fromBoatsJaxbToArrayBoatInstanceArgs(boats);

        return boatsforms;
    }

    private ArrayList<ArrayList<InfoField>> fromBoatsJaxbToArrayBoatInstanceArgs(Boats boats) {
        List<Boat> listOfBoats = boats.getBoat();

        return fromListOfBoatsJaxbToArrayOfBoatInstanceArgs(listOfBoats);
    }

    private ArrayList<ArrayList<InfoField>> fromListOfBoatsJaxbToArrayOfBoatInstanceArgs(List<Boat> listOfBoats) {
        ArrayList<ArrayList<InfoField>> boatsArgs = new ArrayList<>();

        for(Boat b:listOfBoats){
            boatsArgs.add(fromBoatJaxbToArrayBoatInstanceArgsInstanceArgs(b));
        }

        return boatsArgs;
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

    @Override
    public void updateInstances(InfoField idOfInstanceBeforeUpdate, BoutHouseInstance instance) throws FileNotFoundException, ExtensionException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(BOAT_DATA_BASE_LOCATION);
        InputStream inputStream = getInputStreamFromPath(pathOfDatabaseXml);
        Boats boats = (Boats) deserializeFrom(inputStream);

        List<Boat> membersList = removeBoatFromList(boats.getBoat(), idOfInstanceBeforeUpdate);
        Boat convertedBoat = fromBoutHouseBoatToBoatJaxbObject((BMS.bouthouse.storage.vessel.Boat) instance);
        membersList.add(convertedBoat);
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), boats);
    }

    private List<Boat> removeBoatFromList(List<Boat> boatList, InfoField idOfInstanceBeforeUpdate) {
        Boat boatToDelete = new Boat();

        for (Boat boat:boatList) {
            if (boat.getId().equals((String) idOfInstanceBeforeUpdate.getValue())){
                boatToDelete = boat;
            }
        }

        boatList.remove(boatToDelete);

        return boatList;
    }

    private Boat fromBoutHouseBoatToBoatJaxbObject(BMS.bouthouse.storage.vessel.Boat instance) {
        Boat boatToReturn = new Boat();
        Map<Informable, InfoField> allMemberFormField = instance.getAllFields();

        for (InfoField field : allMemberFormField.values()) {
            if(field.getType() == BoatInfoFieldType.BOAT_NAME){
                boatToReturn.setName((String) field.getValue());
            }
            else if (field.getType() == BoatInfoFieldType.BOAT_DISABLED){
                boatToReturn.setOutOfOrder((Boolean) field.getValue());
            }
            else if (field.getType() == BoatInfoFieldType.BOAT_PRIVATE){
                boatToReturn.setPrivate((Boolean) field.getValue());
            }
            else if(field.getType() == BoatInfoFieldType.SERIAL_NUMBER){
                boatToReturn.setId((String) field.getValue());
            }
            else if (field.getType() == BoatInfoFieldType.BOAT_TYPE){
                BMS.boutHouse.form.field.type.BoatType boatType = (BMS.boutHouse.form.field.type.BoatType) field.getValue();
                boatToReturn.setCostal(boatType.getRowingEnvironment().contains("Coastal"));
                boatToReturn.setWide(boatType.getWidthType().contains("Wide"));
                boatToReturn.setHasCoxswain(boatType.getBoatClassification().contains("+"));
                boatToReturn.setType(fromBoutHouseBoatTypeToJaxb(boatType.getBoatClassification()));
            }
        }

        return boatToReturn;
    }

    private BoatType fromBoutHouseBoatTypeToJaxb(String boatClassification) {
        BoatType jaxbBoatType;

        if(boatClassification.equals("1X")){
            jaxbBoatType = BoatType.SINGLE;
        }
        else if (boatClassification.equals("2X")){
            jaxbBoatType = BoatType.DOUBLE;
        }
        else if (boatClassification.equals("2+")){
            jaxbBoatType = BoatType.COXED_PAIR;
        }
        else if (boatClassification.equals("2X+")){
            jaxbBoatType = BoatType.COXED_DOUBLE;
        }
        else if (boatClassification.equals("2-")){
            jaxbBoatType = BoatType.PAIR;
        }
        else if (boatClassification.equals("4X+")){
            jaxbBoatType = BoatType.COXED_QUAD;
        }
        else if (boatClassification.equals("4+")){
            jaxbBoatType = BoatType.COXED_FOUR;
        }
        else if (boatClassification.equals("4X")){
            jaxbBoatType = BoatType.QUAD;
        }
        else if (boatClassification.equals("4-")){
            jaxbBoatType = BoatType.FOUR;
        }
        else if (boatClassification.equals("8X+")){
            jaxbBoatType = BoatType.OCTUPLE;
        }
        else if (boatClassification.equals("8+")) {
            jaxbBoatType = BoatType.EIGHT;
        }
        else {
            jaxbBoatType = null;
        }

        return jaxbBoatType;
    }

    @Override
    public void deleteInstance(InfoField idOfInstanceToDelete) throws FileNotFoundException, ExtensionException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(BOAT_DATA_BASE_LOCATION);
        InputStream inputStream = getInputStreamFromPath(pathOfDatabaseXml);

        Boats boats = (Boats) deserializeFrom(inputStream);
        List<Boat> boatsList = boats.getBoat();
        boatsList = removeMemberFromList(boatsList, idOfInstanceToDelete);

        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), boats);
    }

    private List<Boat> removeMemberFromList(List<Boat> boatsList, InfoField idOfInstanceToDelete) {
        Boat boatToDelete = new Boat();

        for (Boat boat:boatsList) {
            if (boat.getId().equals((String) idOfInstanceToDelete.getValue())){
                boatToDelete = boat;
            }
        }

        boatsList.remove(boatToDelete);

        return boatsList;
    }

    @Override
    public void addInstance(BoutHouseInstance instance) throws ExtensionException, JAXBException, FileNotFoundException {
        Path pathOfDatabaseXml = Paths.get(BOAT_DATA_BASE_LOCATION);
        InputStream inputStream;
        Boats boats;
        try {
            inputStream = getInputStreamFromPath(pathOfDatabaseXml);
            boats = (Boats) deserializeFrom(inputStream);
        } catch (FileNotFoundException e) {
            boats = new Boats();
        }

        List<Boat> membersList = boats.getBoat();
        membersList.add(fromBoutHouseBoatToBoatJaxbObject((BMS.bouthouse.storage.vessel.Boat) instance));

        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), boats);
    }

    @Override
    public void saveInstances(Collection<BoutHouseInstance> instances) throws FileNotFoundException, ExtensionException, JAXBException {
        Boats boats = new Boats();

        instances.forEach((instance) -> {
            boats.getBoat().add(fromBoutHouseBoatToBoatJaxbObject((BMS.bouthouse.storage.vessel.Boat) instance));
        });

        Path pathOfDatabaseXml = Paths.get(BOAT_DATA_BASE_LOCATION);
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), boats);
    }

    @Override
    public ArrayList<ArrayList<InfoField>> loadDataBase() throws FileNotFoundException, ExtensionException, JAXBException {
        return fromXMLFileToListOfInstancesArgs(Paths.get(BOAT_DATA_BASE_LOCATION));
    }

    public static void serializeTo(OutputStream out, Boats objectToWrite) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME_BOATS);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(objectToWrite, out);
    }

    @Override
    public void resetDatabase() throws ExtensionException, FileNotFoundException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(BOAT_DATA_BASE_LOCATION);
        Boats newBoats = new Boats();
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), newBoats);
    }

}
