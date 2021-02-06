const FUTURE_RESERVATION_URL = '../../future reservation'

async function fetchWeeklyReservation() {
    const response = await fetch(FUTURE_RESERVATION_URL)
    const futureReservation = await response.json();
    displayFutureReservation(futureReservation)
}

/*
<main class="container">

    <div class="my-3 p-3 bg-white rounded shadow-sm">
        <h6 class="border-bottom pb-2 mb-0">Recent updates</h6>
        <div class="d-flex text-muted pt-3">
            <svg class="bd-placeholder-img flex-shrink-0 me-2 rounded" width="32" height="32" xmlns="http://www.w3.org/2000/svg" role="img"
            aria-label="Placeholder: 32x32" preserveAspectRatio="xMidYMid slice" focusable="false">
                <title>Placeholder</title>
                <rect width="100%" height="100%" fill="#007bff"/>
                <text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text>
            </svg>

            <p class="pb-3 mb-0 small lh-sm border-bottom">
                <strong class="d-block text-gray-dark">@username</strong>
                Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.
            </p>
        </div>
    </div>
</main>
 */

function createReservationEl(reservation, numberOfReservation) {
    const reservationDiv = document.createElement('div')

    reservationDiv.className = "d-flex text-muted pt-3"
    reservationDiv.append(createReservationSVG())
    reservationDiv.append(createP(reservation, numberOfReservation))

    return reservationDiv
}

function createReservationSVG(){
    const svg = document.createElement('svg')

    svg.className = "bd-placeholder-img flex-shrink-0 me-2 rounded"
    svg.setAttribute("width", "32")
    svg.setAttribute("hegiht", "32")
    svg.setAttribute("xmlns", "http://www.w3.org/2000/svg")
    svg.setAttribute("role", "img")
    svg.setAttribute("aria-label", "Placeholder: 32x32")
    svg.setAttribute("preserveAspectRatio", "xMidYMid slice")
    svg.setAttribute("focusable", false)
    svg.append(createReservationTitle())
    svg.append(createRect())
    svg.append(createText())

    return svg
}

function createReservationTitle() {
    const title = document.createElement('title')
    title.innerText = "Placeholder"

    return title
}

function createRect() {
    const rect = document.createElement('rect')

    rect.setAttribute("width", "100%")
    rect.setAttribute("height", "100%")
    rect.setAttribute("fill", "#007bff")

    return rect
}

function createText() {
    const text = document.createElement('text')

    text.setAttribute("x", "50%")
    text.setAttribute("y", "50%")
    text.setAttribute("fill", "#007bff")
    text.setAttribute("dy", ".3em")
    text.innerText = "32x32"

    return text
}

function createP(reservation, numberOfReservation) {
    const pEl = document.createElement('p')
    const strongEl = document.createElement('strong')

    strongEl.className = "d-block text-gray-dark"
    strongEl.innerText = "Reservation #".concat(numberOfReservation.toString())
    pEl.className = "pb-3 mb-0 small lh-sm border-bottom"
    pEl.appendChild(strongEl)
    pEl.append(reservation)

    return pEl
}

function displayFutureReservation(futureReservation = []) {
    const body = document.getElementById("accountBody")
    const htmlContentEl = document.getElementById('about-dialog-text-area');
    body.removeChild(htmlContentEl)

    const mainEl = document.createElement('main')
    const mainDiv = document.createElement('div')
    const titleEl = document.createElement('h6')

    mainEl.className = "container"
    mainDiv.className = "my-3 p-3 bg-white rounded shadow-sm"
    titleEl.className = "border-bottom pb-2 mb-0"
    titleEl.innerText = "Future Reservation"
    mainDiv.append(titleEl)

    Object.keys(futureReservation).forEach(function (key){
        console.log("Adding Reservation #"+ key +": " + futureReservation[key]);
        mainDiv.append(createReservationEl(futureReservation[key], key));
    });

    mainEl.append(mainDiv)
    body.appendChild(mainEl)
}
