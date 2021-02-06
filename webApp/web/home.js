window.addEventListener("hashchange", function(e) {
    history.back()
})

window.addEventListener("load", () => {
    fetchWeeklyReservation()
});