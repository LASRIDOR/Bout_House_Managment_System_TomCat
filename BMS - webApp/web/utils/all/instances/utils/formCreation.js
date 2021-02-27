const DETAILS_CLASS_NAME = "row mt-3"
const DETAILS_DIV_CLASS_NAME = "col-md-12"

function putOnlyFormInBody(loggedMemberDetails = [], boutHouseDataType, action) {
    const body = document.getElementById("body")
    const htmlContentEl = document.getElementById('about-dialog-text-area');

    body.appendChild(createMainForm(loggedMemberDetails , boutHouseDataType, action))

    if (htmlContentEl != null) {
        body.removeChild(htmlContentEl)
    }
}

function createMainForm(loggedMemberDetails = [], boutHouseDataType, action){
    const mainDiv = document.createElement('div')
    const styleSheetEl = document.createElement('link')

    styleSheetEl.rel = "stylesheet"
    styleSheetEl.href = "/webApp/utils/all/instances/utils/formCreation.css"
    mainDiv.className = "container rounded bg-white mt-5 mb-5"
    mainDiv.appendChild(createSubDiv(loggedMemberDetails, boutHouseDataType, action))
    mainDiv.appendChild(styleSheetEl)


    return mainDiv
}

function createSubDiv(loggedMemberDetails = [], boutHouseDataType, action){
    const subMainDiv = document.createElement('div')
    subMainDiv.className = "row"
    subMainDiv.appendChild(createForm(loggedMemberDetails, boutHouseDataType, action))
    return subMainDiv
}

function createForm(loggedMemberDetails = [], boutHouseDataType, action) {
    const profileForm = document.createElement('form')

    profileForm.id = "mainForm"
    //profileForm.className = "col-md-10 border"
    profileForm.name = "mainForm"
    profileForm.action = "/webApp/" + action
    profileForm.method = "post"
    profileForm.appendChild(createFormMainDiv(loggedMemberDetails, boutHouseDataType, action))

    return profileForm
}

function createFormMainDiv(loggedMemberDetails = [], boutHouseDataType, action) {
    const mainFormDiv = document.createElement('div')
    const errorDiv = document.createElement('div')

    errorDiv.id = "errorMessage"
    mainFormDiv.className = "p-3 py-5"
    mainFormDiv.append(createUserFormHeader(boutHouseDataType))
    mainFormDiv.append(createBoutHouseInstanceHiddenInput(boutHouseDataType))
    mainFormDiv.append(createFieldPlace(loggedMemberDetails))
    mainFormDiv.append(createSubmitButtonEl())
    mainFormDiv.append(errorDiv)
    InCaseOfInstanceIdNeeded(mainFormDiv, action)

    return mainFormDiv
}

function createBoutHouseInstanceHiddenInput(boutHouseDataType){
    const inputEl = createInputDetails("BoutHouseDataType", boutHouseDataType)
    inputEl.type = "hidden"
    inputEl.value = boutHouseDataType
    return inputEl
}

function createUserFormHeader(boutHouseDataType) {
    const divHeaderEl = document.createElement('div')
    divHeaderEl.className = "d-flex justify-content-between align-items-center mb-3";
    const headerEl = document.createElement('h4')
    headerEl.className = 'text-right'
    headerEl.innerText = boutHouseDataType + " Details"
    divHeaderEl.append(headerEl)
    return headerEl;
}

function createLabelDetails(name){
    const labelEl = document.createElement('label')

    labelEl.className = "labels"
    labelEl.innerText = name

    return labelEl
}

function createFieldPlace(loggedMemberDetails = []){
    let fieldEl = document.createElement('div')

    fieldEl.className = DETAILS_CLASS_NAME
    Object.keys(loggedMemberDetails).forEach(function (key){
        let fieldDiv = document.createElement('div')
        fieldDiv.id = key
        fieldDiv.className = DETAILS_DIV_CLASS_NAME
        fieldDiv.append(createLabelDetails(key))
        fieldDiv.append(createInputDetails(key, loggedMemberDetails[key]))
        fieldEl.append(fieldDiv)
    })

    return fieldEl
}

function createInputDetails(name, value) {
    let inputEl = document.createElement('input')

    inputEl.name = name
    inputEl.id = name+"ID"
    inputEl.type = "text"
    inputEl.className = "form-control"
    inputEl.placeholder = value

    return inputEl
}

function createSubmitButtonEl(submitFunction) {
    const submitEl = document.createElement('div')
    submitEl.className = "mt-5 text-center"

    const submitButtonEL = document.createElement('button')
    submitButtonEL.className = "btn btn-primary profile-button"
    submitButtonEL.type = "button"
    submitButtonEL.id = "submitButton"
    submitButtonEL.setAttribute('onclick', "submitMainForm()")
    submitButtonEL.innerText = "Save"
    submitEl.append(submitButtonEL)

    return submitButtonEL
}

function InCaseOfInstanceIdNeeded(mainFormDiv, action){
    if (action === "update" || action === "update reservation"){
        const instanceIdInputEl = document.getElementById("instanceId")

        instanceIdInputEl.hidden = true
        mainFormDiv.append(instanceIdInputEl)
    }
}

async function submitMainForm() {
    const updateFormEl = document.forms.namedItem('mainForm');
    const errorDiv = document.getElementById("errorMessage")

    const data = new URLSearchParams();
    let isChecked

    const inputs = document.getElementsByTagName('input');

    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].type.toLowerCase() == 'checkbox') {
            !inputs[i].checked ? isChecked = 'no' : isChecked = 'yes';
            data.append(inputs[i].name, isChecked)
        }
    }

    const allFormsInPage = document.getElementsByName('mainForm')

    for (let i = 0; i < allFormsInPage.length; i++) {
        for (const pair of new FormData(allFormsInPage[i])) {
            if (pair[1] !== "") {
                data.append(pair[0], pair[1]);
            }
        }
    }

    // Only happens on the reservation's form - both reservation form and time window form appear on the page
    if (allFormsInPage.length === 2) {
        updateFormEl.action = '/webApp/create reservation'
    }

    await fetch(updateFormEl.action, {
        method: updateFormEl.method,
        body: data
    })
        .then((response) => response.json())
        .then((message) => errorDiv.innerText = message.message)
}
