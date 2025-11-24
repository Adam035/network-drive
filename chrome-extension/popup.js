const backendUrl = "http://localhost:8080";

document.getElementById("registerBtn").addEventListener("click", async () => {
    const email = document.getElementById("email").value;
    const username = document.getElementById("username").value;

    try {
        const startResp = await fetch(`${backendUrl}/auth/register/start`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                user: {
                    username,
                    email
                }
            })
        });

        const { jwtToken } = await startResp.json();
        console.log("JWT token:", jwtToken);
        const payload = jwtToken.split('.')[1];
        const decoded = JSON.parse(atob(payload));
        const request = JSON.parse(decoded.request);
        console.log("request:", request);

        request.challenge = base64urlToUint8Array(request.challenge);
        request.user.id = base64urlToUint8Array(request.user.id);

        const credential = await navigator.credentials.create({ publicKey: request });
        console.log("Credential:", credential);

        console.log("JWT token:", jwtToken);
        const finishResp = await fetch(`${backendUrl}/auth/register/finish`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                user: {
                    username,
                    email
                },
                jwtToken,
                publicKeyCredential: credential
            })
        });

        console.log(finishResp);

    } catch (err) {
        console.error(err);
        alert("Error: " + err.message);
    }
});

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
