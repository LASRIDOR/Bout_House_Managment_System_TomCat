package BMS.xml.xmlManager;


import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.server.BoutHouseDataType;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.handler.BoatXmlLoader;
import BMS.xml.handler.BoutHouseXMLLoader;
import BMS.xml.handler.MembersXMLLoader;
import BMS.xml.handler.TimeWindowXmlLoader;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class XmlManager {
    HashMap<BoutHouseDataType, BoutHouseXMLLoader> xmlManagers = new HashMap<>();
    private static final String DATA_BASE_LOCATION = "C:/BoutHouseProjectDataBase";

    public XmlManager(){
        xmlManagers.put(BoutHouseDataType.MEMBERS, new MembersXMLLoader());
        xmlManagers.put(BoutHouseDataType.TIME_WINDOW, new TimeWindowXmlLoader());
        xmlManagers.put(BoutHouseDataType.BOATS, new BoatXmlLoader());
    }

    public ArrayList<ArrayList<InfoField>> loadArgsFromXml(BoutHouseDataType typeOfManager, InfoField<String> xmlPath) throws FileNotFoundException, ExtensionException, JAXBException {
        BoutHouseXMLLoader xmlManager = xmlManagers.get(typeOfManager);

        Path pathToXml = Paths.get(xmlPath.getValue());

        return xmlManager.fromXMLFileToListOfInstancesArgs(pathToXml);
    }

}
