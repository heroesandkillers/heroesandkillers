
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>campoPruebas</title>

        <script src="js/batalla/Capas.js"></script>

        <script>
                    
            var equipo1 = [];
            var equipo2 = [];
                
            function colocarPruebas(){
                       
                var equipo, k;
                
                for(var i = 0; i < 5; i++){
                    equipo = 1;
                    k = $("#"+equipo+i);
                    equipo1[i] = getValuesPruebas(equipo, k, i);
                
                    equipo = 2;
                    k = $("#"+equipo+i);
                    equipo2[i] = getValuesPruebas(equipo, k, i);
                }
            }
            
            function getValuesPruebas(equipo, k, i){
                
                return  equiparPrueba(
                i, equipo, k.find(".x").val(), k.find(".y").val(), k.find(".dano").val(), k.find(".ataque").val(),
                k.find(".vida").val(), k.find(".magia").val(), k.find(".armadura").val(),
                k.find(".defensa").val(), k.find(".velocidad").val(), k.find(".iniciativa").val()); 
                
            }
                        
            function equiparPrueba(equipo, i, x, y, dano, ataque, vida, magia, armadura, defensa, velocidad, iniciativa){
                        
                //                var criatura = {};
                //    
                //                criatura.dano = dano;
                //                criatura.ataque = 30 - ataque/4;
                //                criatura.armadura = (100 - (armadura / 2)) /100;
                //                criatura.velocidad = velocidad /2 + 3;
                //                criatura.reaccion = 20 - iniciativa/8;
                //                criatura.alcance = 25;
                //                criatura.alto = 25;
                //                criatura.ancho = 25;
                //                criatura.arma = 'ninguna';
                //                                                
                //                criaturas[k] = criatura;
                         
                //                         {"id":81,"user":9,"raza":"humano","dano":8,"ataque":5,"magia":2,"vida":2,"armadura":9,"defensa":8,"velocidad":7,"iniciativa":6,"exp":10,"fisico":50,"frescura":100,"arma":"ninguna","personalidad":"","alcance":30,"x":0,"y":1},
                        
                var criaturaRetorno = {
                    y: y,
                    x: x,
                    
                    id: equipo + i,
                    user: 'bot'+equipo,
                    raza:'humano',
                    
                    dano: dano,
                    ataque: ataque,
                    magia: magia,
                    vida: vida,
                    armadura: armadura,
                    defensa: defensa,
                    velocidad: velocidad,
                    iniciativa: iniciativa,
                    
                    exp:10,
                    fisico:50,
                    frescura:100,
                    arma:'ninguna',
                    alcance:30
                
                }
                return criaturaRetorno;
            }
                        
                        
            var base = new Image();
            var baseRojo = new Image();
            
            var escalaCanvasBatalla = (function(){
                var gameCanvas = $("#container");
     
                var gameArea = $("#gameArea");
                var widthToHeight = 2;
                var newWidth = gameArea.width();
                var newHeight = gameArea.height();
                var newWidthToHeight = newWidth / newHeight;
        
                gameCanvas.css("margin-top",(-(newHeight-gameArea.clientHeight) / 2) + 'px');
                gameCanvas.css("margin-left", (-(newWidth-gameArea.clientWidth) / 2) + 'px');
                
                if (newWidthToHeight > widthToHeight) {
                    newWidth = newHeight * widthToHeight;
//                    gameCanvas.height(newHeight);
                    gameCanvas.width(newWidth);
            
                    return gameArea.height() / 800;
            
                } else {
                    newHeight = newWidth / widthToHeight;
                    gameCanvas.width(newWidth);
//                    gameCanvas.height(newHeight);
            
                    return gameArea.width() / 1200;
                }
            })();
            
            var stage = null;
            var layerCriaturas;
            
            
            var batallas = {
                
                imagenes: ['img/base.png', 'img/baseRojo.png'],
                
                loader: function(imagenes, thingToDo, allDone) {
                    if (!imagenes) {
                        return;
                    }
                    if ("undefined" === imagenes.length) {
                        imagenes = [imagenes];
                    }

                    var count = imagenes.length;

                    var thingToDoCompleted = function (imagenes, i) {
                        count--;
                        if (0 == count) {
                            allDone(imagenes);
                        }
                    };

                    for (var i = 0; i < imagenes.length; i++) {
                        thingToDo(imagenes, i, thingToDoCompleted);
                    }
                },
                
                loadImage: function(imagenes, i, onComplete) {
                    var onLoad = function (e) {
                        e.target.removeEventListener("load", onLoad);

                        onComplete(imagenes, i);
                    }
                    var img = new Image();
                    img.addEventListener("load", onLoad, false);
                    img.src = imagenes[i];
                }                
            }
            
            var loadBatalla;

            function verBatalla(){
                colocarPruebas();
                loadBatalla = (function (){
                    return calcularBatalla(equipo1, equipo2);
                })();
            
                batallas.imagenes = ['img/base.png', 'img/baseRojo.png'];
                loaderBatalla.start(function(){
                    $('#verBatalla').addClass('noClick');
                    batallas.loader(batallas.imagenes, batallas.loadImage, function() {
                        runBatalla();
                    });
                });
                base.src = "img/base.png";
                baseRojo.src = "img/baseRojo.png";
            };
            
            function reiniciarBatalla(){
                clearInterval(controlIntervaloBatalla);
                frame = 0;
                if (batallaTerminada){
                    for(var j = 0; j < layerCriaturas.get('.group').length; j++){
                        var grupo = layerCriaturas.get('.group')[j];
                        var sprite = grupo.get('.sprite')[0];
                        sprite.start();
                    }
                }
                var arrayRects = layerCriaturas.get('.rect');
                for(var i = 0; i < arrayRects.length; i++){
                    arrayRects[i].setVisible(true);
                }
                controlIntervaloBatalla = setInterval(intervaloBatalla,60);
            }
 
            var loaderBatalla = {
                start: function(callback){
                    $("#loaderBatalla").css('display', 'inherit');
                    callback();
                },
                stop: function(){
                    $("#loaderBatalla").css('display', 'none');
                }
            }
            
            var controlIntervaloBatalla;
            var batallaTerminada = false;
            
            function runBatalla(){
                stage = new Kinetic.Stage({
                    container: "container",
                    width: $("#gameArea").width(),
                    height: $("#gameArea").height(),
                    draggable: false
                });
                
                layerCriaturas = crearCapas(loadBatalla[0]);
    
                stage.add(layerCriaturas);
                stage.setScale(escalaCanvasBatalla);
                
                clearInterval(controlIntervaloBatalla);
                loaderBatalla.stop();
                controlIntervaloBatalla = setInterval(intervaloBatalla,60);
    
                for(var i = 0; i < layerCriaturas.get('.group').length; i++){   
                    layerCriaturas.get('.group')[i].on('click', function() {
                        
                        for(var i = 0; i < layerCriaturas.get('.group').length; i++){
                            layerCriaturas.get('.group')[i].get(".rect")[0].setStroke('black');
                        }
                        
                        this.get(".rect")[0].setStroke('white');
          
                        var oval = layerCriaturas.get('.oval')[0];
            
                        oval.remove();
                        this.add(oval);            
                        oval.moveToBottom();
                        oval.setVisible(true);
                        index = layerCriaturas.get('.group').indexOf(this);
                        
                    });
                }
            }
            
            var criaturasBatalla;
            var frame = 0;
            var index = -1;
            
            function intervaloBatalla(){
        
                if (loadBatalla.length == frame) {
                    for(var j = 0; j < layerCriaturas.get('.group').length; j++){
                        var grupo = layerCriaturas.get('.group')[j];
                        var sprite = grupo.get('.sprite')[0];
                        sprite.stop();
                    }
                    frame = 0;
                    clearInterval(controlIntervaloBatalla);
                    batallaTerminada = true;
                }
        
                criaturasBatalla = loadBatalla[frame];
                
                for (var i = 0; i < criaturasBatalla.length; i++) {
                    IAsprite(criaturasBatalla[i], i)
                }
        
                //                if(index > -1){
                //                    criaturaId = criaturas[criaturasBatalla[index].id];
                //                    //document.getElementById("id").innerHTML = criaturasBatalla[index].id;
                //                    if(criaturasBatalla[index].equipo == 1){
                //                        document.getElementById("equipo").innerHTML = batalla.eqLoc;
                //                    }else{
                //                        document.getElementById("equipo").innerHTML = batalla.eqVis;
                //                    }
                //                    document.getElementById("dano").innerHTML = criaturaId.dano;
                //                    document.getElementById("ataque").innerHTML = criaturaId.ataque;
                //                    document.getElementById("vida").innerHTML = parseInt(criaturasBatalla[index].vida);
                //                    document.getElementById("magia").innerHTML = criaturaId.magia;
                //                    document.getElementById("armadura").innerHTML = criaturaId.armadura;
                //                    document.getElementById("defensa").innerHTML = criaturaId.defensa;
                //                    document.getElementById("velocidad").innerHTML = criaturaId.velocidad;
                //                    document.getElementById("iniciativa").innerHTML = criaturaId.iniciativa;
                //                    
                //                    document.getElementById("param").innerHTML = $.param(criaturasBatalla[index]);
                //                    document.getElementById("sprite").innerHTML = criaturasBatalla[index].sprite;
                //                    document.getElementById("estado").innerHTML = 
                //                }
                document.getElementById("param").innerHTML = $.param(criaturasBatalla[0]);

                frame++;
            }
            
            $('input').val(0);
            
            $('#10 .x').val(0);
            $('#10 .y').val(0);
            $('#11 .x').val(0);
            $('#11 .y').val(1);
            $('#12 .x').val(0);
            $('#12 .y').val(2);
            $('#13 .x').val(0);
            $('#13 .y').val(3);
            $('#14 .x').val(0);
            $('#14 .y').val(4);
            
            
            $('#20 .x').val(1);
            $('#20 .y').val(0);
            $('#21 .x').val(1);
            $('#21 .y').val(1);
            $('#22 .x').val(1);
            $('#22 .y').val(2);
            $('#23 .x').val(1);
            $('#23 .y').val(3);
            $('#24 .x').val(1);
            $('#24 .y').val(4);
            
            $('#10 .vida').val(1);
            $('#20 .vida').val(1);
            $('#10 .dano').val(1);
            $('#20 .dano').val(1);
            
            
        </script>

        <style>
            .equipoPruebas {
                margin-top:20px;
            }
            .equipoPruebas div{
                float:left; 
                min-width:20%
            }
            .equipoPruebas div p{
                text-align: right;
            }
            .equipoPruebas div p input{
                width: 35px;
            }
        </style>

    </head>
    <body>

        <div class="grid_8 contenedor" id="gameArea" style="height: 100%">

            <div id="estadisticas">
                <div>Equipo: <span  id="equipo"></span></div>
                <div>Daño: <span id="dano"></span></div>
                <div>Vida: <span id="vida"></span></div>
                <div><span id="param"></span></div>
            </div>

            <div id="container"></div>

            <div id="loaderBatalla" style="text-align:center; display: none">
                <!--                <img src="img/loader.gif"/>-->
            </div>

        </div>

        <div class="grid_4 contenedor" style="height: 100%">
            <div class="titulo">DATOS DE LA BATALLA</div>

            <div class="cuerpo tabla">
                <div class="subtitulo"></div>
                <div class="scrollTabla">

                    <div class="equipoPruebas">
                        <p>EQUIPO 1:</p>

                        <div id ="10">
                            <p>criatura 1</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>
                        <div id ="11">
                            <p>criatura 2</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>
                        <div id ="12">
                            <p>criatura 3</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>
                        <div id ="13">
                            <p>criatura 4</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>
                        <div id ="14">
                            <p>criatura 5</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>

                    </div>

                    <hr/>

                    <div class="equipoPruebas">
                        <p>EQUIPO 2:</p>

                        <div id ="20">
                            <p>criatura 1</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>
                        <div id ="21">
                            <p>criatura 2</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>
                        <div id ="22">
                            <p>criatura 3</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>
                        <div id ="23">
                            <p>criatura 4</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>
                        <div id ="24">
                            <p>criatura 5</p>
                            <p>x<input class="x"></p>
                            <p>y<input class="y"></p>
                            <p>fuerza<input class="dano"></p>
                            <p>ataque<input class="ataque"></p>
                            <p>vida<input class="vida"></p>
                            <p>magia<input class="magia"></p>
                            <p>armadura<input class="armadura"></p>
                            <p>defensa<input class="defensa"></p>
                            <p>velocidad<input class="velocidad"></p>
                            <p>iniciativa<input class="iniciativa"></p>
                        </div>

                    </div>
                </div>


                <table class="botones">
                    <tr>
                        <td><div id="verBatalla" class="boton" onclick="verBatalla()">Ver Batalla</div></td>
                        <td><div id="reiniciarBatalla" class="boton" onclick="reiniciarBatalla()">Reiniciar</div></td>
                    </tr>
                </table>
            </div>

        </div>

    </body>

</html>
