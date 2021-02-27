GET_EMPTY_FIELDS_URL = '/webApp/field of type'
CREATE_RESERVATION_URL = '/webApp/create reservation'

async function fetchCreateInstance(boutHouseDataType) {
    let params = new URLSearchParams()
    params.set("BoutHouseDataType", boutHouseDataType)

    const request = new Request(GET_EMPTY_FIELDS_URL, {
        method: 'post',
        body: params
    })

    const response = await fetch(request)
    const instanceEmptyFields = await response.json()

    if (boutHouseDataType === 'Reservation') {
        putOnlyFormInBody(instanceEmptyFields, boutHouseDataType, "create reservation")
    }
    else {
        putOnlyFormInBody(instanceEmptyFields, boutHouseDataType, "create")
    }
}