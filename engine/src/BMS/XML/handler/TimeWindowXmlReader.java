package BMS.XML.handler;

import BMS.boutHouse.activities.TimeWindow;
import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.TimeWindowInfoFieldType;
import BMS.server.BoutHouseDataType;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.schema.generated.activities.Activities;
import BMS.xml.schema.generated.activities.BoatType;
import BMS.xml.schema.generated.activities.Timeframe;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TimeWindowXmlReader extends BoutHouseXMLReader {
    private static final String JAXB_XML_PACKAGE_NAME_ACTIVITIES = getMasterJaxbXmlPackageName() + ".activities";
    private static final String TIME_WINDOW_DATA_BASE_LOCATION = getDataBaseLocation() + "/" + BoutHouseDataType.TIME_WINDOW.getNameOfManager() + " Records.xml";

    @Override
    public String getJaxbXmlPackageName() {
        return JAXB_XML_PACKAGE_NAME_ACTIVITIES;
    }

    @Override
    public ArrayList<ArrayList<InfoField>> fromXMLFileToListOfInstancesArgs(Path path) throws JAXBException, FileNotFoundException, ExtensionException {
        InputStream inputStream = getInputStreamFromPath(path);
        Activities activities = (Activities) deserializeFrom(inputStream);
        ArrayList<ArrayList<InfoField>> timeWindows = fromActivityJaxbToArrayTimeWindowArgs(activities);

        return timeWindows;
    }

    private ArrayList<ArrayList<InfoField>> fromActivityJaxbToArrayTimeWindowArgs(Activities activities) {
        List<Timeframe> timeframeList = activities.getTimeframe();

        return fromListOfTimeFrameJaxbToArrayOfTimeWindowArgs(timeframeList);
    }

    private ArrayList<ArrayList<InfoField>> fromListOfTimeFrameJaxbToArrayOfTimeWindowArgs(List<Timeframe> timeframeList) {
        ArrayList<ArrayList<InfoField>> timeWindow = new ArrayList<>();

        for(Timeframe timeframe:timeframeList){
            timeWindow.add(fromTimeFrameJaxbToTimeWindowArgs(timeframe));
        }

        return timeWindow;
    }

    private ArrayList<InfoField>fromTimeFrameJaxbToTimeWindowArgs(Timeframe timeframe) {
        Map<Informable, String> stringValues = new HashMap<>();

        stringValues.put(TimeWindowInfoFieldType.TIME_WINDOW_NAME, timeframe.getName());
        stringValues.put(TimeWindowInfoFieldType.ACTIVITY_START_TIME, timeframe.getStartTime());
        stringValues.put(TimeWindowInfoFieldType.ACTIVITY_END_TIME, timeframe.getEndTime());
        stringValues.put(TimeWindowInfoFieldType.BOAT_TYPE, fromJaxbBoatTypeToBoutHouseBoatType(timeframe.getBoatType()));

        return fromListOfStringJaxbInstanceToArrayOfBoutHouseInstanceArgs(stringValues);
    }

    private String fromJaxbBoatTypeToBoutHouseBoatType(BoatType boatType) {
        String boatTypeInput;

        if(boatType == BoatType.SINGLE){
            boatTypeInput = "1X";
        }
        else if (boatType == BoatType.DOUBLE){
            boatTypeInput = ("2X");
        }
        else if (boatType == BoatType.COXED_PAIR){
            boatTypeInput = ("2+");
        }
        else if (boatType == BoatType.COXED_DOUBLE){
            boatTypeInput = ("2X+");
        }
        else if (boatType == BoatType.PAIR){
            boatTypeInput = ("2-");
        }
        else if (boatType == BoatType.COXED_QUAD){
            boatTypeInput = ("4X+");
        }
        else if (boatType == BoatType.COXED_FOUR){
            boatTypeInput = ("4+");
        }
        else if (boatType == BoatType.QUAD){
            boatTypeInput = ("4X");
        }
        else if (boatType == BoatType.FOUR){
            boatTypeInput = ("4-");
        }
        else if (boatType == BoatType.OCTUPLE){
            boatTypeInput = ("8X+");
        }
        else if (boatType == BoatType.EIGHT) {
            boatTypeInput = ("8+");
        }
        else {
            boatTypeInput = null;
        }

        return boatTypeInput;
    }

    @Override
    public void updateInstances(InfoField idOfInstanceBeforeUpdate, BoutHouseInstance instance) throws FileNotFoundException, ExtensionException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(TIME_WINDOW_DATA_BASE_LOCATION);
        InputStream inputStream = getInputStreamFromPath(pathOfDatabaseXml);
        Activities activities = (Activities) deserializeFrom(inputStream);

        List<Timeframe> boatsList = removeTimeFrameFromList(activities.getTimeframe(), idOfInstanceBeforeUpdate);
        Timeframe convertedTimeWindow = fromTimeWindowToTimeFrameJaxb((TimeWindow) instance);
        boatsList.add(convertedTimeWindow);
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), activities);
    }

    @Override
    public void deleteInstance(InfoField idOfInstanceToDelete) throws FileNotFoundException, ExtensionException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(TIME_WINDOW_DATA_BASE_LOCATION);
        InputStream inputStream = getInputStreamFromPath(pathOfDatabaseXml);

        Activities activities = (Activities) deserializeFrom(inputStream);
        List<Timeframe> activitiesList = activities.getTimeframe();
        activitiesList = removeTimeFrameFromList(activitiesList, idOfInstanceToDelete);

        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), activities);
    }

    private List<Timeframe> removeTimeFrameFromList(List<Timeframe> activitiesList, InfoField idOfInstanceToDelete) {
        Timeframe timeFrameToDelete = new Timeframe();

        for (Timeframe timeFrame:activitiesList) {
            if (timeFrame.getName().equals((String) idOfInstanceToDelete.getValue())){
                timeFrameToDelete = timeFrame;
            }
        }

        activitiesList.remove(timeFrameToDelete);

        return activitiesList;
    }

    @Override
    public void addInstance(BoutHouseInstance instance) throws ExtensionException, JAXBException, FileNotFoundException {
        Path pathOfDatabaseXml = Paths.get(TIME_WINDOW_DATA_BASE_LOCATION);
        InputStream inputStream;
        Activities activities;
        try {
            inputStream = getInputStreamFromPath(pathOfDatabaseXml);
            activities = (Activities) deserializeFrom(inputStream);
        } catch (FileNotFoundException e) {
            activities = new Activities();
        }

        List<Timeframe> activitiesList = activities.getTimeframe();
        activitiesList.add(fromTimeWindowToTimeFrameJaxb((TimeWindow) instance));

        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), activities);
    }

    private BoatType fromBoatTypeBoutHouseToJaxb(String boatClassification) {
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

    // todo finish save instance to xml
    @Override
    public void saveInstances(Collection<BoutHouseInstance> instances) throws FileNotFoundException, ExtensionException, JAXBException {
        Activities activities = new Activities();

        instances.forEach((instance) -> { activities.getTimeframe().add(fromTimeWindowToTimeFrameJaxb((TimeWindow) instance)); });

        Path pathOfDatabaseXml = Paths.get(TIME_WINDOW_DATA_BASE_LOCATION);
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), activities);
    }

    @Override
    public ArrayList<ArrayList<InfoField>> loadDataBase() throws FileNotFoundException, ExtensionException, JAXBException {
        return fromXMLFileToListOfInstancesArgs(Paths.get(TIME_WINDOW_DATA_BASE_LOCATION));
    }

    private Timeframe fromTimeWindowToTimeFrameJaxb(TimeWindow timeWindow) {
        Timeframe timeFrameToReturn = new Timeframe();
        Map<Informable, InfoField> allMemberFormField = timeWindow.getAllFields();

        for (InfoField value : allMemberFormField.values()) {
            if(value.getType() == TimeWindowInfoFieldType.TIME_WINDOW_NAME){
                timeFrameToReturn.setName((String) value.getValue());
            }
            else if (value.getType() == TimeWindowInfoFieldType.ACTIVITY_END_TIME){
                timeFrameToReturn.setEndTime(String.valueOf(value.getValue()));
            }
            else if (value.getType() == TimeWindowInfoFieldType.ACTIVITY_START_TIME){
                timeFrameToReturn.setStartTime(String.valueOf(value.getValue()));
            }
            else if (value.getType() == TimeWindowInfoFieldType.BOAT_TYPE){
                timeFrameToReturn.setBoatType(fromBoatTypeBoutHouseToJaxb((String) value.getValue()));
            }
            else{
            }
        }

        return timeFrameToReturn;
    }

    public static void serializeTo(OutputStream out, Activities objectToWrite) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME_ACTIVITIES);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(objectToWrite, out);
    }

    @Override
    public void resetDatabase() throws ExtensionException, FileNotFoundException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(TIME_WINDOW_DATA_BASE_LOCATION);
        Activities newActivities = new Activities();
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), newActivities);
    }
}
