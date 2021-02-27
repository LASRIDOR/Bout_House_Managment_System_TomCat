const existingRefreshRate = 2000; //milliseconds
const EXISTING_CHECK_URL = "/webApp/existing instance";

async function fetchCheckingForInstanceExisting() {
    try {
        const memberToNotify = document.getElementById("instanceId");
        const messageArea = document.getElementById("errorMessage");

        const data = new URLSearchParams();

        data.set("instanceId", memberToNotify.value);
        data.set("BoutHouseDataType", 'Member');

        await fetch(EXISTING_CHECK_URL, {
            method: 'post' || 'get',
            body: data
        })
            .then((response) => response.json())
            .then((message) => messageArea.innerText = message.message)
    }
    finally {
        triggerTimeoutForChecking();
    }
}

function triggerTimeoutForChecking() {
    setTimeout(fetchCheckingForInstanceExisting, existingRefreshRate);
}