{
    "endpoint": "${url.loginAction?no_esc}",
    <#if !challenge.authenticated>
    "error": "authorization_pending"
    <#else>
    "authenticated": true
    </#if>
}
