DISPLAY_RESERVATIONS_URL = "/webApp/display reservations"

async function fetchManagerDisplayReservations(option) {
    const dateIDEl = document.getElementById("dateId")
    let params = new URLSearchParams()

    params.set("Display Reservations option", option)

    if (dateIDEl != null) {
        params.set("dateId", dateIDEl.value)
    }

    const request = new Request(DISPLAY_RESERVATIONS_URL, {
        method: 'post',
        body: params
    })

    const response = await fetch(request)
    const allReservations = await response.json()

    displayObjects(allReservations, option.replace("display", "").replace("Button", ""), "Reservation")
}

/*

<main class="container">

    <div class="my-3 p-3 bg-white rounded shadow-sm">
        <h6 class="border-bottom pb-2 mb-0">Recent updates</h6>
        <div class="d-flex text-muted pt-3">

            <text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text>
 */

function insertToBodyDisplayReservations(allReservations) {
    const body = document.getElementById("body")
    const htmlContentEl = document.getElementById('about-dialog-text-area');

    body.appendChild(createMainDisplayAllDisplayReservations(allReservations))
    body.removeChild(htmlContentEl)
}

function createMainDisplayAllDisplayReservations(allReservations){
    const mainEl = document.createElement('main')

    mainEl.className = "container"
    mainEl.appendChild(createMainDivDisplayReservations(allReservations))

    return mainEl
}

function createMainDivDisplayReservations(allReservations){
    const mainDiv = document.createElement('div')

    mainDiv.className = "my-3 p-3 bg-white rounded shadow-sm"
    mainDiv.appendChild(createHeaderOfMainDisplayReservations())
    mainDiv.appendChild(createDivOfTextDisplayReservations(allReservations))

    return mainDiv
}

function createHeaderOfMainDisplayReservations(){
    const headerEl = document.createElement('h6')

    headerEl.className = "border-bottom pb-2 mb-0"
    headerEl.innerText = "Reservations List"

    return headerEl
}

function createDivOfTextDisplayReservations(allReservations){
    const textEl = document.createElement('text')

    textEl.innerText = allReservations.message

    return textEl
}