const NOTIFY_MEMBER_ACTION = "notify member";
const NOTIFY_ALL_MEMBERS_ACTION = "notify all members";

/*

function createNotificationFormForMember(){
    const memberToNotify = document.getElementById("instanceId");
    memberToNotify.hidden = true;
    replacePageContentToNotificationForm("notify member")

    const body = document.getElementById("notificationForm")
    body.append(memberToNotify)
}

function createNotificationFormForAllMember(){
    replacePageContentToNotificationForm("notify all members")
}

 */

function replacePageContentToNotificationForm(action){
    const pageContent = document.getElementById("page-content")
    const htmlContentEl = document.getElementById('about-dialog-text-area');

    pageContent.appendChild(createNotificationForm(action))
    pageContent.removeChild(htmlContentEl)
    fetchCheckingForInstanceExisting();
}

function createNotificationForm(action){
    const notificationForm = document.createElement('form')
    const centerDiv = document.createElement("div")

    centerDiv.align = "center"
    notificationForm.id = "notificationForm"
    notificationForm.className = "col-md-5 border"
    notificationForm.name = "notificationForm"
    notificationForm.action = "/webApp/" + action
    notificationForm.method = "post"
    notificationForm.appendChild(createNotificationFormMainDiv(action))
    notificationForm.append(createMessageArea())
    centerDiv.append(notificationForm)

    return centerDiv
}

function createMessageArea(){
    const messageDiv = document.createElement('div')

    messageDiv.id = "errorMessage"

    return messageDiv
}

function createNotificationFormMainDiv(action){
    const mainDiv = document.createElement('div')
    const errorDiv = document.createElement('div')

    errorDiv.id = "errorMessage"
    let notificationInput = []
    notificationInput.header = ""
    notificationInput.message = ""

    mainDiv.className = "p-3 py-5"
    mainDiv.append(createNotificationHeader())
    mainDiv.append(createNotificationFieldPlace(notificationInput, action))

    mainDiv.append(createSubmitNotificationButton())
    mainDiv.append(errorDiv)
    return mainDiv
}

function createInstanceIdInput(){
    const divOfInput = document.createElement('div')
    const labelOfInstance = document.createElement('label')
    const inputEl = document.createElement('input')

    divOfInput.id = "email"
    divOfInput.className = "col-md-12"
    labelOfInstance.className = "labels" +
        ""
    labelOfInstance.innerText = "Email"
    inputEl.type  = "text"
    inputEl.id = "instanceId"
    inputEl.name = "Email"
    inputEl.className = "form-control"
    inputEl.placeholder = ""
    divOfInput.append(labelOfInstance)
    divOfInput.append(inputEl)

    return divOfInput
}

function createNotificationHeader(){
    const divHeaderEl = document.createElement('div')
    divHeaderEl.className = "d-flex justify-content-between align-items-center mb-3";
    const headerEl = document.createElement('h4')
    headerEl.className = 'text-right'
    headerEl.innerText = "Notification Details"
    divHeaderEl.append(headerEl)
    return headerEl;
}


function createNotificationFieldPlace(notificationInput = [], action){
    let fieldEl = document.createElement('div')

    fieldEl.className = DETAILS_CLASS_NAME
    Object.keys(notificationInput).forEach(function (key){
        let fieldDiv = document.createElement('div')
        fieldDiv.id = key
        fieldDiv.className = DETAILS_DIV_CLASS_NAME
        fieldDiv.append(createLabelNotificationInput(key))
        fieldDiv.append(createNotificationInputDetails(key, notificationInput[key]))
        fieldEl.append(fieldDiv)
    })

    if (action === NOTIFY_MEMBER_ACTION){
        fieldEl.append(createInstanceIdInput())
    }

    return fieldEl
}

function createLabelNotificationInput(name){
    const labelEl = document.createElement('label')
    labelEl.className = "labels"
    labelEl.innerText = name

    return labelEl
}

function createNotificationInputDetails(name, value) {
    let inputEl = document.createElement('input')

    inputEl.name = name
    inputEl.id = name+"Id"
    inputEl.type = "text"
    inputEl.className = "form-control"
    inputEl.placeholder = value

    return inputEl
}

function createSubmitNotificationButton() {
    const submitEl = document.createElement('div')
    submitEl.className = "mt-5 text-center"

    const submitButtonEL = document.createElement('button')
    submitButtonEL.className = "btn btn-primary profile-button"
    submitButtonEL.type = "button"
    submitButtonEL.setAttribute('onclick', "submitNotificationForm()")
    submitButtonEL.innerText = "Save"
    submitEl.append(submitButtonEL)

    return submitButtonEL
}

async function submitNotificationForm() {
    const notificationFormEl = document.forms.namedItem('notificationForm');
    const errorDiv = document.getElementById("errorMessage")

    const data = new URLSearchParams();
    for (const pair of new FormData(notificationFormEl)) {
        if (pair[1] !== ""){
            data.append(pair[0], pair[1]);
        }
    }

    await fetch(notificationFormEl.action, {
        method: notificationFormEl.method,
        body: data
    })
        .then((response) => response.json())
        .then((message) => errorDiv.innerText = message.message)
        .then(() => {cleanInputsValues()})
}

function cleanInputsValues(){
    const allInput = document.forms["notificationForm"].getElementsByTagName('input');

    for (let i = 0; i < allInput.length; i++) {
        allInput[i].value = ""
    }
}