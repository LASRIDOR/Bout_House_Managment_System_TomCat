GET_ALL_INSTANCES = "/webApp/display all"

async function fetchDisplayAllInstances(boutHouseDataType) {
    let params = new URLSearchParams()
    params.set("BoutHouseDataType", boutHouseDataType)

    const request = new Request(GET_ALL_INSTANCES, {
        method: 'post',
        body: params
    })

    const response = await fetch(request)
    const allInstances = await response.json()

    insertToBody(allInstances, boutHouseDataType)
}

/*

<main class="container">

    <div class="my-3 p-3 bg-white rounded shadow-sm">
        <h6 class="border-bottom pb-2 mb-0">Recent updates</h6>
        <div class="d-flex text-muted pt-3">
        
            <text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text>
 */

function insertToBody(allInstances, boutHouseDataType) {
    const body = document.getElementById("body")
    const htmlContentEl = document.getElementById('about-dialog-text-area');

    body.appendChild(createMainDisplayAll(allInstances, boutHouseDataType))

    if (htmlContentEl != null) {
        body.removeChild(htmlContentEl)
    }
}

function createMainDisplayAll(allInstances, boutHouseDataType){
    const mainEl = document.createElement('main')
    
    mainEl.className = "container"
    mainEl.appendChild(createMainDiv(allInstances, boutHouseDataType))
    
    return mainEl
}

function createMainDiv(allInstances, boutHouseDataType){
    const mainDiv = document.createElement('div')
    
    mainDiv.className = "my-3 p-3 bg-white rounded shadow-sm"
    mainDiv.appendChild(createHeaderOfMain(boutHouseDataType))
    mainDiv.appendChild(createDivOfText(allInstances))
    
    return mainDiv
}

function createHeaderOfMain(boutHouseDataType){
    const headerEl = document.createElement('h6')

    headerEl.className = "border-bottom pb-2 mb-0"
    headerEl.innerText = "All" + boutHouseDataType + "s"

    return headerEl
}

function createDivOfText(allInstances){
    const textEl = document.createElement('text')

    textEl.id = 'allInstances'
    textEl.innerText = allInstances.message

    return textEl
}