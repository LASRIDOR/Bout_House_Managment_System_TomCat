package BMS.XML.handler;


import BMS.boutHouse.form.BoutHouseInstance;
import BMS.boutHouse.form.field.infoField.InfoField;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.field.type.LevelRowing;
import BMS.boutHouse.form.field.type.MemberFieldType;
import BMS.bouthouse.users.MemberForm;
import BMS.server.BoutHouseDataType;
import BMS.xml.exceptions.ConvertingException;
import BMS.xml.exceptions.ExtensionException;
import BMS.xml.schema.generated.members.Member;
import BMS.xml.schema.generated.members.Members;
import BMS.xml.schema.generated.members.RowingLevel;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class MembersXMLReader extends BoutHouseXMLReader {
    private static final String JAXB_XML_PACKAGE_NAME_MEMBERS = getMasterJaxbXmlPackageName() + ".members";
    private static final String MEMBER_DATA_BASE_LOCATION = getDataBaseLocation() + "\\" + BoutHouseDataType.MEMBERS.getNameOfManager() + " Records.xml";

    @Override
    public String getJaxbXmlPackageName() {
        return JAXB_XML_PACKAGE_NAME_MEMBERS;
    }

    public ArrayList<ArrayList<InfoField>> fromXMLFileToListOfInstancesArgs(Path path) throws JAXBException, FileNotFoundException, ExtensionException {
        InputStream inputStream = getInputStreamFromPath(path);
        Members members = (Members) deserializeFrom(inputStream);
        ArrayList<ArrayList<InfoField>> memberForms = fromMembersJaxbToArrayMemberFormsArgs(members);

        return memberForms;
    }

    public void updateInstances(InfoField idOfInstanceBeforeUpdate, BoutHouseInstance instance) throws FileNotFoundException, ExtensionException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(MEMBER_DATA_BASE_LOCATION);
        InputStream inputStream = getInputStreamFromPath(pathOfDatabaseXml);
        Members members = (Members) deserializeFrom(inputStream);

        List<Member> membersList = removeMemberFromList(members.getMember(), idOfInstanceBeforeUpdate);
        Member convertedMember = fromMemberFormToMemberJaxb((MemberForm) instance);
        membersList.add(convertedMember);
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), members);
    }

    public void deleteInstance(InfoField idOfInstanceToDelete) throws FileNotFoundException, ExtensionException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(MEMBER_DATA_BASE_LOCATION);
        InputStream inputStream = getInputStreamFromPath(pathOfDatabaseXml);

        Members members = (Members) deserializeFrom(inputStream);
        List<Member> membersList = members.getMember();
        membersList = removeMemberFromList(membersList, idOfInstanceToDelete);

        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), members);
    }

    private List<Member> removeMemberFromList(List<Member> membersList, InfoField instance) {
        Member memberToDelete = new Member();

        for (Member member:membersList) {
            if (member.getEmail().equals(instance.getValue())){
                memberToDelete = member;
            }
        }

        membersList.remove(memberToDelete);

        return membersList;
    }

    public void addInstance(BoutHouseInstance instance) throws ExtensionException, JAXBException, FileNotFoundException {
        Path pathOfDatabaseXml = Paths.get(MEMBER_DATA_BASE_LOCATION);
        InputStream inputStream;
        Members members;
        try {
            inputStream = getInputStreamFromPath(pathOfDatabaseXml);
            members = (Members) deserializeFrom(inputStream);
        } catch (FileNotFoundException e) {
            members = new Members();
        }

        List<Member> membersList = members.getMember();
        membersList.add(fromMemberFormToMemberJaxb((MemberForm) instance));

        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), members);
    }

    public void saveInstances(Collection<BoutHouseInstance> instances) throws FileNotFoundException, ExtensionException, JAXBException {
        Members members = new Members();

        instances.forEach((instance) -> {
            members.getMember().add(fromMemberFormToMemberJaxb((MemberForm) instance));
        });

        Path pathOfDatabaseXml = Paths.get(MEMBER_DATA_BASE_LOCATION);
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), members);
    }

    @Override
    public ArrayList<ArrayList<InfoField>> loadDataBase() throws FileNotFoundException, ExtensionException, JAXBException {
        return fromXMLFileToListOfInstancesArgs(Paths.get(MEMBER_DATA_BASE_LOCATION));
    }

    @Override
    public void resetDatabase() throws ExtensionException, FileNotFoundException, JAXBException {
        Path pathOfDatabaseXml = Paths.get(MEMBER_DATA_BASE_LOCATION);
        Members newMembers = new Members();
        serializeTo(getOutputStreamFromPath(pathOfDatabaseXml), newMembers);
    }

    public ArrayList<ArrayList<InfoField>> fromMembersJaxbToArrayMemberFormsArgs(Members members) {
        List<Member> listOfMember= members.getMember();

        return fromListOfMemberJaxbToArrayOfMemberFormArgs(listOfMember);
    }

    public ArrayList<ArrayList<InfoField>> fromListOfMemberJaxbToArrayOfMemberFormArgs(List<Member> listOfMember) {
        ArrayList<ArrayList<InfoField>> memberForm = new ArrayList<>();

        for(Member m:listOfMember){
            memberForm.add(fromMemberJaxbToMemberFormArgs(m));
        }

        return memberForm;
    }

    public ArrayList<InfoField> fromMemberJaxbToMemberFormArgs(Member member) {
        ArrayList<InfoField> memberFormArgs;
        Map<Informable, String> stringValues = new HashMap<>();

        stringValues.put(MemberFieldType.EMAIL, member.getEmail());
        stringValues.put(MemberFieldType.USERNAME, member.getName());
        //stringValues.put(MemberFieldType.PASSWORD, member.getPassword());
        stringValues.put(MemberFieldType.DATE_OF_JOIN, member.getJoined().toString());
        stringValues.put(MemberFieldType.AGE, String.valueOf(member.getAge()));
        stringValues.put(MemberFieldType.DATE_OF_EXPIRY, String.valueOf(member.getMembershipExpiration()));
        stringValues.put(MemberFieldType.FREE_COMMENT_PLACE, member.getComments());
        stringValues.put(MemberFieldType.IS_MANAGER, fromIsManagerJaxbToBoutHouse(member.isManager()));
        stringValues.put(MemberFieldType.PRIVATE_BOAT_HASH, member.getId());
        stringValues.put(MemberFieldType.PHONE_NUMBER, member.getPhone());
        stringValues.put(MemberFieldType.LEVEL, fromRowingLevelJaxbToBoutHouse(member.getLevel()));

        memberFormArgs = fromListOfStringJaxbInstanceToArrayOfBoutHouseInstanceArgs(stringValues);
        memberFormArgs.add(new InfoField<>(MemberFieldType.PASSWORD, member.getPassword()));
        return memberFormArgs;
    }

    private Member fromMemberFormToMemberJaxb(MemberForm memberForm) {
        Member memberToReturn = new Member();
        Map<Informable, InfoField> allMemberFormField = memberForm.getAllFields();

        for (InfoField field : allMemberFormField.values()) {
            if(field.getType() == MemberFieldType.EMAIL){
                memberToReturn.setEmail((String) field.getValue());
            }
            else if (field.getType() == MemberFieldType.AGE){
                memberToReturn.setAge((Integer) field.getValue());
            }
            else if (field.getType() == MemberFieldType.USERNAME){
                memberToReturn.setName((String) field.getValue());
            }
            else if (field.getType() == MemberFieldType.IS_MANAGER){
                memberToReturn.setManager((Boolean) field.getValue());
            }
            else if (field.getType() == MemberFieldType.PASSWORD){
                memberToReturn.setPassword((String) field.getValue());
            }
            else if (field.getType() == MemberFieldType.FREE_COMMENT_PLACE){
                memberToReturn.setComments((String) field.getValue());
            }
            else if (field.getType() == MemberFieldType.DATE_OF_JOIN){
                memberToReturn.setJoined(fromLocalDateTimeToXmlGregorianCalendar((LocalDateTime) field.getValue()));
            }
            else if (field.getType() == MemberFieldType.DATE_OF_EXPIRY){
                memberToReturn.setMembershipExpiration(fromLocalDateTimeToXmlGregorianCalendar((LocalDateTime) field.getValue()));
            }
            else if (field.getType() == MemberFieldType.LEVEL){
                try {
                    memberToReturn.setLevel(fromLevelBoutHouseToJaxb((LevelRowing) field.getValue()));
                } catch (ConvertingException ignored) {
                }
            }
            else if (field.getType() == MemberFieldType.PRIVATE_BOAT_HASH){
                memberToReturn.setId(String.valueOf(field.getValue()));
            }
            else if (field.getType() == MemberFieldType.PHONE_NUMBER){
                memberToReturn.setPhone((String) field.getValue());
            }
            else{
                //throw new ConvertingException(field.getType().getNameOfField(), "XML Member Reader");
            }
        }

        return memberToReturn;
    }

    private XMLGregorianCalendar fromLocalDateTimeToXmlGregorianCalendar(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();
        XMLGregorianCalendar xmlGregorianCalendar = new XMLGregorianCalendarImpl();
        xmlGregorianCalendar.setYear(localDate.getYear());
        xmlGregorianCalendar.setMonth(localDate.getMonthValue());
        xmlGregorianCalendar.setDay(localDate.getDayOfMonth());
        xmlGregorianCalendar.setTime(localTime.getHour(), localTime.getMinute(), localTime.getSecond());

        return xmlGregorianCalendar;
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

    private RowingLevel fromLevelBoutHouseToJaxb(LevelRowing value) throws ConvertingException {
        RowingLevel level;

        if(value == LevelRowing.BEGINNER){
            level = RowingLevel.BEGINNER;
        }
        else if (value == LevelRowing.INTERMEDIATE){
            level = RowingLevel.INTERMEDIATE;
        }
        else if(value == LevelRowing.ADVANCED){
            level = RowingLevel.ADVANCED;
        }
        else{
            throw new ConvertingException(MemberFieldType.LEVEL.getNameOfField(), "Member Xml Reader");
        }

        return level;
    }

    public static void serializeTo(OutputStream out, Members objectToWrite) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME_MEMBERS);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(objectToWrite, out);
    }
}

