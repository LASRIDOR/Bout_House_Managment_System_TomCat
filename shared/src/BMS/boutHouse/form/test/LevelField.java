package BMS.boutHouse.form.test;

import BMS.boutHouse.form.field.type.LevelRowing;
import BMS.boutHouse.form.field.type.Informable;
import BMS.boutHouse.form.test.exceptions.ConvertingException;

public class LevelField extends Field{

    public LevelField(Informable type, String stringValue) throws ConvertingException, InvalidInputInfoField {
        super(type, stringValue);
    }

    @Override
    public LevelRowing getValue() {
        return (LevelRowing) this.value;
    }

    @Override
    protected LevelRowing makeValueAccordingToType(String stringValue) throws ConvertingException {
        LevelRowing valueLevel;

        if(stringValue.equals(LevelRowing.BEGINNER.getNameOfLevel())){
            valueLevel = LevelRowing.BEGINNER;
        }
        else if(stringValue.equals(LevelRowing.INTERMEDIATE.getNameOfLevel())){
            valueLevel = LevelRowing.INTERMEDIATE;
        }
        else if (stringValue.equals(LevelRowing.ADVANCED.getNameOfLevel())){
            valueLevel = LevelRowing.ADVANCED;
        }
        else{
            throw new ConvertingException("string", "Level Rowing");
        }

        return valueLevel;
    }
}
