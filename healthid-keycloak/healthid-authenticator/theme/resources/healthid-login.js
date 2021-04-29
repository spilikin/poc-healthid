console.log("Loading HealthID login script")
//setTimeout(function(){ document.getElementById("healthid-login-form").submit(); }, 2000);

function startPolling(formElement) {
    pollAuthenticationInfo(formElement).then( xhr => {
        formElement.action = xhr.response.action;
        console.log(xhr.response)
    }).catch( error => {
        console.log(error)
        location.reload()
    })
}

function pollAuthenticationInfo(formElement) {

    return new Promise((resolve, reject) => {
        var xhr = new XMLHttpRequest();

        xhr.onload = function () {

        if (xhr.status == 200) {
            resolve(xhr);
        } else {
            console.log("Received error")
            reject(Error(xhr.status));
        }
        };
        xhr.open(formElement.method, formElement.action, true);
        xhr.responseType = "json"
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        var formData = new FormData (formElement)
        var params = new URLSearchParams(formElement.action.split('?')[1])
        for (var [key, value] of formData) {
            params.append(key, value)
            console.log(key)
        }
        params.set('command', 'poll')
        xhr.send(params)
    });    
}