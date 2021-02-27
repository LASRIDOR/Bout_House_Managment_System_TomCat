const PAST_RESERVATION_URL = '/webApp/past reservation'

async function fetchPastWeekReservations() {
    const response = await fetch(PAST_RESERVATION_URL)
    const futureReservation = await response.json();
    displayObjects(futureReservation, "Past Reservations", "Reservation")
}

