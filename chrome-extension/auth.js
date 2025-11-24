import "../dto/UserDto.js";
import "./constants";

document.getElementById("registerBtn").addEventListener("click", async () => {
    const email = document.getElementById("email").value;
    const username = document.getElementById("username").value;

    try {
        const user = { username, email };

        const { jwtToken } = await startRegistration(user);

        const payload = jwtToken.split('.')[1];
        const decoded = JSON.parse(atob(payload));
        const request = JSON.parse(decoded.request);

        request.challenge = base64urlToUint8Array(request.challenge);
        request.user.id = base64urlToUint8Array(request.user.id);

        const credential = await navigator.credentials.create({ publicKey: request });

        await finishRegistration(user, jwtToken, credential);

    } catch (err) {
        console.error(err);
    }
});

/**
 * @param {UserDto} user
 */
async function startRegistration(user) {
    return await fetch(`${BASE_API_URL}/auth/register/start`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            user,
        })
    }).then(response => response.json());
}

/**
 * @param {UserDto} user
 * @param {String} jwtToken
 * @param {Credential} publicKeyCredential
 */
async function finishRegistration(user, jwtToken, publicKeyCredential) {
    return await fetch(`${BASE_API_URL}/auth/register/finish`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            user,
            jwtToken,
            publicKeyCredential,
        })
    });
}

function base64urlToUint8Array(base64url) {
    const padding = '='.repeat((4 - base64url.length % 4) % 4);
    const base64 = (base64url + padding).replace(/-/g, '+').replace(/_/g, '/');
    const rawData = atob(base64);
    const outputArray = new Uint8Array(rawData.length);
    for (let i = 0; i < rawData.length; ++i) {
        outputArray[i] = rawData.charCodeAt(i);
    }
    return outputArray;
}
