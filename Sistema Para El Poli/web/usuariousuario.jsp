<%-- 
    Document   : usuariousuario
    Created on : 25-mar-2020, 18:50:22
    Author     : user
--%>

<%@page import="java.io.OutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conexion.conectar"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Administrador</title>
        <link rel="icon"    href="imagenes/escudo.jpg">
        <link rel="stylesheet" type="text/css" media="screen" href="css/style.css">
        <script src="js/jquery.easing.1.3.js"></script>
        <script src="jquery.js"></script>
        <script>
            $(fuction(){
                $()
            }
            })
            
        </script>
    </head>
    <body>
        <h1>Hello World!</h1>
        <% conectar con = new conectar();
           OutputStream omg = con.VerImagen();
        %>

        <img src="+<% out.println(omg); %>+"alt="Smiley face" height="42" width="42">
        <form action="CambiarImagen" method="post" enctype="multipart/form-data" target="_blank">
         
            <p>

                Cambiar Imagen

                <input type="file" name="imagensubida" accept="image/png, .jpeg, .jpg, image/gif">

                <input type="submit" value="Enviar datos">

            </p>

        </form>

    </body>
</html>
