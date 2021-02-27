function managerApproveReservations() {
    const htmlContentEl = document.getElementById('buttonArea')
    const approveReservationsArea = document.getElementById("approveReservationsArea")
    const extraInformation = document.getElementById("extraInformation")
    const dateInputArea = document.getElementById("dateInputArea")
    const displayReservationsArea = document.getElementById("displayReservationsArea")

    if (displayReservationsArea != null) {
        displayReservationsArea.remove()
    }

    if (extraInformation != null) {
        htmlContentEl.removeChild(extraInformation)
    }

    if (dateInputArea != null) {
        dateInputArea.remove()
    }

    if (approveReservationsArea == null) {
        const approveReservationsArea = document.createElement('div')
        const approveUpcomingWeekReservationsButton = document.createElement('a')
        const approveSpecificDateReservationsButton = document.createElement('a')

        const labelEl = document.createElement('label')

        approveReservationsArea.id = "approveReservationsArea"

        labelEl.className = "labels"
        labelEl.id = "approveReservationsLabel"
        labelEl.innerText = "Please choose an approving options of the ones below: "

        approveUpcomingWeekReservationsButton.className = "btn btn-primary my-2"
        approveUpcomingWeekReservationsButton.id = "approveUpcomingWeekReservationsButton"
        approveUpcomingWeekReservationsButton.innerText = "Approve upcoming week reservations"
        approveUpcomingWeekReservationsButton.setAttribute("onclick", "fetchManagerApproveReservations('displayUnapprovedUpcomingWeekReservationsButton')")

        approveSpecificDateReservationsButton.className = "btn btn-primary my-2"
        approveSpecificDateReservationsButton.id = "approveSpecificDateReservationsButton"
        approveSpecificDateReservationsButton.innerText = "Approve specific date reservations"
        approveSpecificDateReservationsButton.setAttribute("onclick", "managerDisplaySpecificDateUnapprovedReservationsButton('displayUnapprovedSpecificDateReservationsButton')")

        approveReservationsArea.appendChild(labelEl)
        approveReservationsArea.appendChild(approveUpcomingWeekReservationsButton)
        approveReservationsArea.appendChild(approveSpecificDateReservationsButton)

        htmlContentEl.appendChild(approveReservationsArea)
    }
}

function managerDisplaySpecificDateUnapprovedReservationsButton() {
    const htmlContentEl = document.getElementById('buttonArea')
    const dateInputArea = document.getElementById("dateInputArea")
    const approveReservationsArea = document.getElementById("approveReservationsArea")

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

        submitButton.setAttribute("onclick", "fetchManagerApproveReservations('displayUnapprovedSpecificDateReservationsButton')")

        dateInputArea.appendChild(labelEl)
        dateInputArea.appendChild(dateInput)
        dateInputArea.appendChild(submitButton)

        approveReservationsArea.appendChild(dateInputArea)
        htmlContentEl.appendChild(approveReservationsArea)
    }
    else {
        const submitButton = document.getElementById("instanceIdSubmit")
        const labelEl = document.getElementById("instanceIdLabel")

        labelEl.innerText = "Please choose a date: "
        submitButton.setAttribute("onclick", "fetchManagerApproveReservations('displayUnapprovedSpecificDateReservationsButton')")
    }
}