//starting the program and send enemy attack
function run() {
    shipType();

        $("#spaceShip").animate({
            top: 520,
            backgroundColor: "black",
            opacity:1
             
        }, {
            duration: 3500,
            progress: function (o, p, t) { compareForStop(); },

            easing: "linear",
            complete: function () { $("#spaceShip").css({"background-color":"black",top:0 + "px",opacity:0}) }
        });
    
}
//choose the position of war ships enemy
function shipPosition() {
    placeShip = Math.round(35 + Math.random() * 300);
    $("#spaceShip").css({ left: placeShip +"px"})
}

//count ships
var regularShip = 0;
var generalShip = 0;
var motherShip = 0;
var totalShip = 0;

//hit count
var hitRegularShip = 0;
var hitGeneralShip = 0;
var hitMotherShip = 0;
var totalHit = 0;


//select ship enemy type regular/generals/motherboard
var ship = 0;
function shipType()
{
    ship = Math.round(Math.random()* 100);
    if (ship <= 70) {
        $("#spaceShip").css({ "width": "15px", "height": "15px" })//create regular
        $("#spaceShip").html('<img src="./public/img/regship.bmp" />');

        regularShip = (regularShip + 1);
        document.getElementById("shipHit").innerHTML = hitRegularShip + "/" + regularShip;
        totalShip = (totalShip + 1);
        document.getElementById("totalShips").innerHTML = totalHit + "/" + totalShip;
    }
    if (ship > 70 && ship <= 90) {
        $("#spaceShip").css({ "width": "25px", "height": "25px" })// create generals
        $("#spaceShip").html('<img src="./public/img/generalShip.bmp" />');

        generalShip = (generalShip + 1);
        document.getElementById("generalHit").innerHTML = hitGeneralShip + "/" + generalShip;
        totalShip = (totalShip + 1);
        document.getElementById("totalShips").innerHTML = totalHit + "/" + totalShip;
    }
    if (ship > 90) {
        $("#spaceShip").css({ "width": "40px", "height": "40px" })//create motherboard
        $("#spaceShip").html('<img src="./public/img/motherShip.bmp" />');

        motherShip = (motherShip + 1);
        document.getElementById("motherHit").innerHTML = hitMotherShip + "/" + motherShip;
        totalShip = (totalShip + 1);
        document.getElementById("totalShips").innerHTML = totalHit + "/" + totalShip;


    }
    shipPosition();

}

//click start to play
function start() {
    //changing game instruction
    $("#header").html("השתמש בחצים כדי לזוז ולירות").css('color' , 'yellow');
   
    //start and loop background music
    myAudio = new Audio('./public/sound/spaceGameLoop_mp3.mp3'); 
    if (typeof myAudio.loop == 'boolean')
    {
        myAudio.loop = true;
    }
    else
    {
        myAudio.addEventListener('ended', function() {
            this.currentTime = 0;
            this.play();
        }, false);
    }
    myAudio.play();

    //changing game instruction

    setTimeout(()=>{
        $("#header").html("הזהר האויב כבר כאן, התחל לירות!").css('color' , 'red');

    },3000)

    //start space ship attack
    setInterval(run, 4000);
}

//take a fire
var upKey = 38;
var stopMoveFire = 8; //start move again
function fire() {

    var audio = new Audio('./public/sound/SpaceLaserShot.mp3');
    audio.play();
   
    $(".bullet").animate({
        top: 0,
        backgroundColor: "pink",
        opacity:1


    }, {
        duration: 500,
        progress: function (o, p, t) { compareForStop();},

        easing: "linear",

        complete: function () {
            var position = parseInt($('.shooter').css('left'))+20;
            $(".bullet").css({ "background-color": "pink", top: 620 + "px", left: position + "px", opacity:0 })
            stopMoveFire = 8;// start move with the shooter
            upKey = 38; // return the option to shoot;
        }
        

    })

}
var la = document.getElementById("laser");

//stick or control arrows keyboard events 
document.onkeydown = function (event) {
    //right arrow
    if (event.keyCode == 37) {
        var rightArrow = parseInt($('.shooter').css('left'));
        if (rightArrow > 0) {
            $(".shooter").css({ left: "-="+8 })
            $(".bullet").css({ left: "-=" + stopMoveFire })
        }
    }
    //left arrow
    if (event.keyCode == 39) {

        var leftArrow = parseInt($('.shooter').css('left'));
        if (leftArrow < 350) {
            $(".shooter").css({ left: "+="+8 })
            $(".bullet").css({ left: "+=" + stopMoveFire })

        }
    }
    //up arrow
    if (event.keyCode == upKey) {
        upKey = 0; //stop the shooting until the animation complete
        stopMoveFire = 0;// when we take fire stop the bullet from move right or left
        fire();
    }

}
var pointShip = 0;
var pointGeneral = 0;
var pointMother = 0;

//check if we got hit & count hit point
function compareForStop() {
    var ship = parseFloat($("#spaceShip").css("top"));// getting the top position
    var fire = parseFloat($(".bullet").css("top"));
    var ship1 = parseFloat($("#spaceShip").css("left"));
    var fire1 = parseFloat($(".bullet").css("left"));



    if (Math.abs(ship - fire) < 20 && Math.abs(ship1 - fire1) < 20) {// check if they in the same area
        $("#spaceShip").html('<img src="./public/img/boom.jpg" />');
     
        setTimeout(()=>{
         $("#spaceShip").css({ "background-color": "black", top: 0 + "px", opacity:0 }).stop();

        },200)
       
        $(".bullet").css({ "background-color": "black", top: 0 + "px", opacity: 0 });

        

       
        var audio = new Audio('./public/sound/Explosion.mp3');
        audio.play();   

        if (parseFloat($("#spaceShip").css("width")) == 15) {
            pointShip = pointShip + 2;
            document.getElementById("regular").innerHTML = pointShip;
            $("#spaceShip").css("width", 16)
            document.getElementById("total").innerHTML = pointShip + pointGeneral + pointMother;

            hitRegularShip = (hitRegularShip + 1);
            document.getElementById("shipHit").innerHTML = hitRegularShip + "/" + regularShip;
            totalHit = (totalHit + 1);
            document.getElementById("totalShips").innerHTML = totalHit + "/" + totalShip;

            

        }
        if (parseFloat($("#spaceShip").css("width")) == 25) {
            pointGeneral = pointGeneral + 5;
            document.getElementById("general").innerHTML = pointGeneral;
            $("#spaceShip").css("width", 26)
            document.getElementById("total").innerHTML = pointShip + pointGeneral + pointMother;

            hitGeneralShip = (hitGeneralShip + 1);
            document.getElementById("generalHit").innerHTML = hitGeneralShip + "/" + generalShip;
            totalHit = (totalHit + 1);
            document.getElementById("totalShips").innerHTML = totalHit + "/" + totalShip;

     
        }
        if (parseFloat($("#spaceShip").css("width")) == 40) {
            pointMother = pointMother + 10;
            document.getElementById("mother").innerHTML = pointMother;
            $("#spaceShip").css("width", 41)
            document.getElementById("total").innerHTML = pointShip + pointGeneral + pointMother;

            hitMotherShip = (hitMotherShip + 1);
            document.getElementById("motherHit").innerHTML = hitMotherShip + "/" + motherShip;
            totalHit = (totalHit + 1);
            document.getElementById("totalShips").innerHTML = totalHit + "/" + totalShip;


        }

        // 100 point and reload the page
        var totalFinishGame = document.getElementById("total").innerHTML;
        if (totalFinishGame > 100) {
            alert("Well done, you've reached 100 points!")
            window.location.reload();
        }



    }

}

//this game made by erez Asmara