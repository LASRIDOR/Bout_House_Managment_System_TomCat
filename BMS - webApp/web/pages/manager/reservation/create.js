GET_EMPTY_FIELDS_URL = '/webApp/field of type'
GET_TIME_WINDOW_URL = '/webApp/get time windows'

const RESERVATION_NUMBER_KEY = "Reservation Number"
const NAME_OF_ROWER_KEY = "Name of Rower"
const DATE_OF_PRACTICE_KEY = "Date of Practice"
const TIME_WINDOW_KEY = "Time window"
const BOAT_TYPE_KEY = "Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal) i.e. 2- Wide Coastal"
const NAME_OF_RESERVATION_MAKER_KEY = "Name of Reservation Maker"
const DATE_OF_RESERVATION_KEY = "Date of Reservation"
const NAMES_OF_ROWERS_KEY = "Names of Rowers"
const NAME_COXSWAIN_KEY = "Coxswain name (if coxswain has not been picked, type \"null\")"
const NUMBER_OF_ROWERS_KEY = "Number of rowers (including the lead rower)"
const ASSIGNED_BOAT_SERIAL_NUMBER_KEY = "Assigned boat serial number"

async function fetchReservationEmptyFields() {
    await fetchDisplayAllInstances('Time Window')
        .then(() => fetchManagerCreateTimeWindowInstance())
        .then(() => setTimeWindowFormAccordingToTimeWindowsDisplayed())
        .then(() => fetchReservationCreateInstance())
}

function fetchReservationCreateInstance() {
    fetchCreateInstance('Reservation')
        .then(() => removeCreateReservationNoInputNeededFields())
        .then(() => changeElementsVisibility([NAME_COXSWAIN_KEY], false))
        .then(() => setElementsRequiredToTrue([NAME_OF_ROWER_KEY, DATE_OF_PRACTICE_KEY, BOAT_TYPE_KEY, NAME_COXSWAIN_KEY, NUMBER_OF_ROWERS_KEY]))
        .then(() => turnTextInputIntoDate(DATE_OF_PRACTICE_KEY))
        .then(() => turnTextInputIntoSelect(NUMBER_OF_ROWERS_KEY, ['1', '2', '3', '4', '5', '6', '7', '8'], "displayNamesOfRowersTextBoxes(this.options[this.selectedIndex].value)"))
        .then(() => setOnKeyUpAttributeToElement(BOAT_TYPE_KEY, "setCoxswainNameFieldToVisible()"))
        .then(() => document.getElementById("errorMessage").remove())
        .then(() => setOrderOfElements([NAME_OF_ROWER_KEY, DATE_OF_PRACTICE_KEY, BOAT_TYPE_KEY, NUMBER_OF_ROWERS_KEY, NAME_COXSWAIN_KEY]))
}
function removeCreateReservationNoInputNeededFields() {
    document.getElementById(ASSIGNED_BOAT_SERIAL_NUMBER_KEY).remove();
    document.getElementById(TIME_WINDOW_KEY).remove();
    document.getElementById(NAME_OF_RESERVATION_MAKER_KEY).remove();
    document.getElementById(DATE_OF_RESERVATION_KEY).remove();
    document.getElementById(RESERVATION_NUMBER_KEY).remove();
    document.getElementById(NAMES_OF_ROWERS_KEY).remove();
}

function displayNamesOfRowersTextBoxes(numberOfRowersSelected) {
    const numberOfRowersEl = document.getElementById(NUMBER_OF_ROWERS_KEY)

    if (document.getElementById(NAMES_OF_ROWERS_KEY) != null) {
        document.getElementById(NAMES_OF_ROWERS_KEY).remove();
    }

    const namesOfRowersEl = document.createElement("div")

    namesOfRowersEl.id = NAMES_OF_ROWERS_KEY;
    numberOfRowersEl.append(namesOfRowersEl)

    for (let i = 1; i < numberOfRowersSelected; i++) {
        const namesOfRowersDiv = document.createElement("div")
        namesOfRowersDiv.id = "Rower no. " + i
        namesOfRowersEl.append(namesOfRowersDiv)

        namesOfRowersDiv.append(createLabelDetails("Rower no. " + i))

        let inputElement = createInputDetails("RowerNo." + i, "")
        inputElement.required = true
        namesOfRowersDiv.append(inputElement)
    }
}
function setOnKeyUpAttributeToElement(elementKey, functionToSet) {
    const element = document.getElementById(elementKey + 'ID')
    element.setAttribute("onkeyup", functionToSet)
}

function setCoxswainNameFieldToVisible() {
    if (document.getElementById(BOAT_TYPE_KEY + 'ID').value.indexOf('+') != -1) {
        changeElementsVisibility([NAME_COXSWAIN_KEY], true)
    }
    else {
        changeElementsVisibility([NAME_COXSWAIN_KEY], false)
    }
}

function setTimeWindowFormAccordingToTimeWindowsDisplayed() {
    document.getElementById("submitButton").remove()

    if (document.getElementById('allInstances').innerText != "") {
        document.getElementById("Activity Start Time").remove()
        document.getElementById("Activity End Time").remove()
        document.getElementById("(Optional) Boat Type: (1X/2-/2+/2X/2X+/4+/4-/4X+/4X/8X+/8+) (Narrow/Wide) (FlatWater/Coastal) i.e. 2- Wide Coastal").remove()
    }
}

