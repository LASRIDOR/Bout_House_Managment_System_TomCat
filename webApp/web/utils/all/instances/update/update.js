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
        publishErrorMessage(loggedMemberDetails["ERROR"])
    }
    else{
        putOnlyFormInBody(loggedMemberDetails, boutHouseDataType, "update")
    }
}

function publishErrorMessage(message) {
    const errorArea = document.getElementById('errorMessage')

    errorArea.innerText = message
}