var row = 11;
var column = 11;
var tileSlotNumber = 7;

var currentTileCount = tileSlotNumber + 1;

// data object
var data = {
    selectedTileId: '',
    selectedTileParentId: '',
    selectedSquareId: '',
    // can change to computed property
    rows: generateTableRows(),
    squares: generateSquares(),
    tileSlots: generateTileSlots(),
    tilesOnBoard: [], 
    selectedTileCopyId: "",
    currentRoundtileIdsOnBoard: []
}

function generateTableRows () {
    var tableRows = [];
    for (var i = 0; i < row; i++) {
        tableRows.push(i);
    }
    return tableRows;
}

function generateSquares () {
    var squares = [];
    for (var i = 0; i < row; i++) {
        for (var j = 0; j < column; j++) {
            // use sum to render square background color 
            var sum = i + j;
            switch(sum) {
                case 1: case 3: case 5: case 7: case 9: case 11: case 13: case 15: case 17: case 19:
                    squares.push({id: 'square-' + i + '-' + j, xAxis: i, yAxis: j, isSquareGreen: true, isSquareYellow: false});
                    break;
                default:
                    squares.push({id: 'square-' + i + '-' + j, xAxis: i, yAxis: j, isSquareGreen: false, isSquareYellow: false});
                    break;
            }
        }
    }
    
    return squares;
}

// initialize slots and inside tiles
function generateTileSlots () {
    var tileSlots = [];
    for (var i = 0; i < this.tileSlotNumber; i++) {
        var tile = randomTile();
        tileSlots.push({
            id: 'slot' + i,
            hasTile: true,
            tile: {
                id: ('tile' + i),
                letter: tile.letter,
                value: tile.letterValue,
                highlightedColor: tile.highlightedColor,
                visibility: tile.visibility
            }
        });
    }
    return tileSlots;
}
                       
function randomTile() {
    var char = randomCharacter();
    var value = 0;
    switch (char) {
        case 'A': case 'E': case 'I': case 'O': case 'R': case 'S': case 'T':
            value = 1;
            break; 
        case 'D': case 'L': case 'N': case 'U':
            value = 2;
            break;
        case 'G': case 'H': case 'Y':
            value = 3;
            break;
        case 'B': case 'C': case 'F': case 'M': case 'P': case 'W':
            value = 4;
            break;
        case 'K': case 'V':
            value = 5;
            break;
        case 'X':
            value = 8;
            break;
        case 'J': case 'Q': case 'Z':
            value = 10;
            break;
    }

    function randomCharacter() {
            var chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            return chars.substr( Math.floor(Math.random() * 26), 1);
    }
    
    return {letter: char, letterValue: value, borderColor: "#000000", visibility: "visible"};
}

var drag = function (ev) {   
    ev.dataTransfer.setData("text", ev.target.id);
}

var allowDrop = function (ev) {
    ev.preventDefault();
}

var drop = function (ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    ev.target.innerHTML = ""; ev.target.appendChild(document.getElementById(data));
    
    // test
    var isDoubleScore = false;
    var i;
    for(i = 0; i < this.doubleScoreGameBoardBlocks.length; i++) {
        //console.log(ev.target.id + " " + this.doubleScoreGameBoardBlocks[i] + "\n");
        if (ev.target.id === this.doubleScoreGameBoardBlocks[i]) {
            isDoubleScore = true; 
            break;
        }
    }
    
    //console.log(isDoubleScore);
    
    if(isDoubleScore) {
        this.accumulator += (2 * this.scoreUnit);
    } else {
        this.accumulator += this.scoreUnit;
    }
}

// when clicking on a tile in a tile slot or in the game board 
var selectAndDeselectTile = function (tileId) {
    // get the tile
    var tile = document.getElementById(tileId);
    //tile.children[0].stroke = "red"; (20180304 not working)
    // set tile border color 
    for (var i = 0; i < tileSlotNumber; i++) {
        if (tile.id === this.tileSlots[i].tile.id) {
            this.tileSlots[i].tile.highlightedColor = "#d61515";
        } else {
            this.tileSlots[i].tile.highlightedColor = "#000000";
        }
    }
    // get tile parent
    var wrapper = tile.parentNode.parentNode.parentNode; 
    // record Tile ID
    this.selectedTileId = tileId; 
    // select tile if it is in a slot
    if (wrapper.className === 'wrapper-slots') {
        // get tile 
        var tile = document.getElementById(tileId);
        // get tile's parent ID
        var parentId = tile.parentNode.id;
        // record parent ID
        this.selectedTileParentId = parentId;
        // record both tile and parent ID 
        this.tilesOnBoard.push({tileId: tileId, parentId: parentId});
        // print out selected tile letter in console
        console.log(tile.children[1].innerHTML);
        //console.log(tile.lastChild.innerHTML);
        
        this.selectedTileCopyId = "";
        
    }
    // deselect/destroy tile if it is on the board 
    
    /*
    else {  
        console.log(wrapper.className);
        // find original parent id
        for(var i = 0; i < this.tilesOnBoard.length; i++) {
            if (this.tilesOnBoard[i].tileId === this.selectedTileId) {
                this.selectedTileParentId = this.tilesOnBoard[i].parentId;
                this.tilesOnBoard.splice(i, 1);
                
            }
        }

        // put tile back to slot
        var selectedTileCopy = document.getElementById(this.selectedTileCopyId);
        // destroy tile on board (won't work here)
        console.log("testttt");
        selectedTile.parentNode.removeChild(selectedTileCopy);
        
        //console.log(selectedTile);
        // resize the tile (old way)
        //selectedTile.className = 'slot-tile-size';
        //document.getElementById(this.selectedTileParentId).appendChild(selectedTile);
        

        // update slot information
        for(var i = 0; i < tileSlotNumber; i++) {
            if (this.selectedTileId === this.tileSlots[i].tile.id) {
                
                //this.tileSlots[i] = {};
                this.tileSlots[i].hasTile = true;
            }
        }
        
        this.selectedTileId = '';      
    }
    */
}

// when clicking on a square in the game board
var putTileInSquare = function (squareId) {
    //console.log("called putTileInSquare()");
    var square = document.getElementById(squareId);
    // if a tile in a slot has been clicked 
    //if(this.selectedTileId !== "") {
        // get the square
        var selectedSquare = document.getElementById(squareId);
        // copy
        if(this.selectedTileCopyId === "" && selectedSquare.children.length === 0) {
            // get the tile 
            var selectedTile = document.getElementById(this.selectedTileId);
            // copy the tile
            var cln = selectedTile.cloneNode(true);
            //console.log("cln id: " + cln.id);
            // put the clone tile on the game board
            selectedSquare.appendChild(cln);
            //
            this.selectedTileCopyId = cln.id;
            // update slot information
            for(var i = 0; i < tileSlotNumber; i++) {
                if (this.selectedTileId === this.tileSlots[i].tile.id) {
                    //this.tileSlots[i] = {};
                    this.tileSlots[i].hasTile = false;
                    this.tileSlots[i].tile.visibility = "hidden";
                }
            }
            // add tile id in the current round 
            this.currentRoundtileIdsOnBoard.push(this.selectedTileCopyId);
            
        } else { // move around or distroy
            var selectedTileCopy = document.getElementById(this.selectedTileCopyId);
            //console.log("selectedSquare.hasChildNodes(): " + selectedSquare.hasChildNodes()); 
            
            if(!selectedSquare.hasChildNodes()) {
                //console.log(selectedTileCopy.children[0].getAttribute("fill"));
                if (selectedTileCopy.children[0].getAttribute("fill") !== "#D3D3D3") {
                    selectedSquare.appendChild(selectedTileCopy);
                }
            } else {
                var childTile = selectedSquare.children[0];
                // only current round tiles can be put back 
                //console.log(childTile.children[0].attributes[7].nodeValue);
                if (childTile.children[0].getAttribute("fill") !== "#D3D3D3") {
                    //selectedSquare.removeChild(selectedTileCopy);
                    selectedSquare.removeChild(childTile);
                    // update slot information
                    for(var i = 0; i < tileSlotNumber; i++) {
                        if (childTile.id === this.tileSlots[i].tile.id) {
                        //if (this.selectedTileId === this.tileSlots[i].tile.id) {
                            //this.tileSlots[i] = {};
                            this.tileSlots[i].hasTile = true;
                            this.tileSlots[i].tile.visibility = "visible";
                            this.tileSlots[i].tile.highlightedColor = "#000000";
                        }
                    }
                    // remove tile id in the current round
                    this.currentRoundtileIdsOnBoard.pop(this.selectedTileCopyId);
                } 
            }   
        }
        
//        if (square.hasChildNodes()) {
//            
//        } else 
        
//        for (var i = 0; i < this.tilesOnBoard.length; i++) {
//            if (this.selectedTileId === this.tilesOnBoard.tileId) {
//                
//            }
//        }
        
        //this.selectedTileId = '';
        // record the square position
        this.selectedSquareId = selectedSquare.id;
        
        //this.selectedTileId = "";
    //}
}

var refillSlots = function () {
    
    for(var i = 0; i < tileSlotNumber; i++) {
        //console.log(this.tileSlots[i]);
        // tileSlots[i] != null && 
        if (!this.tileSlots[i].hasTile) {
            //generate a tile in it (update id)
            var tile = randomTile();
            this.tileSlots[i].tile.id = 'tile' + currentTileCount;
            this.tileSlots[i].tile.letter = tile.letter;
            this.tileSlots[i].tile.value = tile.letterValue;
            this.tileSlots[i].tile.visibility = "visible";
            this.tileSlots[i].tile.highlightedColor = "black";
            currentTileCount++; 
        }
    }
    
    // change color of tiles on board
    for (var i = 0; i < this.currentRoundtileIdsOnBoard.length; i++) {
        var tile = document.getElementById(this.currentRoundtileIdsOnBoard[i]);
        tile.children[0].setAttribute("fill", "#D3D3D3");
        tile.children[1].setAttribute("fill", "#000000");
        tile.children[2].setAttribute("fill", "#000000");
    }
    
    this.currentRoundtileIdsOnBoard = [];
    this.selectedTileId = "";
    
}

/*
var svg = function (id, letter, value) {
    
    var svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.setAttribute("id", id); 
    // test (looks the same but not working)
    //svg.setAttribute("v-on:click", "onClickTile('"+ id +"')");
    // considering changeing this from window to this 
    //svg.addEventListener("v-on:click", selectAndDeselectTile(id));
    
    var rect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
    rect.setAttributeNS(null, "x", "0");
    rect.setAttributeNS(null, "y", "0");
    rect.setAttributeNS(null, "stroke", "black");
    rect.setAttributeNS(null, "stroke-width", "1px");
    rect.setAttributeNS(null, "width", "100%");
    rect.setAttributeNS(null, "height", "100%");
    rect.setAttributeNS(null, "fill", "#f4dc00");
    
    var text = document.createElementNS("http://www.w3.org/2000/svg", "text");
    text.setAttribute("x", "50%");
    text.setAttribute("y", "60%");
    text.setAttribute("alignment-baseline", "middle");
    text.setAttribute("text-anchor", "middle");
    text.innerHTML = letter;
    
    var letterValue = document.createElementNS("http://www.w3.org/2000/svg", "text");
    letterValue.setAttribute("x", "70%");
    letterValue.setAttribute("y", "30%");
    letterValue.setAttribute("class", "letter-value");
    letterValue.innerHTML = value;
    
    svg.appendChild(rect);                  
    svg.appendChild(text); 
    svg.appendChild(letterValue);
    
    return svg;
}
*/