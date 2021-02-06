const DETAILS_CLASS_NAME = "row mt-3"
const DETAILS_DIV_CLASS_NAME = "col-md-12"

async function fetchUpdateDetails(urlOfFields, boutHouseDataType) {
    let params = new URLSearchParams()

    params.set("BoutHouseDataType", boutHouseDataType)
    const request = new Request(urlOfFields, {
        method: 'post',
        body:params
    })

    const response = await fetch(request)
    const loggedMemberDetails = await response.json()
    createMemberUpdateForm(loggedMemberDetails, boutHouseDataType)
}

function createMemberUpdateForm(loggedMemberDetails = [], boutHouseDataType) {
    const body = document.getElementById("body")
    const htmlContentEl = document.getElementById('about-dialog-text-area');
    const mainDiv = document.createElement('div')

    body.removeChild(htmlContentEl)
    mainDiv.className = "container rounded bg-white mt-5 mb-5"
    mainDiv.appendChild(createSubDiv(loggedMemberDetails, boutHouseDataType))
    body.appendChild(mainDiv)
}

function createSubDiv(loggedMemberDetails = [], boutHouseDataType){
    const subMainDiv = document.createElement('div')
    subMainDiv.className = "row"
    subMainDiv.appendChild(createForm(loggedMemberDetails, boutHouseDataType))
    return subMainDiv
}

function createForm(loggedMemberDetails = [], boutHouseDataType) {
    const profileForm = document.createElement('form')

    profileForm.className = "col-md-5 border"
    profileForm.action = "update"
    profileForm.method = "post"
    profileForm.appendChild(createMainFormDiv(loggedMemberDetails, boutHouseDataType))

    return profileForm
}

function createMainFormDiv(loggedMemberDetails = [], boutHouseDataType) {
    const mainFormDiv = document.createElement('div')

    mainFormDiv.className = "p-3 py-5"
    mainFormDiv.append(createUserFormHeader(boutHouseDataType))
    mainFormDiv.append(createBoutHouseInstanceHiddenInput(boutHouseDataType))
    mainFormDiv.append(createFieldPlace(loggedMemberDetails))
    mainFormDiv.append(createSubmitButtonEl())

    return mainFormDiv
}

function createBoutHouseInstanceHiddenInput(boutHouseDataType){
    const inputEl = createInputDetails("BoutHouseDataType", boutHouseDataType)
    inputEl.type = "hidden"
    return inputEl
}

function createUserFormHeader(boutHouseDataType) {
    const divHeaderEl = document.createElement('div')
    divHeaderEl.className = "d-flex justify-content-between align-items-center mb-3";
    const headerEl = document.createElement('h4')
    headerEl.className = 'text-right'
    headerEl.innerText = boutHouseDataType
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
    let usernameDetailsEl = document.createElement('div')

    usernameDetailsEl.className = DETAILS_CLASS_NAME
    Object.keys(loggedMemberDetails).forEach(function (key){
        let fieldDiv = document.createElement('div')
        fieldDiv.id = key
        fieldDiv.className = DETAILS_DIV_CLASS_NAME
        fieldDiv.append(createLabelDetails(key))
        fieldDiv.append(createInputDetails(key, loggedMemberDetails[key]))
        usernameDetailsEl.append(fieldDiv)
    })

    return usernameDetailsEl
}

function createInputDetails(name, value) {
    let inputEl = document.createElement('input')

    inputEl.name = name
    inputEl.type = "text"
    inputEl.className = "form-control"
    inputEl.placeholder = value

    return inputEl
}
/*
function createMemberEmailUserNameEl(loggedMemberDetails) {
    let usernameDetailsEl = document.createElement('div')
    usernameDetailsEl.className = USERNAME_EMAIL_DETAILS_CLASS_NAME

    let usernameDiv = document.createElement('div')
    usernameDiv.className = USERNAME_EMAIL_DETAILS_DIV_CLASS_NAME
    usernameDiv.append(createLabelDetails(USERNAME))
    let usernameInput = createInputDetails(USERNAME)
    usernameInput.placeholder = loggedMemberDetails.username
    usernameDiv.append(usernameInput)
    usernameDetailsEl.append(usernameDiv)

    let emailDiv = document.createElement('div')
    emailDiv.className = USERNAME_EMAIL_DETAILS_DIV_CLASS_NAME
    emailDiv.append(createLabelDetails(EMAIL))
    let emailInput = createInputDetails(EMAIL)
    emailInput.placeholder = loggedMemberDetails.email
    emailDiv.append(emailInput)
    usernameDetailsEl.append(emailDiv)

    return usernameDetailsEl
}

function createMemberPhonePasswordEl(loggedMemberDetails) {
    let phonePasswordEL = document.createElement('div')
    phonePasswordEL.className = PHONE_PASSWORD_DETAILS_CLASS_NAME

    let phoneNumberDiv = document.createElement('div')
    phoneNumberDiv.className = PHONE_PASSWORD_DETAILS_DIV_CLASS_NAME
    phoneNumberDiv.append(createLabelDetails(PHONE_NUMBER))
    let inputEl = createInputDetails(PHONE_NUMBER)
    inputEl.placeholder = loggedMemberDetails.phoneNumber;
    phoneNumberDiv.append(inputEl)
    phonePasswordEL.append(phoneNumberDiv)

    let passwordDiv = document.createElement('div')
    passwordDiv.className = PHONE_PASSWORD_DETAILS_DIV_CLASS_NAME
    passwordDiv.append(createLabelDetails(PASSWORD))
    let passwordInput = createInputDetails(PASSWORD)
    passwordInput.placeholder = loggedMemberDetails.password
    passwordDiv.append(passwordInput)
    phonePasswordEL.append(passwordDiv)

    return phonePasswordEL
}
*/

function createSubmitButtonEl() {
    const submitEl = document.createElement('div')
    submitEl.className = "mt-5 text-center"

    const submitButtonEL = document.createElement('button')
    submitButtonEL.className = "btn btn-primary profile-button"
    submitButtonEL.type = "submit"
    submitButtonEL.innerText = "Save"
    submitEl.append(submitButtonEL)

    return submitButtonEL
}



// if users === undefined then it will have the default value of an empty array


/*


<div class="container rounded bg-white mt-5 mb-5">
    <div class="row">
        <form class="col-md-5 border" action="../../../member update" method="post">
            <div class="p-3 py-5" id="memberUpdateForm">



                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h4 class="text-right">Profile Settings</h4>
                </div>
                <div class="row mt-2">
                    <div class="col-md-6">
                    <label class="labels">Surname</label>
                    <input name="Username" type="text" class="form-control" placeholder="surname">
                </div>
                    <div class="col-md-6"><label class="labels">Email</label><input name="Email" type="text" class="form-control" placeholder="enter email"></div>
                </div>
                <div class="row mt-3">
                    <div class="col-md-12">
                        <label class="labels">PhoneNumber</label>
                        <input type="text" class="form-control" placeholder="enter phone number" name="Phone Number (05X-XXXXXXX)">
                    </div>
                    <div class="col-md-12">
                        <label class="labels">Password</label>
                        <input type="text" class="form-control" placeholder="enter password" name="Password">
                    </div>
                </div>
                <div class="mt-5 text-center"><button class="btn btn-primary profile-button" type="submit">Save Profile</button></div>
                </div>


            </div>
        </form>

    </div>
</div>



 */

//activate the timer calls after the page is loaded