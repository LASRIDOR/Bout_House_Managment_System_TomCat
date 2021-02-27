async function submitXmlForm() {
    const updateFormEl = document.getElementById('xmlUploadForm');
    const errorDiv = document.getElementById("errorMessage")

    //const xmlFileEl = document.getElementById("XML Path").files[0]
    const data = new URLSearchParams();

    for (const pair of new FormData(updateFormEl)) {
        data.append(pair[0], pair[1]);
    }

    //data.append("XML Path", xmlFileEl);

    await fetch(updateFormEl.action, {
        method: updateFormEl.method,
        body: data,
        headers: new Headers({'Content-Type': 'multipart/form-data'}),
    })
        .then((response) => response.json())
        .then((message) => console.log(message))
        .then((message) => errorDiv.innerText = message.message)
}

function replaceContentToDownloadDataBaseForm(){
    const xmlContentPage = document.getElementById('xml content')
    const xmlUploadForm = document.getElementById('xmlUploadForm')

    xmlContentPage.removeChild(xmlUploadForm)
    xmlContentPage.appendChild(createDownloadForm())
}


function createDownloadForm(){
    const downloadForm = document.createElement('form')

    downloadForm.id = "xmlDownloadForm"
    downloadForm.action = "/webApp/download xml database"
    downloadForm.method = 'get'
    downloadForm.appendChild(createSelectBoutHouseDataType())
    downloadForm.appendChild(createSubmitButtonDownloadXml())

    return downloadForm
}

function createSelectBoutHouseDataType(){
    const selectDiv = document.createElement('div')
    const selectLabel = document.createElement('label')
    const selectEl = document.createElement('select')

    selectDiv.className = "form-group"
    selectLabel.for = "boutHouseDataType"
    selectLabel.innerText = "Instance Type"
    selectEl.className = "form-control"
    selectEl.name = "BoutHouseDataType"
    selectEl.required = "required"
    appendSelectValue(selectEl)


    selectDiv.appendChild(selectLabel)
    selectDiv.appendChild(selectEl)

    return selectDiv
}

function appendSelectValue(selectEl){
    const memberOption = document.createElement('option')
    const boatOption = document.createElement('option')
    const timeWindowOption = document.createElement('option')

    memberOption.value = "Member"
    memberOption.innerText = "Member"
    selectEl.appendChild(memberOption)
    boatOption.value = "Boat"
    boatOption.innerText = "Boat"
    selectEl.appendChild(boatOption)
    timeWindowOption.value = "Time Window"
    timeWindowOption.innerText = "Time Window"
    selectEl.appendChild(timeWindowOption)
}

//        <button type="submit" class="btn btn-primary">Submit</button>

function createSubmitButtonDownloadXml(){
    const submitButton = document.createElement('button')

    submitButton.type = 'submit'
    submitButton.className = 'btn btn-primary'
    submitButton.innerText = "download"

    return submitButton
}