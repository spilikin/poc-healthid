<#import "template.ftl" as layout>
<@layout.registrationLayout; section>
    <#if section = "form">
        <script src="${url.resourcesPath}/healthid/VanillaQR.min.js"></script>
        <script src="${url.resourcesPath}/healthid/healthid-login.js"></script>

        <form class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
        <input name="username" type="text" value="user1">
        <input name="signature" type="text">
        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
               name="demoLogin" id="kc-login" type="submit" value="Login with code"/>
        </form>

        <form id="healthid-login-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div class="${properties.kcInputWrapperClass!}">
                    <div id="qrcode" style="float: left;"></div>
                    <script>

                    //Create qr object
                    //Minus the url, these are the defaults
                    var qr = new VanillaQR({

                        url: "https://id.acme.spilikin.dev/auth/auth/realms/healthid/protocols/remoteauth?client_id=aua.spilikin.dev&redirect_uri=&&code_challenge=${challenge}",
                        size: 140,

                        colorLight: "#ffffff",
                        colorDark: "#000000",

                        //output to table or canvas
                        toTable: false,

                        //Ecc correction level 1-4
                        ecclevel: 1,

                        //Use a border or not
                        noBorder: false,

                        //Border size to output at
                        borderSize: 4

                    });

                    //Canvas or table is stored in domElement property
                    document.getElementById("qrcode").appendChild(qr.domElement);
                    </script>
                    <div style="font-size: 150%">
                    Open HealthID App and enter following code:
                    <p>
                    <b>${userCode}</b>
                    </p>
                    </div>
                    <input id="challenge" name="challenge" type="hidden" value="${challenge}"/>
                    <input id="userCode" name="userCode" type="hidden" value="${userCode}"/>

                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                        onclick="startPolling(document.getElementById('healthid-login-form')); return false;"
                        name="command" type="submit" value="poll"/>

                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                        name="command" type="submit" value="finish"/>

                </div>
            </div>

            <!--
            <div class="${properties.kcFormGroupClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                    <div style="font-size: 200%; text-align: center">OR</div>
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <div class="${properties.kcFormButtonsWrapperClass!}">
                        <input id="username" name="username" type="hidden" class="${properties.kcInputClass!}" value="demouser"/>
                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}"
                               name="demoLogin" id="kc-login" type="submit" value="Demo Login"/>
                    </div>
                </div>
            </div>
            -->
        </form>

    </#if>
</@layout.registrationLayout>