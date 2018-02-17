'use strict';

class Tile {
    constructor(x, y, multiplier){
        this._x = x;
        this._y = y;
        this._multiplier = multiplier;
        this._wordPlaced = false;
        this._letter = "";
    }

    set x(x){
        this._x = x;
    }

    set y(y){
        this._y = y;
    }

    set multiplier(multiplier){
        this._multiplier = multiplier;
    }

    set wordPlaced(placed){
        this._wordPlaced = placed;
    }

    set letter(letter){
        this._letter = letter;
    }

    get x(){
        return this._x;
    }

    get y(){
        return this._y;
    }

    get multiplier(){
        return this._multiplier;
    }

    get wordPlaced(){
        return this._wordPlaced;
    }

    get letter(){
        return this._letter;
    }
}


module.exports = Tile;