let chatVersion = 0;
const chatRefreshRate = 1000; //milliseconds
const CHAT_LIST_URL = '/webApp/chat';

/* ******************************************
    DOM manipulation methods
   ****************************************** */

function createHeaderOfMessage(messageEntry) {
    const element = document.createElement('h5')

    element.className = "media-heading"
    element.innerHTML = `${messageEntry.username}`;

    return element
}

function createMessageContent(messageEntry) {
    const element = document.createElement('small')

    element.className = "col-lg-10"
    element.innerHTML = `${messageEntry.message}`;

    return element
}

function createMediaBodyDiv(messageEntry) {
    const element = document.createElement('div')

    element.className = "media-body"
    element.append(createHeaderOfMessage(messageEntry))
    element.append(createMessageContent(messageEntry))

    return element
}

function createMessageEntryElement (messageEntry) {
    const element = document.createElement('div');

    element.className = "media msg "
    element.classList.add('success');
    element.append(createMediaBodyDiv(messageEntry))

    return element;
}

function appendMessageEntry(messageEntry) {
    const chatAreaEl = document.querySelector('#chatarea');

    chatAreaEl.append(createMessageEntryElement(messageEntry));
}

//entries = the added chat strings represented as a single string
function appendToChatArea(entries = []) {
    const chatAreaEl = document.querySelector('#chatarea');

    entries.forEach(appendMessageEntry);

    // scroll to the end of the area
    const height = chatAreaEl.scrollHeight - chatAreaEl.getBoundingClientRect().height;
    chatAreaEl.scrollTop = height;
}


/* ******************************************
    Fetch / Ajax / JSON methods
   ****************************************** */

//call the server and get the chat version
//we also send it the current chat version so in case there was a change
//in the chat content, we will get the new string as well
async function fetchChatContentAsync() {
    try {
        const data = new URLSearchParams();
        data.append('chatversion', chatVersion);

        const response = await fetch(CHAT_LIST_URL, {
            method: 'post',
            body: data
        });

        const chatData = await response.json();

        console.log("Server chat version: " + chatData.version + ", Client chat version: " + chatVersion);
        if (chatData.version !== chatVersion) {
            chatVersion = chatData.version;
            appendToChatArea(chatData.entries);
        }
    } finally {
        triggerTimeoutRefreshChat();
    }
}

async function sendMessage () {
    const chatFormEl = document.forms.namedItem('chatform');

    const data = new URLSearchParams();
    for (const pair of new FormData(chatFormEl)) {
        data.append(pair[0], pair[1]);
    }

    await fetch(chatFormEl.action, {
        method: chatFormEl.method,
        body: data
    });
}

function handleFormSubmit (event) {
    event.preventDefault();
    sendMessage().then(() => {
        document.getElementById('userstring').value = '';
    });
}

function setupEventHandlers() {
    const chatFormEl = document.forms.namedItem('chatform');
    chatFormEl.addEventListener('submit', handleFormSubmit);
}

function triggerTimeoutRefreshChat() {
    setTimeout(fetchChatContentAsync, chatRefreshRate);
}

//activate the timer calls after the page is loaded
window.addEventListener('load', () => {
    setupEventHandlers();

    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    fetchChatContentAsync();
});

/* ******************************************
    Open url in new tab
   ****************************************** */

function openInNewTab(url) {
    var win = window.open(url, '_blank');
    win.focus();
}