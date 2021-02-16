package BMS.xml.handler;

import BMS.boutHouse.form.field.infoField.FieldTypeIsNotSupportExcpetion;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.infoField.InfoFieldMaker;
import BMS.boutHouse.form.field.infoField.UserInputForInfoFIeldException;
import BMS.boutHouse.form.field.type.Informable;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.validator.Validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public abstract class BoutHouseXMLLoader {
    final static String JAXB_XML_PACKAGE_NAME = "BMS.xml.schema.generated";

    protected abstract String getJaxbXmlPackageName();

    public static String getMasterJaxbXmlPackageName(){
        return JAXB_XML_PACKAGE_NAME;
    }

    public abstract ArrayList<ArrayList<InfoField>> fromXMLFileToListOfInstancesArgs(Path path) throws JAXBException, FileNotFoundException, ExtensionException;

    // todo throw good Exception to user or just collect all the best and print in the end
    public ArrayList<InfoField> fromListOfStringJaxbInstanceToArrayOfBoutHouseInstanceArgs(Map<Informable, String> stringValues) {
        ArrayList<InfoField> boatArgs = new ArrayList<>();

        stringValues.forEach((type, value)->{
            try {
                boatArgs.add(InfoFieldMaker.createInfoField(value, type));
            } catch (UserInputForInfoFIeldException | FieldTypeIsNotSupportExcpetion ignored) {
            }
        });


        return boatArgs;
    }

    public static InputStream getInputStreamFromPath(Path path) throws ExtensionException, FileNotFoundException {
        File file = getFileFromPath(path);
        return new FileInputStream(file);
    }

    private static File getFileFromPath(Path path) throws ExtensionException {
        checkExtensionOfPath(path);
        return path.toFile();
    }

    public static void checkExtensionOfPath(Path path) throws ExtensionException {
        if (!Validator.isXMLExtension(path.toString())) {
            throw new ExtensionException();
        }
    }

    public Object deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(getJaxbXmlPackageName());
        Unmarshaller u = jc.createUnmarshaller();
        return u.unmarshal(in);
    }

}
