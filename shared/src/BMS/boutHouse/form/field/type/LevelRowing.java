package BMS.boutHouse.form.field.type;

public enum LevelRowing {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced");

    private String nameOfLevel;

    LevelRowing(String nameOfLevel) {
        this.nameOfLevel = nameOfLevel;
    }

    public String getNameOfLevel() {
        return nameOfLevel;
    }

    @Override
    public String toString() {
        return nameOfLevel;
    }
}
