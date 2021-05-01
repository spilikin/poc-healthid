{
    "endpoint": "${url.loginAction?no_esc}",    
    "challenge": "${challenge.challenge?no_esc}",
    "device_code": "${challenge.deviceCode?no_esc}",
    "user_code": "${challenge.userCode?no_esc}",
    "authenticated": ${challenge.authenticated?string('true', 'false')}
}