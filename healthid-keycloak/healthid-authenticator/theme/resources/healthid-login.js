console.log("Loading HealthID login script")

function startPolling(formElement) {
    pollAuthenticationInfo(formElement).then( xhr => {
        formElement.action = xhr.response.endpoint;
        if (!xhr.response.authenticated) {
            return setTimeout(function() { startPolling(formElement) }, 1000)
        } else {
            document.getElementById("btn-finish").click()
        }
    }).catch( error => {
        console.log(error)
        setTimeout(function() { location.reload() }, 1000)
    })
}

function pollAuthenticationInfo(formElement) {
    return new Promise((resolve, reject) => {
        var xhr = new XMLHttpRequest();
        xhr.onload = function () {

        if (xhr.status == 200 || (xhr.status == 400 && xhr.response['endpoint'] != undefined) ) {
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
        }
        params.set('command', 'poll')
        xhr.send(params)
    });    
}

function demoLogin(formElement) {
    var input = document.createElement('input');
    input.setAttribute('name', "command");
    input.setAttribute('value', "demo");
    input.setAttribute('type', "hidden");
    formElement.appendChild(input);
}