package BMS.xml.exceptions;

public class ExtensionException extends Exception {
    public ExtensionException() {
        super("Error in Export XML: path is not to XML file");
    }
}
