function managerDisplayReservationOptions(){
    const htmlContentEl = document.getElementById('buttonArea')
    const displayReservationsArea = document.getElementById("displayReservationsArea")
    const extraInformation = document.getElementById("extraInformation")
    const dateInputArea = document.getElementById("dateInputArea")
    const approveReservationsArea = document.getElementById("approveReservationsArea")

    if (extraInformation != null) {
        htmlContentEl.removeChild(extraInformation)
    }

    if (approveReservationsArea != null) {
        approveReservationsArea.remove()
    }

    if (dateInputArea != null) {
        dateInputArea.remove()
    }
    if (displayReservationsArea == null) {
        const displayReservationsArea = document.createElement('div')
        const displayUpcomingWeekReservationsButton = document.createElement('a')
        const displaySpecificDateReservationsButton = document.createElement('a')
        const displayUnapprovedUpcomingWeekReservationsButton = document.createElement('a')
        const displayUnapprovedSpecificDateReservationsButton = document.createElement('a')
        const labelEl = document.createElement('label')

        displayReservationsArea.id = "displayReservationsArea"

        labelEl.className = "labels"
        labelEl.id = "reservationsDisplayLabel"
        labelEl.innerText = "Please choose a display options of the ones below: "

        displayUpcomingWeekReservationsButton.className = "btn btn-primary my-2"
        displayUpcomingWeekReservationsButton.id = "displayUpcomingWeekReservationsButton"
        displayUpcomingWeekReservationsButton.innerText = "Display upcoming week reservations"
        displayUpcomingWeekReservationsButton.setAttribute("onclick", "fetchManagerDisplayReservations('displayUpcomingWeekReservationsButton')")

        displaySpecificDateReservationsButton.className = "btn btn-primary my-2"
        displaySpecificDateReservationsButton.id = "displaySpecificDateReservationsButton"
        displaySpecificDateReservationsButton.innerText = "Display reservations by a date"
        displaySpecificDateReservationsButton.setAttribute("onclick", "managerDisplaySpecificDateReservationsButton('displaySpecificDateReservationsButton')")

        displayUnapprovedUpcomingWeekReservationsButton.className = "btn btn-primary my-2"
        displayUnapprovedUpcomingWeekReservationsButton.id = "displayUnapprovedUpcomingWeekReservationsButton"
        displayUnapprovedUpcomingWeekReservationsButton.innerText = "Display unapproved upcoming week reservations"
        displayUnapprovedUpcomingWeekReservationsButton.setAttribute("onclick", "fetchManagerDisplayReservations('displayUnapprovedUpcomingWeekReservationsButton')")

        displayUnapprovedSpecificDateReservationsButton.className = "btn btn-primary my-2"
        displayUnapprovedSpecificDateReservationsButton.id = "displayUnapprovedSpecificDateReservationsButton"
        displayUnapprovedSpecificDateReservationsButton.innerText = "Display unapproved reservations by a date"
        displayUnapprovedSpecificDateReservationsButton.setAttribute("onclick", "managerDisplaySpecificDateReservationsButton('displayUnapprovedSpecificDateReservationsButton')")


        displayReservationsArea.appendChild(labelEl)
        displayReservationsArea.appendChild(displayUpcomingWeekReservationsButton)
        displayReservationsArea.appendChild(displaySpecificDateReservationsButton)
        displayReservationsArea.appendChild(displayUnapprovedUpcomingWeekReservationsButton)
        displayReservationsArea.appendChild(displayUnapprovedSpecificDateReservationsButton)

        htmlContentEl.appendChild(displayReservationsArea)
    }
}

function managerDisplaySpecificDateReservationsButton(option){
    const htmlContentEl = document.getElementById('buttonArea')
    const dateInputArea = document.getElementById("dateInputArea")
    const displayReservationsArea = document.getElementById("displayReservationsArea")

    if (dateInputArea == null){
        const dateInputArea = document.createElement('div')
        const submitButton = document.createElement('a')
        const dateInput = document.createElement('input')
        const labelEl = document.createElement('label')

        dateInputArea.id = "dateInputArea"
        labelEl.className = "labels"
        labelEl.id = "dateInputLabel"
        labelEl.innerText = "Please choose a date: "
        dateInput.id = "dateId"
        dateInput.name = "dateId"
        dateInput.type = "date"
        dateInput.required = true
        dateInput.className = "form-control"
        submitButton.className = "btn btn-primary my-2"
        submitButton.id = "dateInputSubmit"
        submitButton.innerText = "Submit"

        submitButton.setAttribute("onclick", "fetchManagerDisplayReservations('" + option + "')")

        dateInputArea.appendChild(labelEl)
        dateInputArea.appendChild(dateInput)
        dateInputArea.appendChild(submitButton)

        displayReservationsArea.appendChild(dateInputArea)
        htmlContentEl.appendChild(displayReservationsArea)
    }
    else {
        const submitButton = document.getElementById("instanceIdSubmit")
        const labelEl = document.getElementById("instanceIdLabel")

        labelEl.innerText = "Please choose a date: "
        submitButton.setAttribute("onclick", "fetchManagerDisplayReservations('" + option + "')")
    }
}
