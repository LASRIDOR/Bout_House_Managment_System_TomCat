let memberVersion = 0;
let allMembersVersion = 0;
const notificationRefreshRate = 1000; //milliseconds
const NOTIFICATION_LIST_URL = "/webApp/notification";

async function fetchNotificationContentAsync() {
    try {
        const numberOfNewNotificationEl = document.getElementById("newNotification")

        if (numberOfNewNotificationEl !== null) {
            const data = new URLSearchParams();
            data.append('memberVersion', memberVersion);
            data.append('allMembersVersion', allMembersVersion);

            const response = await fetch(NOTIFICATION_LIST_URL, {
                method: 'post',
                body: data
            });

            const notificationData = await response.json();

            console.log("Server all members notification version: " + notificationData.allMembersVersion + ", Client all members notification version: " + allMembersVersion);
            console.log("Server member notification version: " + notificationData.memberVersion + ", Client member version: " + memberVersion);
            if (notificationData.allMembersVersion !== allMembersVersion || notificationData.memberVersion !== memberVersion) {
                allMembersVersion = notificationData.allMembersVersion;
                memberVersion = notificationData.memberVersion;
                numberOfNewNotificationEl.innerText = " (" + (allMembersVersion + memberVersion) + ") "
                appendToNotificationArea(notificationData.allMembersEntries, "AllMembersNotificationArea");
                appendToNotificationArea(notificationData.memberEntries, "perMemberNotificationArea");
            }
        }
    } finally {
        triggerTimeoutRefreshNotification();
    }
}

function triggerTimeoutRefreshNotification() {
    setTimeout(fetchNotificationContentAsync, notificationRefreshRate);
}

function appendToNotificationArea(entries = [], idOfNotificationArea) {
    entries.forEach((entry) => {appendNotification(entry, idOfNotificationArea)});
}

function appendNotification(notification, idOfNotificationArea) {
    const notificationAreaEl = document.getElementById(idOfNotificationArea);
    const hr = document.createElement('hr');

    notificationAreaEl.append(createNotificationElement(notification));
    notificationAreaEl.appendChild(hr)
}

function createNotificationElement(notification){
    const element = document.createElement('div');

    element.classList = 'media-body';
    element.appendChild(createHeaderOfNotification(notification))
    element.append(notification.message)

    return element;
}

function createHeaderOfNotification(notification){
    const header = document.createElement('h5')

    header.className = "mt-0 mb-1"
    header.innerText = notification.header;

    return header
}

/*
//activate the timer calls after the page is loaded
window.addEventListener('load', () => {
    //The notification content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
    fetchNotificationContentAsync();
});

 */