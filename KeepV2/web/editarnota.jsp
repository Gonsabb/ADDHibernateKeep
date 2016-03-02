<%-- 
    Document   : editarnota
    Created on : 01-mar-2016, 17:31:58
    Author     : Gonzalo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editor de Notas</title>
    </head>
    <body>
        <h1>Edita tu nota aquí:</h1>
        <form action="go">
            <p>Contenido:</p>
            <textarea name="contenido" rows="3" cols="25"></textarea>
            <input type="submit" value="Submit" />
           
            <input type="hidden" name="accion" value="" />
            <input type="hidden" name="op" value="update" />
            <input type="hidden" name="origen" value="web" />
            <input type="hidden" name="tabla" value="keep" />
            <input type="hidden" name="id" value="<%= request.getParameter("id") %>" />
            <input type="hidden" name="login" value="<%= request.getParameter("login") %>" />
        </form>
        
    </body>
</html>
