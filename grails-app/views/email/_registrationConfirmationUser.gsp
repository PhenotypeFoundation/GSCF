<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Welcome to GSCF</title>
  </head>
  <body>
      <h3>Hello ${username}</h3
      <p>Welcome to GSCF. Your account has been created, but you have to confirm your registration. Please click <a href="${link}">here</a> to confirm your registration.</p>
      <p>If you can not click the link, copy this url to the browser:</p>
      <p>${link}</p>
      <p>After you have confirmed your registration and the administrator has approved your account, you can login. Your password is:</p>
      <p><b>${password}</b></p>
      <p>Kind regards,</p>
      <p>The GSCF team</p>
  </body>
</html>
