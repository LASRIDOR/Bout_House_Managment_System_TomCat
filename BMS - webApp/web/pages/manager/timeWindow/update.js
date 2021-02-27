async function fetchUpdateTimeWindowDetails() {
    fetchUpdateInstance('Time Window')
        .then(() => setOrderOfElements([TIME_WINDOW_NAME_KEY, ACTIVITY_START_TIME_KEY, ACTIVITY_END_TIME_KEY, OPTIONAL_BOAT_TYPE_KEY]))
        .then(() => turnFilledTextInputIntoTime())
}

function turnFilledTextInputIntoTime() {
    turnTextInputIntoTime(ACTIVITY_START_TIME_KEY + 'ID');
    turnTextInputIntoTime(ACTIVITY_END_TIME_KEY + 'ID');

    document.getElementById(ACTIVITY_START_TIME_KEY + 'ID').value = document.getElementById(ACTIVITY_START_TIME_KEY + 'ID').placeholder;
    document.getElementById(ACTIVITY_END_TIME_KEY + 'ID').value = document.getElementById(ACTIVITY_END_TIME_KEY + 'ID').placeholder;

}

