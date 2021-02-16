GET_EMPTY_FIELDS_URL = '/webApp/field of type'

async function fetchCreateInstance(boutHouseDataType) {
    let params = new URLSearchParams()
    params.set("BoutHouseDataType", boutHouseDataType)

    const request = new Request(GET_EMPTY_FIELDS_URL, {
        method: 'post',
        body: params
    })

    const response = await fetch(request)
    const instanceEmptyFields = await response.json()

    putOnlyFormInBody(instanceEmptyFields, boutHouseDataType, "create")
}