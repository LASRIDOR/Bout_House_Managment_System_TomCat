const GET_ALL_FIELDS_URL = "/webApp/get all fields"

async function fetchUpdateInstance(boutHouseDataType) {
    const idEl = document.getElementById("instanceId")
    let params = new URLSearchParams()

    params.set("instanceId", idEl.value)
    params.set("BoutHouseDataType", boutHouseDataType)
    const request = new Request(GET_ALL_FIELDS_URL, {
        method: 'post',
        body:params
    })

    const response = await fetch(request)
    const loggedMemberDetails = await response.json()

    if (Object.keys(loggedMemberDetails)[0] === "ERROR"){
        publishError(loggedMemberDetails["ERROR"])
    }
    else{
        if (boutHouseDataType == "Reservation") {
            putOnlyFormInBody(loggedMemberDetails, boutHouseDataType, "update reservation")
        }
        else {
            putOnlyFormInBody(loggedMemberDetails, boutHouseDataType, "update")
        }
    }
}

function publishError(message) {
    const errorArea = document.getElementById('errorMessage')

    errorArea.innerText = message
}