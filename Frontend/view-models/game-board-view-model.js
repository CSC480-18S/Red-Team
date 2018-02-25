var app = new Vue({
    
    el: '#root',
    
    data: oswebbleData,
    
    methods: {
        
        // Mouse Click
        onDragStart: drag,
        onDragOver: allowDrop,
        onDrop: drop,
        
        // Screen Tap
        onClickTile: selectAndDeselectTile,
        onClickSquare: putTileInSquare
    }    
})