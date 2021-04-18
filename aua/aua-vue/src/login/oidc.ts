// Simple OIDC Client
// Base on https://github.com/curityio/pkce-javascript-example
//

export function redirectToAuthEndpoint(
  authEndpoint: string,
  clientId: string,
  redirectURI: string
) {
  const codeVerifier = generateRandomString(64);

  generateCodeChallenge(codeVerifier).then(function (codeChallenge) {
    // TODO: ist it allowed to save the code_verifier in local storage?
    window.localStorage.setItem("code_verifier", codeVerifier);

    const args = new URLSearchParams({
      response_type: "code",
      client_id: clientId,
      code_challenge_method: "S256",
      code_challenge: codeChallenge,
      redirect_uri: redirectURI,
      scope: "openid email"
    });
    window.location.href = authEndpoint + "?" + args;
  });
}

export function retrieveToken(
  clientId: string,
  tokenEndpoint: string,
  args: URLSearchParams
): Promise<string> {
  const code = args.get("code")!;
  return new Promise<string>((resolve, reject) => {
    if (args.get("error") != null) {
      reject(Error(args.get("error")!));
    } else if (args.get("code") != null) {
      const xhr = new XMLHttpRequest();

      xhr.onload = function () {
        const response = xhr.response;

        if (xhr.status == 200) {
          localStorage.setItem("id_token", response.id_token);
          localStorage.setItem("access_token", response.access_token);
          resolve(response.access_token);
        } else {
          reject(Error(response.error_description));
        }
      };
      xhr.responseType = "json";
      xhr.open("POST", tokenEndpoint, true);
      xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      xhr.send(
        new URLSearchParams({
          client_id: clientId,
          code_verifier: window.localStorage.getItem("code_verifier")!,
          grant_type: "authorization_code",
          redirect_uri: location.href.replace(location.search, ""),
          code: code,
        })
      );
    } else {
      reject("Unknown error");
    }
  });
}

function generateRandomString(length: number): string {
  let text = "";
  const possible =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  for (let i = 0; i < length; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  }

  return text;
}

async function generateCodeChallenge(codeVerifier: string) {
  const digest = await crypto.subtle.digest(
    "SHA-256",
    new TextEncoder().encode(codeVerifier)
  );

  return btoa(String.fromCharCode(...new Uint8Array(digest)))
    .replace(/=/g, "")
    .replace(/\+/g, "-")
    .replace(/\//g, "_");
}
