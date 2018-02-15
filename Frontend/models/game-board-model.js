var oswebbleData = {
    
    centerBlockMark: 'TW',
    
    tileValue: '1'
       
}


var drag = function (ev) {
    
    ev.dataTransfer.setData("text", ev.target.id);
    console.log("in drag function");
    
}

var allowDrop = function (ev) {
    
    ev.preventDefault();
    
}

var drop = function (ev) {
    
    ev.preventDefault();
    
    var data = ev.dataTransfer.getData("text");
    ev.target.innerHTML = "";
    ev.target.appendChild(document.getElementById(data));
    
}