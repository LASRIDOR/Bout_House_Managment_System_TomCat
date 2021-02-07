const IS_MANAGER_KEY = "Is Manager (yes/no)"
const BOAT_HASH_KEY = "Private Boat Hash"
const AGE_KEY = "Age"
const COMMENT_KEY = "Free Comment Place"
const LEVEL_KEY = "Level (Beginner/Intermediate/Advanced)"
const EXPIRY_KEY = "Date Of Expiry (Pattern: YYYY-MM-DDTHH:MM:SS)"
const JOIN_KEY = "Date Of Join (Pattern: YYYY-MM-DDTHH:MM:SS)"

async function fetchMemberUpdateDetails(urlOfFields) {
    let params = new URLSearchParams()

    const request = new Request(urlOfFields, {
        method: 'post',
        body:params
    })

    const response = await fetch(request)
    const loggedMemberDetails = await response.json()
    removeAllManagerPermission(loggedMemberDetails)
    createMemberUpdateForm(loggedMemberDetails, "Member")
    changeFormAction()
}

function removeAllManagerPermission(loggedMemberDetails){
    removeElementIfExist(loggedMemberDetails, IS_MANAGER_KEY)
    removeElementIfExist(loggedMemberDetails, BOAT_HASH_KEY)
    removeElementIfExist(loggedMemberDetails, AGE_KEY)
    removeElementIfExist(loggedMemberDetails, COMMENT_KEY)
    removeElementIfExist(loggedMemberDetails, LEVEL_KEY)
    removeElementIfExist(loggedMemberDetails, EXPIRY_KEY)
    removeElementIfExist(loggedMemberDetails, JOIN_KEY)

    return loggedMemberDetails
}

function removeElementIfExist(loggedMemberDetails, keyToRemove){
    delete loggedMemberDetails[keyToRemove];
}

function changeFormAction() {
    const form = document.getElementById("updateForm")

    form.action = "/webApp/logged member update"
}
