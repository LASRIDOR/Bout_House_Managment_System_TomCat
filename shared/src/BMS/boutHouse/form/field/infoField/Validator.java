package BMS.boutHouse.form.field.infoField;

import BMS.boutHouse.form.field.type.Informable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// todo improve name
public class Validator {

    public static boolean isInfoFieldInputValid(Informable type, String value) {
        // option doing a configurable regex for manager
        // Compile the ReGex
        Pattern p = Pattern.compile(type.getRegexPattern());

        // If the field is empty
        // return false
        if (value == null) {
            return false;
        }

        if (type.getRegexPattern() == null) {
            return true;
        }

        // Pattern class contains matcher() method
        // to find matching between given field
        // and regular expression.
        Matcher m = p.matcher(value);

        // Return if the field
        // matched the ReGex
        return m.matches();
    }
}
