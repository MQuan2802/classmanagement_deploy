<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>reset password</title>
</head>
<body>
    <h3>reset password</h3>
    <form action="/ahihi/resetpassword" method="post" accept-charset="utf-8" > 
        <p>${resetpassword}</p><br>
        <p>new password</p><br>
        <input type="hidden" name="token" value=${token}>
        <input type="password" name="newpassword"><br>
        <input type="password" name="confirmnewpassword"><br>
        <input type="submit" value="reset-password">
    </form>
</body>
</html>