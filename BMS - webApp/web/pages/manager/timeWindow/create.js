// THIS FUNCTION IS FETCHING THE CREATE INSTANCE FIELDS AND THEN CHANGES TWO OF THEM (THE ONES THAT ARE CHECKABLE)
// FROM INPUT TYPE 'TEXT' TO 'CHECKBOX'

const TIME_WINDOW_NAME_KEY = "Time Window Name"
const ACTIVITY_START_TIME_KEY = "Activity Start Time"
const ACTIVITY_END_TIME_KEY = "Activity End Time"
const OPTIONAL_BOAT_TYPE_KEY = "(Optional) Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) " +
    "(Narrow/Wide) (FlatWater/Coastal)\ni.e. 2- Wide Coastal"
const TIME_WINDOW_NAME_ID_KEY = "Time Window NameID"


// THE setOrderOfElements and turnTextInputIntoCheckBox METHODS ARE BOTH LOCATED UNDER utils/all/instances/utils/boatFieldsDisplay.js
async function fetchManagerCreateTimeWindowInstance(){
    await fetchCreateInstance('Time Window')
        .then(() => setElementsRequiredToTrue([TIME_WINDOW_NAME_KEY, ACTIVITY_START_TIME_KEY, ACTIVITY_END_TIME_KEY]))
        .then(() => setOrderOfElements([TIME_WINDOW_NAME_KEY, ACTIVITY_START_TIME_KEY, ACTIVITY_END_TIME_KEY, OPTIONAL_BOAT_TYPE_KEY]))
        .then(() => turnTextInputIntoTime(ACTIVITY_START_TIME_KEY))
        .then(() => turnTextInputIntoTime(ACTIVITY_END_TIME_KEY))
}