async function fetchUpdateReservationDetails() {
    fetchUpdateInstance('Reservation')
        .then(() => removeUpdateReservationNoInputNeededFields())
        .then(() => turnTextInputIntoSelect(NUMBER_OF_ROWERS_KEY, ['1', '2', '3', '4', '5', '6', '7', '8'], "displayNamesOfRowersTextBoxes(this.options[this.selectedIndex].value)"))
        .then(() => turnFilledTextInputIntoDate())
        .then(() => turnNamesOfRowersToTextInputFields())
        .then(() => setOrderOfElements([NAME_OF_ROWER_KEY, DATE_OF_PRACTICE_KEY, BOAT_TYPE_KEY, NUMBER_OF_ROWERS_KEY, NAME_COXSWAIN_KEY]))
        .then(() => checkIfCoxswainNeeded())
        .then(() => setOnKeyUpAttributeToElement(BOAT_TYPE_KEY, "setCoxswainNameFieldToVisible()"))
        .then(() => fetchDisplayAllInstances('Time Window'))
        .then(() => setTimeWindowFieldsAccordingToTimeWindowsDisplayed())
        .then(() => setOnKeyUpToTimeWindowFields())
        .then(() => moveTimeWindowListUp())
}

function turnFilledTextInputIntoDate() {
    const dateOfPracticeInReservation = document.getElementById(DATE_OF_PRACTICE_KEY + 'ID').placeholder;
    const dateSplit = dateOfPracticeInReservation.split('-');
    turnTextInputIntoDate(DATE_OF_PRACTICE_KEY);
    document.getElementById(DATE_OF_PRACTICE_KEY + 'ID').placeholder = dateSplit[2] + '/' + dateSplit[1] + '/' + dateSplit[0]
}

function removeUpdateReservationNoInputNeededFields() {
    if (document.getElementById(ASSIGNED_BOAT_SERIAL_NUMBER_KEY) != null) {
        document.getElementById(ASSIGNED_BOAT_SERIAL_NUMBER_KEY).remove();
    }

    document.getElementById(NAME_OF_RESERVATION_MAKER_KEY).remove();
    document.getElementById(DATE_OF_RESERVATION_KEY).remove();
    document.getElementById(RESERVATION_NUMBER_KEY).remove();
}

function turnNamesOfRowersToTextInputFields() {
    const namesOfRowersDiv = document.getElementById(NAMES_OF_ROWERS_KEY);
    const namesOfRowersString = document.getElementById(NAMES_OF_ROWERS_KEY + 'ID').placeholder;
    const namesOfRowersArray = namesOfRowersString.split(',')
    const nameOfRower = document.getElementById(NAME_OF_ROWER_KEY + 'ID').placeholder;
    document.getElementById(NUMBER_OF_ROWERS_KEY + 'ID').value = namesOfRowersArray.length;

    displayNamesOfRowersTextBoxes(namesOfRowersArray.length);

    for (let i = 0 ; i < namesOfRowersArray.length; i++) {
        if (namesOfRowersArray[i] != nameOfRower) {
            document.getElementById("RowerNo." + (i + 1) + 'ID').placeholder = namesOfRowersArray[i];
            setOnKeyUpAttributeToElement("RowerNo." + (i + 1), "setAllNamesOfRowersFieldsValue()");
        }
    }
}

function setOnKeyUpToTimeWindowFields() {
    if (document.getElementById('allInstances').innerText === "") {
        setOnKeyUpAttributeToElement(TIME_WINDOW_NAME_KEY, "setAllTimeWindowFieldsValue()")
        setOnKeyUpAttributeToElement(ACTIVITY_START_TIME_KEY, "setAllTimeWindowFieldsValue()")
        setOnKeyUpAttributeToElement(ACTIVITY_END_TIME_KEY, "setAllTimeWindowFieldsValue()")
        setOnKeyUpAttributeToElement(OPTIONAL_BOAT_TYPE_KEY, "setAllTimeWindowFieldsValue()")
    }
}
function setAllTimeWindowFieldsValue() {
    const timeWindowName = document.getElementById(TIME_WINDOW_NAME_KEY + 'ID');
    const activityStartTime = document.getElementById(ACTIVITY_START_TIME_KEY + 'ID');
    const activityEndTime = document.getElementById(ACTIVITY_END_TIME_KEY + 'ID');
    const optionalBoatType = document.getElementById(OPTIONAL_BOAT_TYPE_KEY + 'ID');

    timeWindowName.value = timeWindowName.placeholder;
    activityStartTime.value = activityStartTime.placeholder;
    activityEndTime.value = activityEndTime.placeholder;
    optionalBoatType.value = optionalBoatType.placeholder;

    timeWindowName.removeAttribute("onkeyup");
    activityStartTime.removeAttribute("onkeyup");
    activityEndTime.removeAttribute("onkeyup");
    optionalBoatType.removeAttribute("onkeyup");

}
function setAllNamesOfRowersFieldsValue() {
    const numberOfRowers = document.getElementById(NUMBER_OF_ROWERS_KEY + 'ID').value;

    for (let i = 0 ; i < (numberOfRowers - 1); i++) {
        const additionalRowerName = document.getElementById("RowerNo." + (i + 1) + 'ID');

        if (additionalRowerName.value === "") {
            additionalRowerName.value = additionalRowerName.placeholder;
        }

        additionalRowerName.removeAttribute("onkeyup")
    }


}

function checkIfCoxswainNeeded() {
    if (document.getElementById(BOAT_TYPE_KEY + 'ID').placeholder.indexOf('+') != -1) {
        changeElementsVisibility([NAME_COXSWAIN_KEY], true)
    }
    else {
        changeElementsVisibility([NAME_COXSWAIN_KEY], false)
    }
}

function setTimeWindowFieldsAccordingToTimeWindowsDisplayed() {
    const timeWindowOfReservation = document.getElementById(TIME_WINDOW_KEY + 'ID').placeholder;
    const timeWindowSeparated = timeWindowOfReservation.split('-');
    const timeWindowDiv = document.createElement("div");
    timeWindowDiv.id = "timeWindowDiv"
    document.getElementById(TIME_WINDOW_KEY).remove();

    const placeToAppendTimeWindowDiv = document.getElementsByClassName("row mt-3");
    placeToAppendTimeWindowDiv[0].append(timeWindowDiv);
    timeWindowDiv.append(createLabelDetails(TIME_WINDOW_NAME_KEY))
    timeWindowDiv.append(createInputDetails(TIME_WINDOW_NAME_KEY, timeWindowSeparated[2].trim()))

    // Meaning there are no time windows to choose from
    if (document.getElementById('allInstances').innerText === "") {
        timeWindowDiv.append(createLabelDetails(ACTIVITY_START_TIME_KEY))
        timeWindowDiv.append(createInputDetails(ACTIVITY_START_TIME_KEY, timeWindowSeparated[0].trim()))

        timeWindowDiv.append(createLabelDetails(ACTIVITY_END_TIME_KEY))
        timeWindowDiv.append(createInputDetails(ACTIVITY_END_TIME_KEY, timeWindowSeparated[1].trim()))

        timeWindowDiv.append(createLabelDetails(OPTIONAL_BOAT_TYPE_KEY))

        if (timeWindowSeparated.length === 4) {
            timeWindowDiv.append(createInputDetails(OPTIONAL_BOAT_TYPE_KEY, timeWindowSeparated[3].trim()))
        }
        else {
            timeWindowDiv.append(createInputDetails(OPTIONAL_BOAT_TYPE_KEY, ""))
        }
    }
}

function moveTimeWindowListUp() {
    const timeWindowContainer = document.getElementsByTagName("main")[0];
    if(timeWindowContainer.previousElementSibling)
        timeWindowContainer.parentNode.insertBefore(timeWindowContainer, timeWindowContainer.previousElementSibling);
}