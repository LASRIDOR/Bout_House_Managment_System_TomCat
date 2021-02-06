const IS_MANAGER_KEY = "Is Manager (yes/no)"
const BOAT_HASH_KEY = "Private Boat Hash"
const AGE_KEY = "Age"
const COMMENT_KEY = "Free Comment Place"
const LEVEL_KEY = "Level (Beginner/Intermediate/Advanced)"
const EXPIRY_KEY = "Date Of Expiry (Pattern: YYYY-MM-DDTHH:MM:SS)"
const JOIN_KEY = "Date Of Join (Pattern: YYYY-MM-DDTHH:MM:SS)"


function removeAllManagerPermission(){
    removeElementIfExist(IS_MANAGER_KEY)
    removeElementIfExist(BOAT_HASH_KEY)
    removeElementIfExist(AGE_KEY)
    removeElementIfExist(COMMENT_KEY)
    removeElementIfExist(LEVEL_KEY)
    removeElementIfExist(EXPIRY_KEY)
    removeElementIfExist(JOIN_KEY)
}

function removeElementIfExist(idOfElement){
    const body = document.getElementById("body")
    const elementToRemove = document.getElementById(idOfElement)

    if (elementToRemove != null){
        body.removeChild(elementToRemove)
    }
}

window.addEventListener('change', (event) => {
    removeAllManagerPermission()
});