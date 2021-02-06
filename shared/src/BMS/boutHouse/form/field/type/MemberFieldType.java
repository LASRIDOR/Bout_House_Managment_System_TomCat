package BMS.boutHouse.form.field.type;

public enum MemberFieldType implements Informable {
    USERNAME("Username", "^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$"),
    PASSWORD("Password", "^[A-Za-z0-9_!@#$%^&*]{4,}$"),
    AGE("Age", "^(1[89]|[2-9]\\d)$"),
    PHONE_NUMBER("Phone Number (05X-XXXXXXX)", "05[0-9]{1}-[0-9]{7}"),
    EMAIL("Email", "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$"),
    DATE_OF_JOIN("Date Of Join (Pattern: YYYY-MM-DDTHH:MM:SS)", "^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})$"),
    DATE_OF_EXPIRY("Date Of Expiry (Pattern: YYYY-MM-DDTHH:MM:SS)", "^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})$"),
    LEVEL("Level (Beginner/Intermediate/Advanced)", String.format("^(%s|%s|%s)", LevelRowing.BEGINNER.getNameOfLevel(), LevelRowing.INTERMEDIATE.getNameOfLevel(), LevelRowing.ADVANCED.getNameOfLevel())),
    PRIVATE_BOAT_HASH("Private Boat Hash", "^\\d+$"),
    FREE_COMMENT_PLACE("Free Comment Place", "^[a-zA-Z0-9_ ]*$"),
    IS_MANAGER("Is Manager (yes/no)", "^(?:Yes|No|yes|no|NO|YES)");
    private String nameOfField;
    private String regexPattern;

    MemberFieldType(String nameOfField, String regexPattern) {
        this.nameOfField = nameOfField;
        this.regexPattern = regexPattern;
    }

    @Override
    public String getNameOfField() {
        return nameOfField;
    }

    @Override
    public String getRegexPattern() {
        return this.regexPattern;
    }

    @Override
    public String toString() {
        return "MemberFieldType{" +
                "nameOfField='" + nameOfField + '\'' +
                '}';
    }
}
