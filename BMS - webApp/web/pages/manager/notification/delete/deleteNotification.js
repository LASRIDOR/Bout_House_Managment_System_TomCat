const ALL_MEMBERS_NOTIFICATION_URL = "/webApp/all members notification"
const ALL_MEMBERS_NOTIFICATION_DELETE_URL = "/webApp/delete notification to all members"
const MEMBER_NOTIFICATION_URL = "/webApp/member notification"
const MEMBER_NOTIFICATION_DELETE_URL = "/webApp/delete notification from member"

async function fetchAllNotificationOfMembersToDelete() {
    let params = new URLSearchParams()

    const request = new Request(ALL_MEMBERS_NOTIFICATION_URL, {
        method: 'post' || 'get',
        body: params
    })

    const response = await fetch(request)
    const notifications = await response.json()

    fetchNotificationToDelete(notifications, ALL_MEMBERS_NOTIFICATION_DELETE_URL)
}

function addInputForInstanceId(){
    const contentPage = document.getElementById("interfaceArea")

    if (!document.getElementById("instanceId")) {
        contentPage.append(createInputForInstanceId())
        contentPage.append(submitFotInstanceId())
        fetchCheckingForInstanceExisting()
    }
}

function createInputForInstanceId() {
    const instanceId = document.createElement('input')

    instanceId.type = "text"
    instanceId.id = "instanceId"
    instanceId.name = "Email"
    instanceId.className = "form-control"
    instanceId.placeholder = ""

    return instanceId
}

function submitFotInstanceId() {
    const buttonForSubmit = document.createElement('a');

    buttonForSubmit.setAttribute("onclick", "fetchAllNotificationOfMemberToDelete()");

    buttonForSubmit.className = "btn btn-primary my-2";
    buttonForSubmit.innerText = "submit"

    return buttonForSubmit
}

async function fetchAllNotificationOfMemberToDelete() {
    const memberToFetchHisNotification = document.getElementById("instanceId");
    memberToFetchHisNotification.hidden = true;

    let params = new URLSearchParams()
    params.set("Email", memberToFetchHisNotification.value)

    const request = new Request(MEMBER_NOTIFICATION_URL, {
        method: 'post' || 'get',
        body: params
    })

    const response = await fetch(request)
    const notifications = await response.json()

    fetchNotificationToDelete(notifications, MEMBER_NOTIFICATION_DELETE_URL)

    const notificationForm = document.getElementById("list-item")
    notificationForm.append(memberToFetchHisNotification)
}

function fetchNotificationToDelete(allNotification = [], deleteAction){
    const pageContent = document.getElementById("page-content")

    appendStyleNotification()
    pageContent.innerHTML = ""
    pageContent.append(createTableOfNotification(allNotification, deleteAction))

    return pageContent
}

function appendStyleNotification(){
    const header = document.head
    const styleSheet = document.createElement('link')

    styleSheet.href = "/webApp/pages/manager/notification/delete/deleteNotification.css"
    styleSheet.rel = "stylesheet"

    header.append(styleSheet)
}

function createTableOfNotification(allNotification = [], deleteAction){
    const paddingDiv = document.createElement('div')

    paddingDiv.className = "padding"
    paddingDiv.append(createRowDiv(allNotification, deleteAction))

    return paddingDiv;
}

function createRowDiv(allNotification = [], deleteAction){
    const rowDiv = document.createElement('div')

    rowDiv.className = "row"
    rowDiv.append(createColSm6Div(allNotification, deleteAction))

    return rowDiv;
}

function createColSm6Div(allNotification = [], deleteAction){
    const sm6Div = document.createElement('div')

    sm6Div.className = "col-sm-6"
    sm6Div.append(createListDiv(allNotification, deleteAction))

    return sm6Div;
}

function createListDiv(allNotification = [], deleteAction){
    const listDiv = document.createElement('div')

    listDiv.className = "list-item"
    listDiv.id = "list-item"

    Object.keys(allNotification).forEach(function (key){
        listDiv.append(createNotificationListItem(key, allNotification[key]))
        listDiv.append(createDeleteButton(key, deleteAction))
    })

    return listDiv;
}

function createNotificationListItem(index, notification){
    const divOfNotification = document.createElement('div')

    divOfNotification.className = "flex"
    divOfNotification.innerText = "Notification #" + index
    divOfNotification.append(createNotificationContent(notification))

    return divOfNotification
}

function createNotificationContent(notification){
    const divOfNotificationContent = document.createElement('div')

    divOfNotificationContent.className = "item-except text-muted text-sm h-1x"
    divOfNotificationContent.innerText = notification

    return divOfNotificationContent
}
function createDeleteButton(index, deleteAction){
    const divOfDeleteButton = document.createElement('div')

    divOfDeleteButton.className = "no-wrap"
    divOfDeleteButton.append(createDeleteButtonEl(index, deleteAction))

    return divOfDeleteButton
}

function createDeleteButtonEl(index, deleteAction){
    const aEl = document.createElement('a')

    aEl.innerText = "Delete"
    aEl.className="btn btn-primary my-2"
    aEl.setAttribute("onclick", "submitDeleteNotification('" + index + "', '" + deleteAction + "')")

    return aEl
}

async function submitDeleteNotification(index, deleteAction) {
    let params = new URLSearchParams()

    params.set("Notification Index To Delete", index)
    inCaseOfDeletingSpecificMemberNotification(deleteAction, params)

    const request = new Request(deleteAction, {
        method: 'post' || 'get',
        body: params
    })

    await fetch(request).then(() => {refreshNotificationList(deleteAction)})
}

function refreshNotificationList(deleteAction){
    if (deleteAction === MEMBER_NOTIFICATION_DELETE_URL) {
        fetchAllNotificationOfMemberToDelete()
    }
    else {
        fetchAllNotificationOfMembersToDelete()
    }
}

function inCaseOfDeletingSpecificMemberNotification(deleteAction, params){
    if (deleteAction === MEMBER_NOTIFICATION_DELETE_URL) {
        const emailOfMember = document.getElementById("instanceId")
        params.set(emailOfMember.name, emailOfMember.value)
    }
}
/*

<div class="page-content page-container" id="page-content">
    <div class="padding">
        <div class="row">
            <div class="col-sm-6">
                <div class="list list-row block">
                    <div class="list-item" data-id="19">
                        <div class="flex"> <a href="#" class="item-author text-color" data-abc="true">Sam Kuran</a>
                            <div class="item-except text-muted text-sm h-1x">For what reason would it be advisable for me to think about business content?</div>
                        </div>
                        <div class="no-wrap">
                            <div class="item-date text-muted text-sm d-none d-md-block">13/12 18</div>
                        </div>
                    </div>
                    <div class="list-item" data-id="7">
                        <div><a href="#" data-abc="true"><span class="w-48 avatar gd-primary"><img src="https://img.icons8.com/color/48/000000/administrator-male.png" alt="."></span></a></div>
                        <div class="flex"> <a href="#" class="item-author text-color" data-abc="true">Kinley Adolf</a>
                            <div class="item-except text-muted text-sm h-1x">For what reason would it be advisable for me to think about business content?</div>
                        </div>
                        <div class="no-wrap">
                            <div class="item-date text-muted text-sm d-none d-md-block">21 July</div>
                        </div>
                    </div>
                    <div class="list-item" data-id="17">
                        <div><a href="#" data-abc="true"><span class="w-48 avatar gd-warning">H</span></a></div>
                        <div class="flex"> <a href="#" class="item-author text-color" data-abc="true">Velden Kamut</a>
                            <div class="item-except text-muted text-sm h-1x">For what reason would it be advisable for me to think about business content?</div>
                        </div>
                        <div class="no-wrap">
                            <div class="item-date text-muted text-sm d-none d-md-block">13/3/19</div>
                        </div>
                    </div>
                    <div class="list-item" data-id="16">
                        <div><a href="#" data-abc="true"><span class="w-48 avatar gd-info">F</span></a></div>
                        <div class="flex"> <a href="#" class="item-author text-color" data-abc="true">Stuart Kim</a>
                            <div class="item-except text-muted text-sm h-1x">For what reason would it be advisable for me to think about business content?</div>
                        </div>
                        <div class="no-wrap">
                            <div class="item-date text-muted text-sm d-none d-md-block">03/1/19</div>
                        </div>
                    </div>
                    <div class="list-item" data-id="4">
                        <div><a href="#" data-abc="true"><span class="w-48 avatar gd-success"><img src="https://img.icons8.com/color/48/000000/guest-male.png" alt="."></span></a></div>
                        <div class="flex"> <a href="#" class="item-author text-color" data-abc="true">Simply Fry</a>
                            <div class="item-except text-muted text-sm h-1x">For what reason would it be advisable for me to think about business content?</div>
                        </div>
                        <div class="no-wrap">
                            <div class="item-date text-muted text-sm d-none d-md-block">2 hours ago</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
 */
