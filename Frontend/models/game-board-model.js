var oswebbleData = {
    selectedTileId: '',
    selectedTileParentId: '',
    selectedSquareId: ''
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
}

var selectAndDeselectTile = function (tileId) {
    var slotGridTable = document.getElementById(tileId).parentNode.parentNode.parentNode.parentNode; 
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
        document.getElementById(this.selectedTileParentId).appendChild(document.getElementById(this.selectedTileId));
    }
}

var putTileInSquare = function (squareId) {
    if(this.selectedTileId !== '' && this.selectedSquareId !== squareId) {
        document.getElementById(squareId).appendChild(document.getElementById(this.selectedTileId));
        //this.selectedTileId = '';
        this.selectedSquareId = squareId;
    }
}

