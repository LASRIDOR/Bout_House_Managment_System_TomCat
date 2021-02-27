const FUTURE_RESERVATION_URL = '/webApp/future reservation'

async function fetchUpcomingWeekReservations() {
    const response = await fetch(FUTURE_RESERVATION_URL)
    const futureReservation = await response.json();
    displayObjects(futureReservation, "Future Reservations", "Reservation")
}
