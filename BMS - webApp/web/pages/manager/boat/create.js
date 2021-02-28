// THIS FUNCTION IS FETCHING THE CREATE INSTANCE FIELDS AND THEN CHANGES TWO OF THEM (THE ONES THAT ARE CHECKABLE)
// FROM INPUT TYPE 'TEXT' TO 'CHECKBOX'
const SERIAL_NUMBER_KEY = "Serial Number"
const BOAT_NAME_KEY = "Boat Name"
const BOAT_TYPE_KEY = "Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal) i.e. 2- Wide Coastal"
const IS_BOAT_PRIVATE_KEY = "Is boat private?"
const IS_BOAT_DISABLED_KEY = "Is boat disabled?"
const IS_BOAT_PRIVATE_ID_KEY = "Is boat private?ID"
const IS_BOAT_DISABLED_ID_KEY = "Is boat disabled?ID"

// THE setOrderOfElements and turnTextInputIntoCheckBox METHODS ARE BOTH LOCATED UNDER utils/all/instances/utils/boatFieldsDisplay.js
function fetchManagerCreateBoatInstance(){
    fetchCreateInstance('Boat')
        .then(() => setElementsRequiredToTrue([SERIAL_NUMBER_KEY, BOAT_NAME_KEY, BOAT_TYPE_KEY, IS_BOAT_PRIVATE_KEY, IS_BOAT_DISABLED_KEY]))
        .then(() => document.getElementsByClassName("row mt-3").id = 'Boat-fields')
        .then(() => setOrderOfElements([SERIAL_NUMBER_KEY, BOAT_NAME_KEY, BOAT_TYPE_KEY, IS_BOAT_PRIVATE_KEY, IS_BOAT_DISABLED_KEY]))
        .then(() => turnTextInputIntoCheckBox(IS_BOAT_PRIVATE_ID_KEY, "15px"))
        .then(() => turnTextInputIntoCheckBox(IS_BOAT_DISABLED_ID_KEY, "6.5px"))
}