<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>recover password</title>
</head>
<body>
    <h3>recover password</h3>
    <form action="/ahihi/sendmailresetpassword" method="post" accept-charset="utf-8" > 
        <p>input email to send recover form</p><br>
        <p>${forgetpasswordresult}</p><br>
        <p>email</p><br>
        <input type="email" name="email"><br>
        <input type="submit" value="send">
    </form>
</body>
</html>