
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html"%>

<s:if test="#session.admin != 'true'">
    <jsp:forward page="index.jsp" />
</s:if>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../hak_static/styles/adapt/master.css" />
        <link rel='stylesheet' href='../hak_static/styles/administrar.css'>

        <script src="../hak_static/js/jquery-1.8.2.min.js"></script>
        <script>

            var ADAPT_CONFIG = {
                path: '../hak_static/styles/adapt/',
                dynamic: true,
                range: [
                    '0px    to 760px  = mobile.min.css',
                    '760px  to 980px  = 720.min.css',
                    '980px  to 1280px = 960.min.css',
                    '1280px to 1600px = 1200.min.css',
                    '1600px to 1940px = 1560.min.css',
                    '1940px to 2540px = 1920.min.css',
                    '2540px           = 2520.min.css'
                ]
            };

            function action(button, action) {
                $(button).click(function() {
                    $.ajax({
                        url: "/game/" + action + ".action", // action to be perform
                        type: 'GET', //type of posting the data
                        success: function(response) {
                            console.log(response);
                            return response;
                        },
                        error: function(xhr, ajaxOptions, thrownError) {
                            alert('An error occurred! ' + thrownError);
                        }
                    });
                });
            }

        </script>

        <script src="../hak_static/js/adapt/adapt.min.js"></script>
    </head>
    <body class='container_12'>

        <div class="grid_12">
            <h1>
                <div id="nota" style="float: right"></div>
                ADMINISTRADOR
            </h1>

        </div>

        <div class="grid_12"><br/></div>

        <div class="grid_3 administrar_grid">

            <h4>opciones GENERALES</h4><br/>            

            <p>
                Reiniciar lista de Precios:
                <button id="reiniciarPrecios">reiniciar</button>
            </p>
            <script>
            action($("#reiniciarPrecios"), "reiniciarPrecios");
            </script>
            <br/>

            <p>
                Anular batallas (anulando las alineaciones de las batallas):
                <s:form action="anularBatallas" theme="simple">
                    <s:submit value="anular" align="right"/>
                </s:form>
            </p><br/>

        </div>

        <div class="grid_3 administrar_grid">
            <h4>Resolver BUGS</h4><br/>

            <p>
                Vaciar puntuaciones de liga
                <s:form action="vaciarPuntuaciones" theme="simple">
                    <s:submit value="vaciar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Vaciar todas las Batallas:
                <s:form action="vaciarBatallas" theme="simple">
                    <s:submit value="vaciar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Vaciar Ultimas Batallas:
                <s:form action="vaciarUltimasBatallas" theme="simple">
                    <s:submit value="vaciar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Eliminar todas las Batallas:
                <s:form action="eliminarBatallas" theme="simple">
                    <s:submit value="vaciar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Generar variable evolucion en criaturas sin evolución:
                <s:form action="saveEvol" theme="simple">
                    <s:submit value="activar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Reiniar todas las variables evolucion:
                <s:form action="updateEvol" theme="simple">
                    <s:submit value="activar" align="right"/>
                </s:form>
            </p><br/>

        </div>

        <div class="grid_3 administrar_grid">
            <h4>ESPECIALES</h4><br/>

            <p id="rectificarPremios">
                Rectificar premios de victoria en base a los mensajes:<br/>
                <input id="mensajeInicio" class="date" type="text"><br/>
                <input id="mensajeFin" class="date" type="text"><br/>                
                <input id="rectificarBoton" type="button" value="rectificar">                
            </p><br/>

        </div>

        <div id="local" class="grid_3 administrar_grid">
            <h4>tests solo LOCAL</h4><br/>

            <p>
                Reiniciar todas las DB:
                <s:form action="reiniciar" theme="simple">
                    <s:submit value="reiniciar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Probar Entreno:
                <s:form action="probarEntreno" theme="simple">
                    <s:submit value="probar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Probar Mazmorras:
                <s:form action="probarMazmorras" theme="simple">
                    <s:submit value="probar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Llenar Mazmorras:
                <s:form action="llenarMazmorras" theme="simple">
                    <s:submit value="llenar" align="right"/>
                </s:form>
            </p><br/>

            <p>
                Activar todos los Usuarios inactivos:
                <s:form action="activarUsuarios" theme="simple">
                    <s:submit value="activar" align="right"/>
                </s:form>
            </p><br/>

        </div>

    </body>
</html>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

<script>

            var domain = document.domain
            if (domain.indexOf("heroesandkillers") != -1) {
                $("#local").css("display", "none");
                $("#nota").html("cuidado, estás on-line!");
            }

            $(".date").datepicker();
            $(".date").datepicker("option", "dateFormat", "yy-mm-dd");
            $("#rectificarPremios").find("#rectificarBoton").click(function() {

                var form = $(this).parent();
                var inicio = form.find("#mensajeInicio");
                var fin = form.find("#mensajeFin");
                if (inicio.val() == "" || fin.val() == "") {
                    alert("no hay datos");
                } else {
                    $.ajax({
                        type: "GET",
                        url: "rectificarPremios",
                        data: {
                            inicio: form.find("#mensajeInicio").val(),
                            fin: form.find("#mensajeFin").val()
                        },
                        complete: function() {
                            alert("completado");
                        }
                    });
                }
            });

</script>
