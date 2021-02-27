function createObjectEl(object, numberOfObject, objectType) {
    const objectDiv = document.createElement('div')

    objectDiv.className = "d-flex text-muted pt-3"
    objectDiv.append(createObjectSVG())
    objectDiv.append(createP(object, numberOfObject, objectType))

    return objectDiv
}

function createObjectSVG(){
    const svg = document.createElement('svg')
    const img = document.createElement('img')

    img.className = "mb-md-auto"
    img.src = "/webApp/assets/brand/boutLogo.png"
    img.width = "50"
    img.height = "30"
    svg.className = "bd-placeholder-img flex-shrink-0 me-2 rounded"
    svg.setAttribute("width", "32")
    svg.setAttribute("height", "32")
    svg.setAttribute("xmlns", "http://www.w3.org/2000/svg")
    svg.setAttribute("role", "img")
    svg.setAttribute("aria-label", "Placeholder: 32x32")
    svg.setAttribute("preserveAspectRatio", "xMidYMid slice")
    svg.setAttribute("focusable", false)
    svg.append(createObjectTitle())
    svg.append(createRect())
    svg.appendChild(img)

    return svg
}

function createObjectTitle() {
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

function createP(object, numberOfObject, objectType) {
    const pEl = document.createElement('p')
    const strongEl = document.createElement('strong')

    strongEl.className = "d-block text-gray-dark"
    strongEl.innerText = objectType + " #".concat(numberOfObject.toString())
    pEl.className = "pb-3 mb-0 small lh-sm border-bottom"
    pEl.appendChild(strongEl)
    pEl.append(object)

    return pEl
}

function displayObjects(theObjects = [], titleInnerText, objectType) {
    const body = document.getElementById("body")
    const htmlContentEl = document.getElementById('about-dialog-text-area');

    if (htmlContentEl != null) {
        body.removeChild(htmlContentEl)
    }

    const mainEl = document.createElement('main')
    const mainDiv = document.createElement('div')
    const titleEl = document.createElement('h6')

    mainEl.className = "container"
    mainDiv.className = "my-3 p-3 bg-white rounded shadow-sm"
    titleEl.className = "border-bottom pb-2 mb-0"
    titleEl.innerText = titleInnerText
    mainDiv.append(titleEl)

    Object.keys(theObjects).forEach(function (key){
        console.log("Adding Object #"+ (key + 1) +": " + theObjects[key]);
        mainDiv.append(createObjectEl(theObjects[key], (key + 1), objectType));
    });

    mainEl.append(mainDiv)
    body.appendChild(mainEl)
}