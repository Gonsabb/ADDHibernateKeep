<%-- 
    Document   : crearnota
    Created on : 01-mar-2016, 17:29:42
    Author     : Gonzalo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crear nota</title>
    </head>
    <body>
        <h1>Crea una nota:</h1>
        <form action="go">
            <p>Nota:</p>
            <textarea name="contenido" rows="3" cols="25"></textarea>

            <input type="submit" value="submit"/>

            <input type="hidden" name="accion" value="" />
            <input type="hidden" name="op" value="create" />
            <input type="hidden" name="login" value="<%= request.getParameter("login")%>" />
            <input type="hidden" name="origen" value="web" />
            <input type="hidden" name="tabla" value="keep" />
        </form>
    </body>
</html>
