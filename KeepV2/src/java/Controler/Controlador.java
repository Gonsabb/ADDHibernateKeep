/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controler;

import gestores.GestorKeep;
import gestores.GestorUsuario;
import hibernate.Keep;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author Gonzalo
 */
@WebServlet(name = "Controlador", urlPatterns = {"/go"})
public class Controlador extends HttpServlet {

    enum Camino {

        forward, redirect, print
    }

    class Destino {

        public Camino camino;
        public String url, texto;

        public Destino() {
        }

        public Destino(Camino camino, String url, String texto) {
            this.camino = camino;
            this.url = url;
            this.texto = texto;
        }
    }

    private Destino handle(HttpServletRequest request, HttpServletResponse response, String tabla, String op, String accion, String origen) {
        if (origen == null) {
            origen = "";
        };
        //login=&
        //pass=&
        //tabla=usuario&
        //accion=view&
        //op=login&
        //origen=web
        if (tabla == null || op == null || accion == null) {
            tabla = "usuario";
            op = "login";
            accion = "view";
        }
        System.out.println("seguro");
        switch (tabla) {
            case "usuario": {
                System.out.println("me voy al case usuario");
                return handleUsuario(request, response, op, accion, origen);

            }
            case "keep": {
                return handleKeep(request, response, op, accion, origen);
            }
            default:
        }
        return null;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tabla = request.getParameter("tabla"); //persona, idioma, etc.
        String op = request.getParameter("op"); //create, read, update, delete, login
        String accion = request.getParameter("accion"); //view, do
        String origen = request.getParameter("origen"); //android, etc.
        Destino destino = handle(request, response, tabla, op, accion, origen);
        if (destino == null) {
            destino = new Destino(Camino.forward, "/WEB-INF/index.jsp", null);
        }
        if (destino.camino == Camino.forward) {
            request.getServletContext().getRequestDispatcher(destino.url).forward(request, response);
        } else if (destino.camino == Camino.redirect) {
            response.sendRedirect(destino.url);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println(destino.texto);
            }
        }
    }

    private Destino handleUsuario(HttpServletRequest request, HttpServletResponse response, String op, String accion, String origen) {
        switch (op) {
            case "login":
                System.out.println("login");
                if (origen.equals("android")) {
                    JSONObject obj = GestorUsuario.getLogin(request.getParameter("login"), request.getParameter("pass"));
                    return new Destino(Camino.print, "", obj.toString());
                    //http://192.168.208.208:8080/HibernateV3/go?tabla=usuario&op=login&user=pepe&pass=pepe&origen=android&accion=
                }
                if (origen.equals("web")) {
                    System.out.println("web");
                    JSONObject obj = GestorUsuario.getLogin(request.getParameter("login"),
                            request.getParameter("pass"));
                    System.out.println("He entrado en web");
                    if (obj.getBoolean("r")) {
                        List<Keep> notas = GestorKeep.listarNotas(request.getParameter("login"));
                        request.setAttribute("listado", notas);
                        request.setAttribute("login", GestorUsuario.getUserBN(request.getParameter("login")));
                        Destino dest = new Destino(Camino.forward, "/vernotas.jsp", notas.toString());
                        return dest;
                    } else {
                        return new Destino(Camino.print, "/index.html", obj.toString());
                    }
                }
        }
        return null;
    }

    private Destino handleKeep(HttpServletRequest request, HttpServletResponse response,
            String op, String accion, String origen) {

        switch (op) {
            case "create":
                if (origen.equals("android")) {
                    Keep nota = new Keep(null, request.getParameter("idAndroid"),
                            request.getParameter("contenido"), null, "estable");
                    JSONObject obj = GestorKeep.registrarNota(nota,
                            request.getParameter("login"));
                    return new Destino(Camino.print, "", obj.toString());
                }
                if (origen.equals("web")) {
                    List<Keep> notas = GestorKeep.listarNotas(request.getParameter("login"));
                    int max = -1;
                    max++;
                    for (Keep nota : notas) {
                        if (nota.getIdAndroid() >= max) {
                            max = nota.getIdAndroid() + 1;
                        }
                    }
                    Keep nota = new Keep(null, max, request.getParameter("contenido"), null, "estable");
                    JSONObject obj = GestorKeep.registrarNota(nota, request.getParameter("login"));
                    notas = GestorKeep.listarNotas(request.getParameter("login"));
                    request.setAttribute("listado", notas);
                    return new Destino(Camino.forward, "/vernotas.jsp", notas.toString());
                }
            case "read":
                if (origen.equals("android")) {
                    JSONObject obj = GestorKeep.obtenerNotas(request.getParameter("login"));
                  
                    return new Destino(Camino.print, "", obj.toString());
                }
            case "delete":
                if (origen.equals("android")) {
                    Keep nota = new Keep(null, request.getParameter("idAndroid"),
                            request.getParameter("contenido"), null, "estable");
                    JSONObject obj = GestorKeep.eliminarNota(nota, request.getParameter("login"));
                    return new Destino(Camino.print, "", obj.toString());
                }
                if (origen.equals("web")) {
                    System.out.println("pepepepepe");
                    GestorKeep.eliminarNotaWeb(Integer.parseInt(request.getParameter("id")));
                    List<Keep> notas = GestorKeep.listarNotas(request.getParameter("login"));
                    request.setAttribute("listado", notas);
                    return new Destino(Camino.forward, "/vernotas.jsp", notas.toString());
                }
            case "update":
                if (origen.equals("web")) {
                    GestorKeep.actualizarNota(Integer.parseInt(request.getParameter("id")), request.getParameter("contenido"));
                    List<Keep> notas = GestorKeep.listarNotas(request.getParameter("login"));
                    request.setAttribute("listado", notas);
                    return new Destino(Camino.forward, "/vernotas.jsp", notas.toString());
                }
        }

        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
