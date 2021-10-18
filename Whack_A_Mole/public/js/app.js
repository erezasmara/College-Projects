    var clicked = false; // true or false for hit clicks
    var hits = 0, misses = -1; // counters

    var body = document.getElementById("body");
    var table = document.createElement("table");
    var trArr =[];
    var tdArr =[];
    for (var i = 0; i<10;i++)
        trArr[i] = document.createElement("tr");
    for (var i = 0; i < 100; i++)
        tdArr[i] = document.createElement("td");

    //setting all randoms
    var randomAppearanceTime = Math.random() * 1.5; // values between 0 to 1.5
    if (randomAppearanceTime < 0.5) // making sure that randomAppearanceTime will be [1,1.5]
        randomAppearanceTime += 1;
    else if (randomAppearanceTime < 1)
        randomAppearanceTime = 1;

    var randomNextAppearanceTime = Math.random(); //values [0,1)
    if (randomNextAppearanceTime < 0.25) // making sure values are [0.25,1)
        randomNextAppearanceTime += 0.25;

    var randomCellIndex = Math.floor(Math.random() * 9); // random index between 0 to 8



    function paint3X3Board() {

        for (var i = 0, j = 0; i < 9; i++)
        {
            trArr[j].appendChild(tdArr[i]);
            if ((i + 1) % 3 == 0)
                j++;
        }

        for (var i = 0; i < 3; i++)
            table.appendChild(trArr[i]);

        body.appendChild(table);  



        for (var i = 0; i < 9; i++)
        {
            tdArr[i].style.backgroundImage = "url('public/img/hole.png')";
            tdArr[i].style.backgroundSize = "133px 133px";
        }
        


        setTimeout(running3X3Game, randomNextAppearanceTime * 1000);



    }


    function running3X3Game() {

       
        if (clicked == false) // its miss
        {
            misses++;
            document.getElementById("misses").innerHTML = "Misses: " + misses;
        }
        clicked = false;

        tdArr[randomCellIndex].onclick = "";


        //painting hole -- mole disappear
        tdArr[randomCellIndex].style.backgroundImage = "url('public/img/hole.png')";
        tdArr[randomCellIndex].style.backgroundSize = "133px 133px";



        randomAppearanceTime = Math.random() * 1.5; // values between 0 to 1.5
        if (randomAppearanceTime < 0.5) // making sure that randomAppearanceTime will be [1,1.5]
            randomAppearanceTime += 1;
        else if (randomAppearanceTime < 1)
            randomAppearanceTime = 1;

        randomNextAppearanceTime = Math.random(); //values [0,1)
        if (randomNextAppearanceTime < 0.25) // making sure values are [0.25,1)
            randomNextAppearanceTime += 0.25;

        randomCellIndex = Math.floor(Math.random() * 9); // random index between 0 to 8

        // painting mole
        tdArr[randomCellIndex].style.backgroundImage = "url('public/img/mole.png')";
        tdArr[randomCellIndex].style.backgroundSize = "133px 133px";

        //Hit click
        tdArr[randomCellIndex].onclick = function () {
            if (clicked == false)
                hits++;

            clicked = true;
            document.getElementById("hits").innerHTML = "Hits: " + hits;

            //painting Hitting mole
            tdArr[randomCellIndex].style.backgroundImage = "url('public/img/hitted-mole.png')";
            tdArr[randomCellIndex].style.backgroundSize = "133px 133px";

        };


        setTimeout(function () {

            var stop = setTimeout(running3X3Game, randomNextAppearanceTime * 1000);

            if (misses == 3) {
                clearTimeout(stop);
                alert("GAME OVER !\nYou will start a new game now:)");
                window.location.reload(); //refresh the current page for a new game;
            }

        }, randomAppearanceTime * 1000)


        

    }







    function paint5X5Board() {

        for (var i = 0, j = 0; i < 25; i++) {
            trArr[j].appendChild(tdArr[i]);
            if ((i + 1) % 5 == 0)
                j++;
        }

        for (var i = 0; i < 5; i++)
            table.appendChild(trArr[i]);
           


        body.appendChild(table);

        for (var i = 0; i < 25; i++) {
            tdArr[i].style.backgroundImage = "url('public/img/hole.png')";
            tdArr[i].style.backgroundSize = "80px 80px";
        }

        setTimeout(running5X5Game, randomNextAppearanceTime * 1000);

    }





    


    function running5X5Game() {

       
        if (clicked == false) // its miss
        {
            misses++;
            document.getElementById("misses").innerHTML = "Misses: " + misses;
        }
        clicked = false;

        tdArr[randomCellIndex].onclick = "";


        //painting hole -- mole dissapear
        tdArr[randomCellIndex].style.backgroundImage = "url('public/img/hole.png')";
        tdArr[randomCellIndex].style.backgroundSize = "80px 80px";



        randomAppearanceTime = Math.random() * 1.5; // values between 0 to 1.5
        if (randomAppearanceTime < 0.5) // making sure that randomAppearanceTime will be [1,1.5]
            randomAppearanceTime += 1;
        else if (randomAppearanceTime < 1)
            randomAppearanceTime = 1;

        randomNextAppearanceTime = Math.random(); //values [0,1)
        if (randomNextAppearanceTime < 0.25) // making sure values are [0.25,1)
            randomNextAppearanceTime += 0.25;

        randomCellIndex = Math.floor(Math.random() * 25); // random index between 0 to 24

        // painting mole
        tdArr[randomCellIndex].style.backgroundImage = "url('public/img/mole.png')";
        tdArr[randomCellIndex].style.backgroundSize = "80px 80px";

        //Hit click
        tdArr[randomCellIndex].onclick = function () {
            if (clicked == false)
                hits++;

            clicked = true;
            document.getElementById("hits").innerHTML = "Hits: " + hits;

            //painting hitted mole
            tdArr[randomCellIndex].style.backgroundImage = "url('public/img/hitted-mole.png')";
            tdArr[randomCellIndex].style.backgroundSize = "80px 80px";

        };


        setTimeout(function () {

            var stop = setTimeout(running5X5Game, randomNextAppearanceTime * 1000);

            if (misses == 3) {
                clearTimeout(stop);
                alert("GAME OVER !\nYou will start a new game now:)");
                window.location.reload(); //refresh the current page for a new game;
            }

        }, randomAppearanceTime * 1000)


        

    }







    function paint10X10Board() {

        for (var i = 0, j = 0; i < 100; i++) {
            trArr[j].appendChild(tdArr[i]);
            if ((i + 1) % 10 == 0)
                j++;
        }

        for (var i = 0; i < 10; i++)
            table.appendChild(trArr[i]);

        body.appendChild(table);




        for (var i = 0; i < 100; i++) {
            tdArr[i].style.backgroundImage = "url('public/img/hole.png')";
            tdArr[i].style.backgroundSize = "40px 40px";
        }

        setTimeout(running10X10Game, randomNextAppearanceTime * 1000);


    }


    function running10X10Game() {

       
        if (clicked == false) // its miss
        {
            misses++;
            document.getElementById("misses").innerHTML = "Misses: " + misses;
        }
        clicked = false;

        tdArr[randomCellIndex].onclick = "";


        //painting hole -- mole disappear
        tdArr[randomCellIndex].style.backgroundImage = "url('public/img/hole.png')";
        tdArr[randomCellIndex].style.backgroundSize = "40px 40px";



        randomAppearanceTime = Math.random() * 1.5; // values between 0 to 1.5
        if (randomAppearanceTime < 0.5) // making sure that randomAppearanceTime will be [1,1.5]
            randomAppearanceTime += 1;
        else if (randomAppearanceTime < 1)
            randomAppearanceTime = 1;

        randomNextAppearanceTime = Math.random(); //values [0,1)
        if (randomNextAppearanceTime < 0.25) // making sure values are [0.25,1)
            randomNextAppearanceTime += 0.25;

        randomCellIndex = Math.floor(Math.random() * 100); // random index between 0 to 99

        // painting mole
        tdArr[randomCellIndex].style.backgroundImage = "url('public/img/mole.png')";
        tdArr[randomCellIndex].style.backgroundSize = "40px 40px";

        //Hit click
        tdArr[randomCellIndex].onclick = function () {
            if (clicked == false)
                hits++;

            clicked = true;
            document.getElementById("hits").innerHTML = "Hits: " + hits;

            //painting hitting mole
            tdArr[randomCellIndex].style.backgroundImage = "url('public/img/hitted-mole.png')";
            tdArr[randomCellIndex].style.backgroundSize = "40px 40px";

        };


        setTimeout(function () {

            var stop = setTimeout(running10X10Game, randomNextAppearanceTime * 1000);

            if (misses == 3) {
                clearTimeout(stop);
                alert("GAME OVER !\nYou will start a new game now:)");
                window.location.reload(); //refresh the current page for a new game;
            }

        }, randomAppearanceTime * 1000)


        

    }



    function chooseBoard() {
        var select = document.getElementById("select");
        select.disabled = true;
        var selectValue = select.options[select.selectedIndex].id;

        if (selectValue == "lvl1")
            paint3X3Board();
        if (selectValue == "lvl2")
            paint5X5Board();
        if (selectValue == "lvl3")
            paint10X10Board();
    }
