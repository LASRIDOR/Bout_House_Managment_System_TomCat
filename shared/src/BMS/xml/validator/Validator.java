package BMS.xml.validator;

public class Validator {
    public static boolean isXMLExtension(String path){
        boolean isXMLPath;

        if(path.endsWith(".xml")){
            isXMLPath = true;
        }
        else {
            isXMLPath = false;
        }

        return isXMLPath;
    }
}
