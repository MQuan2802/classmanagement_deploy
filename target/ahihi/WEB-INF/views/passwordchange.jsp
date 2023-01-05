<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>change password</title>
</head>
<body>
    <h3>Change password</h3>
    <form action="/ahihi/changepassword" method="post" accept-charset="utf-8" enctype="application/x-www-form-urlencoded" > 
       
        <p>${insufficientchangepassword}</p><br>
        <p>old-password</p><br>
        <input type="password"  name="oldpassword" ><br>
        <p>new-password</p>
        <input type="password"  name="newpassword"><br>
        <p>confirm new-password</p>
        <input type="password"  name="confirmnewpassword"><br>
        <input type="submit" value="re-send">
    </form>
</body>
</html>