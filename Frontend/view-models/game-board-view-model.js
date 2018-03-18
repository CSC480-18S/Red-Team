// create a viewmodel instance by passing an options object in 
var vm = new Vue({
    // associate with view
    el: '#root',
    
    // associate with data in model: the data object is added to a Vue instance
    data: data,
    
    // associate with functions in model
    methods: {
        
        // Mouse Click
        onDragStart: drag,
        onDragOver: allowDrop,
        onDrop: drop,
        
        // Screen Tap
        onClickTile: selectAndDeselectTile,
        onClickSquare: putTileInSquare
    },
    
    mounted() {
        
        //fillTileSlots(); 
        
        /*
        var tile0 = svg('tile0', 'O', 1);
        console.log(tile0);
        document.querySelector('#slot0').appendChild(tile0);
        
        var tile1 = svg('tile1', 'L', 4);
        console.log(tile1);
        document.querySelector('#slot1').appendChild(tile1);
        */
    }
    
})



function fillTileSlots() {
	for (var i = 0; i < tileSlotNumber; i++) {
        var slot = document.querySelector("#slot" + i);
		// check if the slot is empty
		if (!slot.hasChildNodes()) {        
			var tileInfo = randomTile();
			// place the tile in 
			slot.appendChild(svg('tile'+ i, tileInfo.letter, tileInfo.letterValue));
		}
	}
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
    
    return {letter: char, letterValue: value};
}