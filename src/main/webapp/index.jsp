<%-- 
    Document   : index
    Created on : 02-11-2025, 2:06:22 a. m.
    Author     : rgallardo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Inicio de Sesión - My Construction</title>
</head>
<body>
    <h1>Acceso al Sistema</h1>
    <form action="LoginServlet" method="POST">
        <label for="username">Usuario:</label>
        <input type="text" id="username" name="username" required><br><br>
        
        <label for="password">Contraseña:</label>
        <input type="password" id="password" name="password" required><br><br>
        
        <input type="submit" value="Ingresar">
    </form>
    
    <% if (request.getAttribute("error") != null) { %>
        <p style="color:red;">Mensaje: <%= request.getAttribute("error") %></p>
    <% } %>
</body>
</html>
