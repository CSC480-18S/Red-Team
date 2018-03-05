var oswebbleData = {
    selectedTileId: '',
    selectedTileParentId: '',
    selectedSquareId: '',
    // can change to computed property
    rows: generateTableRows(),
    squares: generateSquares()
}

function generateTableRows () {
    var row = 11;
    var tableRows = [];
    for (var i = 0; i < row; i++) {
        tableRows.push(i);
    }
    return tableRows;
}

function generateSquares () {
    var row = 11;
    var column = 11;
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

var selectAndDeselectTile = function (tileId) {
    var tile = document.getElementById(tileId);
    // set tile border color 
    //tile.children.stroke = "red"; (20180304 not working)
    var slotGridTable = tile.parentNode.parentNode.parentNode.parentNode; 
    // selet tile
    if (slotGridTable.className === 'tileSlots') {
        // get Tile ID
        this.selectedTileId = tileId; 
        // get Parent ID
        this.selectedTileParentId = document.getElementById(tileId).parentNode.id; 
    }
    // deselect tile
    else {
        // put tile back to slot
        var selectedTile = document.getElementById(this.selectedTileId);
        // resize the tile (old way)
        //selectedTile.className = 'slot-tile-size';
        document.getElementById(this.selectedTileParentId).appendChild(selectedTile);
        
        
        //this.selectedSquareId = '';
        
    }
}

var putTileInSquare = function (squareId) {
    if(this.selectedTileId !== '' && this.selectedSquareId !== squareId) {
        var selectedTile = document.getElementById(this.selectedTileId);
        // resize the tile (old way)
        //selectedTile.className = 'game-board-tile-size';
        document.getElementById(squareId).appendChild(selectedTile);
        //this.selectedTileId = '';
        this.selectedSquareId = squareId;
    }
}
