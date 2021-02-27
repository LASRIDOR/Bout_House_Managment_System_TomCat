function createInputForIdOfInstance(instanceType, operation){
    const htmlContentEl = document.getElementById('buttonArea')
    const displayReservationsArea = document.getElementById("displayReservationsArea")
    const extraInformation = document.getElementById("extraInformation")
    const approveReservationsArea = document.getElementById("approveReservationsArea")

    if (displayReservationsArea != null) {
        displayReservationsArea.remove()
    }

    if (approveReservationsArea != null) {
        approveReservationsArea.remove()
    }

    if (extraInformation == null){
        const extraInformation = document.createElement('div')
        const submitButton = document.createElement('a')
        const instanceIdInput = document.createElement('input')
        const labelEl = document.createElement('label')

        extraInformation.id = "extraInformation"
        labelEl.className = "labels"
        labelEl.id = "instanceIdLabel"
        labelEl.innerText = createCompatibleText(instanceType)
        instanceIdInput.id = "instanceId"
        instanceIdInput.name = "instanceId"
        instanceIdInput.type = "text"
        instanceIdInput.required = true
        instanceIdInput.className = "form-control"
        submitButton.className = "btn btn-primary my-2"
        submitButton.id = "instanceIdSubmit"
        submitButton.innerText = "Submit"

        submitButton.setAttribute("onclick", createSubmitButtonForInstanceId(instanceType, operation))

        extraInformation.appendChild(labelEl)
        extraInformation.appendChild(instanceIdInput)
        extraInformation.appendChild(submitButton)
        htmlContentEl.appendChild(extraInformation)
    }
    else{
        const submitButton = document.getElementById("instanceIdSubmit")
        const labelEl = document.getElementById("instanceIdLabel")

        labelEl.innerText = createCompatibleText(instanceType)
        submitButton.setAttribute("onclick", createSubmitButtonForInstanceId(instanceType, operation))
    }
}

function createCompatibleText(instanceType){
    let message = "Enter "

    if (instanceType === "Member"){
        message += "Email of Member"
    }
    else if (instanceType === "Storage"){
        message += "Boat Id"
    }
    else if (instanceType === "Reservation"){
        message += "Reservation Number"
    }
    else {
        message += "Time Window Name"
    }

    return message
}

function createSubmitButtonForInstanceId(instanceType, operation){
    let functionToInvoke

    if (operation === "Update"){
        if (instanceType === "Storage") {
            functionToInvoke = "fetchBoatUpdateDetails()"
        }
        else if (instanceType === "Time Window") {
            functionToInvoke = "fetchUpdateTimeWindowDetails()"
        }
        else if (instanceType === "Reservation") {
            functionToInvoke = "fetchUpdateReservationDetails()"
        }
        else {
            functionToInvoke = "fetchUpdateInstance('"+ instanceType+"')"
        }
    }
    else if (operation === "Delete"){
        functionToInvoke = "fetchDeleteInstance('"+ instanceType+"')"
    }

    return functionToInvoke
}