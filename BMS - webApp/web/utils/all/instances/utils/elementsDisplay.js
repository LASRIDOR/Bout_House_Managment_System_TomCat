function turnTextInputIntoCheckBox(elementID, marginLeftPixels) {
    let element = document.getElementById(elementID)
    element.type = 'checkbox'
    element.value = 'yes'
    element.className = 'form-check-input'
    element.style.marginLeft = marginLeftPixels
}

function setOrderOfElements(elementsArray) {
    let i;

    for (i = 0; i < elementsArray.length; i++) {
        document.getElementById(elementsArray[i]).style.order = i;
    }
}

function turnTextInputIntoDate(elementID) {
    let element = document.getElementById(elementID + 'ID')
    element.type = 'date'
}

function turnTextInputIntoTime(elementID) {
    let element = document.getElementById(elementID + 'ID')
    element.type = 'time'
}

function setElementsRequiredToTrue(elementsArray) {
    let i;

    for (i = 0; i < elementsArray.length; i++) {
        document.getElementById(elementsArray[i] + 'ID').required = true;
    }
}

function turnTextInputIntoSelect(elementID, optionsList, onChangeFunction) {
    document.getElementById(elementID + 'ID').remove();
    let element = document.createElement("select");
    let parentElement = document.getElementById(elementID);

    element.id = elementID + 'ID'
    element.setAttribute("onchange", onChangeFunction)
    parentElement.appendChild(document.createElement("br"))
    parentElement.appendChild(element)

    for (let i = 0; i < optionsList.length; i++) {
        let option = document.createElement("option");
        option.value = optionsList[i];
        option.text = optionsList[i];
        element.appendChild(option);
    }
}

function changeElementsVisibility(elementsArray, toShow) {
    const visibility = toShow ? 'visible' : 'hidden'

    for (let i = 0; i < elementsArray.length; i++) {
        document.getElementById(elementsArray[i]).style.visibility = visibility;
    }
}