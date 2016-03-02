<%-- 
    Document   : vernotas
    Created on : 01-mar-2016, 17:20:10
    Author     : Gonzalo
--%>

<%@page import="hibernate.Keep"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Keep> listanotas = (List<Keep>) request.getAttribute("listado");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Notas de Keep</title>
    </head>
    <body>
        <h1>Notas de la aplicación Keep:</h1>
        <a href="crearnota.jsp?login=<%= request.getParameter("login")%>"><h3>Añadir una nueva nota</h3></a>
        <table border="1">
            <tr>
                <th>ID Nota</th>
                <th>ID Android</th>
                <th>Usuario</th>
                <th>Nota</th>
                <th>Estado</th>
                <th>Editar</th>
                <th>Borrar</th>
            </tr>

            <%
                for (Keep p : listanotas) {
            %>
            <tr>
                <td><%= p.getId()%></td>
                <td><%= p.getIdAndroid()%></td>
                <td><%= p.getUserName()%></td>
                <td><%= p.getContenido()%></td>
                <td><%= p.getEstado()%></td>
                <td><a href="editarnota.jsp?id=<%= p.getId()%>&login=<%= request.getParameter("login")%>">Editar</a></td>
                <td><a href="go?tabla=keep&origen=web&op=delete&accion=&id=<%= p.getId()%>&login=<%= request.getParameter("login")%>">Eliminar</a></td>
            </tr>
            <%
                }
            %>          
        </table>

    </body>
</html>
