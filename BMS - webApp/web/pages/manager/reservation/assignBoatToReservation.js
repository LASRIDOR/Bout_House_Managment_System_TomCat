ASSIGN_BOAT_TO_RESERVATION_URL = "/webApp/assign boat to reservation"

async function assignBoatToReservation() {
    const reservationNumberIDEl = document.getElementById(RESERVATION_NUMBER_KEY + 'ID')
    const assignedBoatIDEl = document.getElementById(ASSIGNED_BOAT_SERIAL_NUMBER_KEY + 'ID')
    let params = new URLSearchParams()

    params.set("reservationNumber", reservationNumberIDEl.value)
    params.set("assignedBoat", assignedBoatIDEl.value)

    const request = new Request(ASSIGN_BOAT_TO_RESERVATION_URL, {
        method: 'post',
        body: params
    })

    const response = await fetch(request)
    const message = await response.json()
    publishErrorMessage(message)

    function publishErrorMessage(message) {
        const body = document.getElementById("body")
        const errorArea = document.createElement("div")

        body.append(errorArea)
        errorArea.id = "errorMessage"
        errorArea.innerText = message.message
    }
}