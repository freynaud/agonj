<!DOCTYPE html>
<html manifest="/static/map.appcache">
<head>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
<style type="text/css">
    html {
        height: 100%
    }

    body {
        height: 100%;
        margin: 0;
        padding: 0
    }

    #map_canvas {
        height: 100%
    }
</style>
<script type="text/javascript"
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCR4MolzbC6C3Gt905BWiyn-1Usuqft_AY&sensor=true">
</script>
<script type="text/javascript"
        src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js">
</script>
<script type="text/javascript"
        src="/static/js/marker.js">
</script>

<script type="text/javascript">

    // adding an overlay.
    function CoordMapType(tileSize) {
        this.tileSize = tileSize;
    }

    CoordMapType.prototype.getTile = function (coord, zoom, ownerDocument) {
        //var normalizedCoord = getNormalizedCoord(coord, zoom);
        //var div = ownerDocument.createElement('div');
        //if (!normalizedCoord) {
        //div.innerHTML = " ";
        //div.style.backgroundColor = 'grey';
        //div.zIndex = 1000
        //} else {
        //div.innerHTML = "<br/><br/>Overlay:" + coord + 'z:' + zoom;
        //}
        //div.style.width = this.tileSize.width + 'px';
        //div.style.height = this.tileSize.height + 'px';
        //div.style.fontSize = '10';
        //div.style.borderStyle = 'solid';
        //div.style.borderWidth = '1px';
        //div.style.borderColor = 'red';

        //return div;

    };

    var storage = function () {
        if (typeof(Storage) !== "undefined") {
            //console.log('enabled!');
        }
        else {
            //console.log('Sorry! No web storage support.. ');
            alert('Sorry! No web storage support.. ');
        }
    }

    var moonTypeOptions = {
        getTileUrl: function (coord, zoom) {
            var normalizedCoord = getNormalizedCoord(coord, zoom);
            if (!normalizedCoord) {
                return null;
            }
            var bound = Math.pow(2, zoom);
            var path = "/static/data/" + zoom + "/" + zoom + "-" + normalizedCoord.x + "-"
                               + normalizedCoord.y + ".png";
            return path;

        },
        tileSize: new google.maps.Size(184, 184),
        maxZoom: 6,
        minZoom: 0,
        //radius: 1738000,
        name: "Agon"
    };

    var moonMapType = new google.maps.ImageMapType(moonTypeOptions);

    function placeMarker(location) {
        //$('#right-col').html("click  : " + "lat:" + location.lat() + "long:"
        //                             + location.lng());
        var marker = new google.maps.Marker({   position: location,
                                                draggable: true,
                                                animation: google.maps.Animation.DROP,
                                                map: document.map,

                                                title: "Tmp title"
                                            });
        var myMarker = new AgonMarker(marker);
        myMarker.displayDetails();
    }

    function initialize() {
        storage();
        var myLatlng = new google.maps.LatLng(0, 0);
        var mapOptions = {
            center: myLatlng,
            zoom: 2,
            streetViewControl: false,
            mapTypeControlOptions: {
                mapTypeIds: ["Agon"]
            }
        };

        var map = new google.maps.Map(document.getElementById("map_canvas"),
                                      mapOptions);
        document.map = map;
        alert("document.map set to " + document.map);
        map.mapTypes.set('Agon', moonMapType);
        map.setMapTypeId('Agon');

        /*var marker = new google.maps.Marker({
         position: new google.maps.LatLng(85, -179),
         map: map,
         title: "Hello World!"
         });    */

        google.maps.event.addListener(map, 'click', function (event) {
            placeMarker(event.latLng);
        });

        map.overlayMapTypes.insertAt(0, new CoordMapType(new google.maps.Size(184, 184)));

    }

    function getNormalizedCoord(coord, zoom) {
        var y = coord.y;
        var x = coord.x;

        // tile range in one direction range is dependent on zoom level
        // 0 = 1 tile, 1 = 2 tiles, 2 = 4 tiles, 3 = 8 tiles, etc
        var tileRange = 1 << zoom;

        // don't repeat across x,y-axis
        if (y < 0 || x < 0 || x >= tileRange || y >= tileRange) {
            return null;
        }

        return {
            x: x,
            y: y
        };
    }

    $(document).ready(function () {
        initialize();

        $.ajax({
                   url: 'http://localhost:5000/agon/map/get?markers={"id":47}',
                   type: "GET",
                   datatype: "jsonp",
                   success: function (response, textStatus, jqXHR) {
                       console.log("Response : " + response)
                       console.log("Response.title : " + response.title)
                       if (document.map) {
                           var map = document.map;
                       } else {
                           alert(document.map + " document.map...")
                       }

                       var position = new google.maps.LatLng(84, 0);
                       var title = response.title;

                       var marker = new google.maps.Marker({position: position,
                                                               map: map,
                                                               title: title
                                                           });
                   }
               });

        $('#type input').change(function () {
            document.currentMarker.setType($(this).val());
        });

        $('#title').change(function () {
            document.currentMarker.setTitle($(this).val());
        });

        $('#save').click(function () {
            console.log("save!")
        });
    });


</script>

<style>


    #right-col {
        position: absolute;
        top: 0;
        right: 0;
        width: 250px;
    }


</style>
</head>
<body>
<div id="map_canvas" style="width:80%; height:100%"></div>
<div id="right-col"> details

    <form id="detailsForm">

        <input id='title'/><br>
        <textarea id='notes' style="width:80%; height:150px"> </textarea> <br>


        <div id='type'>
            <input type="radio" name="type" value="BEAR" id="BEAR"><img
                src="/static/icons/bear.png">animal<br>
            <input type="radio" name="type" value="MOB" id="MOB"><img src="/static/icons/mob.png">mob<br>
            <input type="radio" name="type" value="FARM" id="FARM"><img
                src="/static/icons/farm.png">farm<br>
            <input type="radio" name="type" value="VILLAGE" id="VILLAGE"><img
                src="/static/icons/village.png">village<br>
        </div>
        <input id="save" type="button" value='save'>
        <br/>
        debug:<br/>
        id: <input id='id'/> <br>
        lat: <input id='lat'/> <br>
        lng:<input id='lng'/> <br>
    </form>
</div>


</body>
</html>