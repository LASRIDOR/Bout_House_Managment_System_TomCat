const IS_MANAGER_KEY = "Is Manager (yes/no)"
const BOAT_HASH_KEY = "Private Boat Hash"
const AGE_KEY = "Age"
const COMMENT_KEY = "Free Comment Place"
const LEVEL_KEY = "Level (Beginner/Intermediate/Advanced)"
const EXPIRY_KEY = "Date Of Expiry (Pattern: YYYY-MM-DDTHH:MM:SS)"
const JOIN_KEY = "Date Of Join (Pattern: YYYY-MM-DDTHH:MM:SS)"
const MEMBER_FIELDS_URL = "/webApp/get member fields"

async function fetchMemberUpdateDetails() {

    const request = new Request(MEMBER_FIELDS_URL, {
        method: 'post',
    })

    const response = await fetch(request)
    const loggedMemberDetails = await response.json()
    removeAllManagerPermission(loggedMemberDetails)
    putOnlyFormInBody(loggedMemberDetails, "Member", "logged member update")
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

