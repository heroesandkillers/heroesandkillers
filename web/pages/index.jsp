<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <!--<meta charset="UTF-8">-->
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
        <title>Heroes and Killers</title>

        <script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
        <script type="text/javascript">

            $(document).ready(function() {
                var arr = document.URL.split('/');
                var result = arr[0] + "//" + arr[2];
                var url;
                var width = $(document).width();
//                var width = Math.max(document.documentElement.clientWidth, window.innerWidth || 0)
                console.log("java document width: " + width);
                if (width >= 760) {
                    url = result + "/hak_static/pages/loadIndex.html";
                    $("body").load(url);

                } else {
                    window.isMobile = true;
                    var org = "/hakMovil/assets";
                    url = result + org + "/pages/loadIndex_mob.html";

                    //every mobile versions will have only ajax connection
                    location.href = location.origin + "/hakMovil/assets/pages/index_mob.html";
                }
            });

        </script>
    </head>
    <body>

    </body>
</html>
