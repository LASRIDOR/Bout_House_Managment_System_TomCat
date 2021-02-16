DELETE_INSTANCE_URL = "/webApp/delete"

async function fetchDeleteInstance(boutHouseDataType) {
    const idEl = document.getElementById("instanceId")
    let params = new URLSearchParams()

    params.set("instanceId", idEl.value)
    params.set("BoutHouseDataType", boutHouseDataType)

    const request = new Request(DELETE_INSTANCE_URL, {
        method: 'post',
        body: params
    })

    const response = await fetch(request)
    const message = await response.json()
    publishErrorMessage(message)
}

function publishErrorMessage(message) {
    const errorArea = document.getElementById('errorMessage')

    errorArea.innerText = message.message
}