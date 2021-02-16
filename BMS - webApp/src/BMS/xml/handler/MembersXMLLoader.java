package BMS.xml.handler;

import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.LevelRowing;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.schema.generated.members.Member;
import BMS.xml.schema.generated.members.Members;
import BMS.xml.schema.generated.members.RowingLevel;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembersXMLLoader extends BoutHouseXMLLoader {
    private static final String JAXB_XML_PACKAGE_NAME_MEMBERS = getMasterJaxbXmlPackageName() + ".members";

    @Override
    protected String getJaxbXmlPackageName() {
        return JAXB_XML_PACKAGE_NAME_MEMBERS;
    }

    public ArrayList<ArrayList<InfoField>> fromXMLFileToListOfInstancesArgs(Path path) throws JAXBException, FileNotFoundException, ExtensionException {
        InputStream inputStream = getInputStreamFromPath(path);
        Members members = (Members) deserializeFrom(inputStream);
        ArrayList<ArrayList<InfoField>> memberForms = fromMembersJaxbToArrayMemberFormsArgs(members);

        return memberForms;
    }

    public ArrayList<ArrayList<InfoField>> fromMembersJaxbToArrayMemberFormsArgs(Members members) {
        List<Member> listOfMember= members.getMember();

        return fromListOfMemberJaxbToArrayOfMemberFormArgs(listOfMember);
    }

    public ArrayList<ArrayList<InfoField>> fromListOfMemberJaxbToArrayOfMemberFormArgs(List<Member> listOfMember) {
        ArrayList<ArrayList<InfoField>> allMemberForms = new ArrayList<>();

        for(Member m:listOfMember){
            allMemberForms.add(fromMemberJaxbToMemberFormArgs(m));
        }

        return allMemberForms;
    }

    public ArrayList<InfoField> fromMemberJaxbToMemberFormArgs(Member member) {
        Map<Informable, String> stringValues = new HashMap<>();

        stringValues.put(MemberFieldType.EMAIL, member.getEmail());
        stringValues.put(MemberFieldType.USERNAME, member.getName());
        stringValues.put(MemberFieldType.PASSWORD, member.getPassword());
        stringValues.put(MemberFieldType.DATE_OF_JOIN, member.getJoined().toString());
        stringValues.put(MemberFieldType.AGE, String.valueOf(member.getAge()));
        stringValues.put(MemberFieldType.DATE_OF_EXPIRY, String.valueOf(member.getMembershipExpiration()));
        stringValues.put(MemberFieldType.FREE_COMMENT_PLACE, member.getComments());
        stringValues.put(MemberFieldType.IS_MANAGER, fromIsManagerJaxbToBoutHouse(member.isManager()));
        stringValues.put(MemberFieldType.PRIVATE_BOAT_HASH, member.getId());
        stringValues.put(MemberFieldType.PHONE_NUMBER, member.getPhone());
        stringValues.put(MemberFieldType.LEVEL, fromRowingLevelJaxbToBoutHouse(member.getLevel()));

        return fromListOfStringJaxbInstanceToArrayOfBoutHouseInstanceArgs(stringValues);
    }

    private String fromIsManagerJaxbToBoutHouse(Boolean manager) {
        String isManager;

        if(manager == null || !manager){
            isManager = "no";
        }
        else{
            isManager= "yes";
        }

        return isManager;
    }

    private static String fromRowingLevelJaxbToBoutHouse(RowingLevel level) {
        String myLevelRowing;

        if(level == RowingLevel.BEGINNER){
            myLevelRowing = LevelRowing.BEGINNER.getNameOfLevel();
        }
        else if(level == RowingLevel.INTERMEDIATE){
            myLevelRowing = LevelRowing.INTERMEDIATE.getNameOfLevel();
        }
        else if (level == RowingLevel.ADVANCED){
            myLevelRowing = LevelRowing.ADVANCED.getNameOfLevel();
        }
        else{
            myLevelRowing = null;
        }

        return myLevelRowing;
    }
}

