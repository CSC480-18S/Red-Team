var app = new Vue({
    
    el: '#root',
    
    data: oswebbleData,
    
    methods: {
        
        onDragStart: drag,
        onDragOver: allowDrop,
        onDrop: drop
        
    }    
})