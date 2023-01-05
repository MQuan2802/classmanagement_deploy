<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Health Check</title>
 <style type="text/css"> 
    form {display: inline-block; } 
    p {display:inline-block}
</style> 
</head>
<body>
    <h3>register form</h3>
    <p>${result}</p><br>
    <form action=${registerpath} method="post" accept-charset="utf-8" modelAttribute="ahihi"> 
        ${insufficientusername}
        <p>username</p><br>
        <input type="text"  name="username"><br>
        <p>password</p><br>
        <input type="password" name="password"><br>
        <p>${passwordnotconfirm}</p>
        <p>confirm password</p><br>
        <input type="password" name="confirmpassword"><br>
        <p>${insufficientdateofbirth}</p>
        <p>Date of birth</p><br>
        <input type="date" name="dateofbirth"><br>
        <p>${insufficientfullname}</p>
        <p>full name</p><br>
        <input type="text" name="fullname"><br>
        <p>${insufficientID}</p>
        <p>Identity</p><br>
        <input type="text" name="id"><br>
        <p>${sufficientemail}</p>
        <p>email</p><br>
        <input type="email" name="email"><br>
        <input type="submit" value="submit">
        </form><br>
</body>
</html>