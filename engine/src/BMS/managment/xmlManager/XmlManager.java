package BMS.managment.xmlManager;

import BMS.XML.handler.BoatXmlReader;
import BMS.XML.handler.BoutHouseXMLReader;
import BMS.XML.handler.MembersXMLReader;
import BMS.XML.handler.TimeWindowXmlReader;
import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.managment.userManager.MemberManager;
import BMS.managment.utils.exceptions.NeedToLoginException;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;
import BMS.server.BoutHouseDataType;
import BMS.xml.exceptions.ExtensionException;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class XmlManager {
    HashMap<BoutHouseDataType, BoutHouseXMLReader> xmlManagers = new HashMap<>();
    private static final String DATA_BASE_LOCATION = "C:/BoutHouseProjectDataBase";

    public XmlManager(){
        xmlManagers.put(BoutHouseDataType.MEMBERS, new MembersXMLReader());
        xmlManagers.put(BoutHouseDataType.TIME_WINDOW, new TimeWindowXmlReader());
        xmlManagers.put(BoutHouseDataType.BOATS, new BoatXmlReader());
    }

    public void saveInstancesToXml(BoutHouseDataType typeOfManager, InfoField<String> emailOfSaver, Collection<BoutHouseInstance> instances) throws FileNotFoundException, ExtensionException, OnlyManagerAccessException, JAXBException, NeedToLoginException {
        BoutHouseXMLReader xmlManager= xmlManagers.get(typeOfManager);

        MemberManager.checkLoggedIsManager(emailOfSaver);
        xmlManager.saveInstances(instances);
    }

    public void updateInstanceToExistingXml(BoutHouseDataType typeOfManager, InfoField<String> emailBeforeUpdate, BoutHouseInstance instances) throws FileNotFoundException, ExtensionException, JAXBException {
        BoutHouseXMLReader xmlManager= xmlManagers.get(typeOfManager);
        xmlManager.updateInstances(emailBeforeUpdate, instances);
    }

    public void deleteInstanceToExistingXml(BoutHouseDataType typeOfManager, InfoField<String> emailOfDeleter, InfoField idOfInstanceToDelete) throws FileNotFoundException, ExtensionException, OnlyManagerAccessException, JAXBException, NeedToLoginException {
        BoutHouseXMLReader xmlManager= xmlManagers.get(typeOfManager);
        MemberManager.checkLoggedIsManager(emailOfDeleter);
        xmlManager.deleteInstance(idOfInstanceToDelete);
    }

    public void addInstanceToExistingXml(BoutHouseDataType typeOfManager, InfoField<String> emailOfAdder, BoutHouseInstance instances) throws ExtensionException, FileNotFoundException, OnlyManagerAccessException, JAXBException, NeedToLoginException {
        BoutHouseXMLReader xmlManager= xmlManagers.get(typeOfManager);
        MemberManager.checkLoggedIsManager(emailOfAdder);
        xmlManager.addInstance(instances);
    }

    public ArrayList<ArrayList<InfoField>> loadArgsFromXml(BoutHouseDataType typeOfManager, InfoField<String> emailOfLoader, InfoField<String> xmlPath) throws FileNotFoundException, ExtensionException, OnlyManagerAccessException, JAXBException, NeedToLoginException {
        BoutHouseXMLReader xmlManager = xmlManagers.get(typeOfManager);

        MemberManager.checkLoggedIsManager(emailOfLoader);
        Path pathToXml = Paths.get(xmlPath.getValue());

        return xmlManager.fromXMLFileToListOfInstancesArgs(pathToXml);
    }

    public ArrayList<ArrayList<InfoField>> loadDataBaseXml(BoutHouseDataType managerType) throws FileNotFoundException, ExtensionException, JAXBException {
        BoutHouseXMLReader xmlManager = xmlManagers.get(managerType);
        return xmlManager.loadDataBase();
    }

    public void resetDataBaseOfBoutHouseType(BoutHouseDataType managerType) throws FileNotFoundException, ExtensionException, JAXBException {
        BoutHouseXMLReader xmlManager = xmlManagers.get(managerType);
        xmlManager.resetDatabase();
    }

    public String getDataBaseLocation(BoutHouseDataType managerType){
        BoutHouseXMLReader xmlManager = xmlManagers.get(managerType);
        return xmlManager.getDataBaseLocation();
    }
}
