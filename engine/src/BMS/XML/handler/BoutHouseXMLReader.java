package BMS.XML.handler;

import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.managment.utils.exceptions.OnlyManagerAccessException;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.validator.Validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public abstract class BoutHouseXMLReader {
    final static String JAXB_XML_PACKAGE_NAME = "BMS.xml.schema.generated";
    private static final String DATA_BASE_LOCATION = System.getProperty("user.dir") + "\\BoutHouseProjectDataBase";

    public BoutHouseXMLReader(){
        File file = new File(DATA_BASE_LOCATION);
        //Creating the directory
        boolean bool = file.mkdir();
        if(bool){
            System.out.println(DATA_BASE_LOCATION + "Directory created successfully");
        }else{
            System.out.println(DATA_BASE_LOCATION + "Sorry couldn't create specified directory");
        }
    }

    public static String getDataBaseLocation() {
        return DATA_BASE_LOCATION;
    }

    public static String getMasterJaxbXmlPackageName(){
        return JAXB_XML_PACKAGE_NAME;
    }

    public abstract String getJaxbXmlPackageName();

    public abstract ArrayList<ArrayList<InfoField>> fromXMLFileToListOfInstancesArgs(Path path) throws JAXBException, FileNotFoundException, OnlyManagerAccessException, ExtensionException;

    public abstract void updateInstances(InfoField idOfInstanceBeforeUpdate, BoutHouseInstance instance) throws FileNotFoundException, ExtensionException, JAXBException;

    public abstract void deleteInstance(InfoField idOfInstanceToDelete) throws FileNotFoundException, OnlyManagerAccessException, ExtensionException, JAXBException;

    public abstract void addInstance(BoutHouseInstance instance) throws FileNotFoundException, OnlyManagerAccessException, ExtensionException, JAXBException;

    public abstract void saveInstances(Collection<BoutHouseInstance> instances) throws FileNotFoundException, OnlyManagerAccessException, ExtensionException, JAXBException;

    // todo throw good Exception to user or just collect all the best and print in the end
    public ArrayList<InfoField> fromListOfStringJaxbInstanceToArrayOfBoutHouseInstanceArgs(Map<Informable, String> stringValues) {
        ArrayList<InfoField> allInfoFields = new ArrayList<>();

        stringValues.forEach((type, value)->{
            try {
                allInfoFields.add(InfoFieldMaker.createInfoField(value, type));
            } catch (FieldTypeIsNotSupportExcpetion | UserInputForInfoFIeldException ignored) {
            }
        });

        return allInfoFields;
    }

    public static InputStream getInputStreamFromPath(Path path) throws ExtensionException, FileNotFoundException {
        File file = getFileFromPath(path);
        return new FileInputStream(file);
    }

    public static OutputStream getOutputStreamFromPath(Path path) throws ExtensionException, FileNotFoundException {
        File file = getFileFromPath(path);
        return new FileOutputStream(file);
    }

    private static File getFileFromPath(Path path) throws ExtensionException {
        if (!Validator.isXMLExtension(path.toString())) {
            throw new ExtensionException();
        }else {
            return path.toFile();
        }
    }

    public Object deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(getJaxbXmlPackageName());
        Unmarshaller u = jc.createUnmarshaller();
        return u.unmarshal(in);
    }

    public abstract ArrayList<ArrayList<InfoField>> loadDataBase() throws FileNotFoundException, ExtensionException, JAXBException;

    public abstract void resetDatabase() throws ExtensionException, FileNotFoundException, JAXBException;
}
