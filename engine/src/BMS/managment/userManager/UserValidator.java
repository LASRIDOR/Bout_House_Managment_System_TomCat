package BMS.managment.userManager;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.LevelRowing;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.bouthouse.users.MemberForm;

import java.time.LocalDateTime;
import java.util.Map;

// Todo check as option to move all checking validation here and make all form as lazy set
public class UserValidator {
    public static boolean isValidMember(MemberForm memberToCheck){
        Map<Informable, InfoField> memberFields = memberToCheck.getAllFields();
        boolean isValidMember;

        if(isContainAllMustHaveField(memberFields) && isAllMemberFieldValid(memberFields)){
            isValidMember = true;
        }
        else{
            isValidMember = false;
        }

        return isValidMember;
    }

    public static boolean isAllMemberFieldValid(Map<Informable, InfoField> memberFields){
        for (InfoField field: memberFields.values()) {
            if(!isValidMemberField(field) && field.getType() instanceof MemberFieldType){
                return false;
            }
        }

        return true;
    }

    public static boolean isContainAllMustHaveField(Map<Informable, InfoField> memberFields) {
        if(memberFields.containsKey(MemberFieldType.EMAIL) && memberFields.containsKey(MemberFieldType.PASSWORD) && memberFields.containsKey(MemberFieldType.USERNAME)){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isValidMemberField(InfoField infoField){
        boolean isCompatible = false;

        if((infoField.getType() == MemberFieldType.EMAIL || infoField.getType() == MemberFieldType.PASSWORD || infoField.getType() == MemberFieldType.FREE_COMMENT_PLACE || infoField.getType() == MemberFieldType.PHONE_NUMBER || infoField.getType() == MemberFieldType.USERNAME) && infoField.getValue() instanceof String){
            isCompatible = true;
        }
        else if(infoField.getType() == MemberFieldType.DATE_OF_EXPIRY || infoField.getType() == MemberFieldType.DATE_OF_JOIN){
            if(infoField.getValue() instanceof LocalDateTime){
                isCompatible = true;
            }
        }
        else if(infoField.getType() == MemberFieldType.LEVEL){
            if(infoField.getValue() instanceof LevelRowing){
                isCompatible = true;
            }
        }
        else if (infoField.getType() == MemberFieldType.PRIVATE_BOAT_HASH || infoField.getType() == MemberFieldType.AGE){
            if(infoField.getValue() instanceof Integer){
                isCompatible = true;
            }
        }
        else if (infoField.getType() == MemberFieldType.IS_MANAGER){
            if(infoField.getValue() instanceof Boolean){
                isCompatible = true;
            }
        }
        else{
            isCompatible = false;
        }

        return isCompatible;
    }
}
