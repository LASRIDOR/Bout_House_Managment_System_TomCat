package BMS.xml.handler;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.TimeWindowInfoFieldType;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.schema.generated.activities.Activities;
import BMS.xml.schema.generated.activities.BoatType;
import BMS.xml.schema.generated.activities.Timeframe;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TimeWindowXmlLoader extends BoutHouseXMLLoader {
    private static final String JAXB_XML_PACKAGE_NAME_ACTIVITIES = getMasterJaxbXmlPackageName() + ".activities";

    @Override
    protected String getJaxbXmlPackageName() {
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

    private ArrayList<InfoField> fromTimeFrameJaxbToTimeWindowArgs(Timeframe timeframe) {
        Map<Informable, String> strigArgs = new HashMap<>();

        strigArgs.put(TimeWindowInfoFieldType.TIME_WINDOW_NAME, timeframe.getName());
        strigArgs.put(TimeWindowInfoFieldType.ACTIVITY_START_TIME, timeframe.getStartTime());
        strigArgs.put(TimeWindowInfoFieldType.ACTIVITY_END_TIME, timeframe.getEndTime());
        strigArgs.put(TimeWindowInfoFieldType.BOAT_TYPE, fromJaxbBoatTypeToBoutHouseBoatType(timeframe.getBoatType()));


        return fromListOfStringJaxbInstanceToArrayOfBoutHouseInstanceArgs(strigArgs);
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
}
