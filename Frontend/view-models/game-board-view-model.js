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
        onClickSquare: putTileInSquare,
        
        // place tile
        onClickSwap: swap,
        onClickPlace: refillSlots,
        onClickShuffle: shuffle
    },
    
    mounted() {
        // Make a request for a user with a given ID
        axios.get('initial_tile.json')
            .then(function (response) {
                //console.log(response);
                for(var i = 0; i < data.tileSlots.length; i++) {
                    for (key in response.data.tiles[i]) {
                        data.tileSlots[i].tile.letter = response.data.tiles[i][key];
                    }
                }    
            })
            .catch(function (error) {
                console.log(error);
            });
    }  
})

