<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>successful register</title>
</head>
<body>
    <h3>Your account had been register. Verification form had been sent to your email</h3>
    <form action="/ahihi/reverification" method="post" accept-charset="utf-8" > 
        <p>input email to re sent verification form</p>
        <p>${insufficientverficationemail}</p>
        <input type="email" name="email"><br>
        <input type="submit" value="re-send">
    </form>
</body>
</html>