async function fetchBoatUpdateDetails() {
        fetchUpdateInstance('Time Window')
            .then(() => turnFilledTextInputIntoCheckbox())
            .then(() => setOrderOfElements([SERIAL_NUMBER_KEY, BOAT_NAME_KEY, BOAT_TYPE_KEY, IS_BOAT_PRIVATE_KEY, IS_BOAT_DISABLED_KEY]))
}

function turnFilledTextInputIntoCheckbox() {
        turnTextInputIntoCheckBox(IS_BOAT_PRIVATE_ID_KEY, "15px");
        turnTextInputIntoCheckBox(IS_BOAT_DISABLED_ID_KEY, "6.5px");

        let isBoatDisabledId = document.getElementById(IS_BOAT_DISABLED_ID_KEY);
        isBoatDisabledId.placeholder === 'true' ? isBoatDisabledId.checked = true : isBoatDisabledId.checked = false;

        let isBoatPrivateID = document.getElementById(IS_BOAT_PRIVATE_ID_KEY);
        isBoatPrivateID.placeholder === 'true' ? isBoatPrivateID.checked = true : isBoatDisabledId.checked = false;
}


