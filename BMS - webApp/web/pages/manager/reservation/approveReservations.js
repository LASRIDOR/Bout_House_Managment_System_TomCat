GET_OPTIONAL_BOATS_TO_ASSIGN = "/webApp/get boats to assign"
APPROVED_RESERVATIONS_URL = "/webApp/approve reservations"

async function fetchManagerApproveReservations(option) {
    fetchManagerDisplayReservations(option)
        .then(() => addReservationNumberTextBox())
}

function addReservationNumberTextBox() {
    const body = document.getElementById("body")
    const container = document.getElementsByClassName("container");
    const reservationNumberDiv = document.createElement("div")
    const submitReservationNumberButton = document.createElement('a')
    let content = document.body.textContent || document.body.innerText;
    let IsThereAreReservationToDisplay = content.indexOf("There are no reservations to display")!==-1;

    if (!IsThereAreReservationToDisplay) {
        reservationNumberDiv.id = "reservationNumberDiv"

        container[0].append(reservationNumberDiv)

        reservationNumberDiv.append(createLabelDetails(RESERVATION_NUMBER_KEY))
        reservationNumberDiv.append(createInputDetails(RESERVATION_NUMBER_KEY, ""))
        reservationNumberDiv.append(submitReservationNumberButton)

        submitReservationNumberButton.className = "btn btn-primary my-2"
        submitReservationNumberButton.id = "submitReservationNumberButton"
        submitReservationNumberButton.innerText = "Submit"
        submitReservationNumberButton.setAttribute("onclick", "fetchManagerApproveReservationAfterGettingReservationNumber()")
    }
}

async function fetchManagerApproveReservationAfterGettingReservationNumber() {
    const reservationNumberKey = document.getElementById(RESERVATION_NUMBER_KEY + 'ID')
    let params = new URLSearchParams()

    params.set("reservationNumber", reservationNumberKey.value)
    const request = new Request(GET_OPTIONAL_BOATS_TO_ASSIGN, {
        method: 'post',
        body: params
    })

    const response = await fetch(request)
    const optionalBoats = await response.json()

    displayOptionalBoats(optionalBoats)
}

function displayOptionalBoats(optionalBoats) {
    const body = document.getElementById("body");
    const container = document.getElementsByClassName("container");
    const htmlContentEl = document.getElementById('about-dialog-text-area');

    displayObjects(optionalBoats, "Optional Boats To Assign To Reservation", "Boat")

    if (htmlContentEl != null) {
        body.removeChild(htmlContentEl)
    }

    // SHOULD BE AN IF OPTIONAL BOATS IS NOT EMPTY
    if (optionalBoats !== null) {
        const boatNumberDiv = document.createElement("div")
        const submitBoatNumberButton = document.createElement('a')

        boatNumberDiv.id = "boatNumberDiv"
        container[1].append(boatNumberDiv)

        boatNumberDiv.append(createLabelDetails(ASSIGNED_BOAT_SERIAL_NUMBER_KEY))
        boatNumberDiv.append(createInputDetails(ASSIGNED_BOAT_SERIAL_NUMBER_KEY, ""))
        boatNumberDiv.append(submitBoatNumberButton)

        submitBoatNumberButton.className = "btn btn-primary my-2"
        submitBoatNumberButton.id = "submitReservationNumberButton"
        submitBoatNumberButton.innerText = "Submit"
        submitBoatNumberButton.setAttribute("onclick", "assignBoatToReservation()")
    }
}