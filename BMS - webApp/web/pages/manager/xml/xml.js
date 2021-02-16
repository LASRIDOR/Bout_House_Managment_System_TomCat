async function submitXmlForm() {
    const updateFormEl = document.forms.namedItem('xmlForm');
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
        headers: new Headers({'content-type': 'multipart/form-data'}),
    })
        .then((response) => response.json())
        .then((message) => errorDiv.innerText = message.message)
}