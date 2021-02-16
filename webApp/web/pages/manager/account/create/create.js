function fetchManagerCreateMemberInstance(){
    fetchCreateInstance('Member')
        .then(r => document.getElementById("UsernameID").required = true)
        .then(r => document.getElementById("EmailID").required = true)
        .then(r => document.getElementById("PasswordID").required = true)
        .then(r => document.getElementById("Is Manager (yes/no)ID").required = true)
}