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
        onClickPlace: refillSlots
    }
    
})