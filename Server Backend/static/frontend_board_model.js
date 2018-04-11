var row = 11
var column = 11
var tileSlotNumber = 7

var currentTileCount = tileSlotNumber + 1

// sockets
let socket = io.connect('')
socket.on('connect', () => {
  console.log(socket.id)
})

socket.on('whoAreYou', () => {
  socket.emit('whoAreYou', {
    isClient: true
  })
})

socket.on('errorMessage', response => {
  alert(response.error)
})

socket.on('boardUpdate', response => {
  this.data.currentPlayTileAmount = 0
  for (i = 0; i < row; i++) {
    for (j = 0; j < column; j++) {
      var square = document.getElementById('square-' + i + '-' + j)
      if (!square.hasChildNodes()) {
        if (response.board[i][j] !== null) {
          var tile = {
            id: 'playedLetter: ' + response.board[i][j],
            letter: response.board[i][j],
            value: tileValue(response.board[i][j]),
            highlightedColor: undefined,
            visibility: 'visible'
          }
          console.log(tile)
          var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg')
          svg.setAttribute('id', tile.id)
          svg.setAttribute('visibility', 'visible')
          var rect = document.createElementNS('http://www.w3.org/2000/svg', 'rect')
          rect.setAttribute('x', 0)
          rect.setAttribute('y', 0)
          rect.setAttribute('stroke', 'black')
          rect.setAttribute('stroke-width', '1px')
          rect.setAttribute('width', '100%')
          rect.setAttribute('height', '100%')
          rect.setAttribute('fill', '#D3D3D3')
          svg.appendChild(rect)
          var text = document.createElementNS('http://www.w3.org/2000/svg', 'text')
          text.setAttribute('x', '50%')
          text.setAttribute('y', '60%')
          text.setAttribute('alignment-baseline', 'middle')
          text.setAttribute('text-anchor', 'middle')
          text.setAttribute('fill', undefined)
          text.textContent = tile.letter
          svg.appendChild(text)
          var text2 = document.createElementNS('http://www.w3.org/2000/svg', 'text')
          text2.setAttribute('x', '70%')
          text2.setAttribute('y', '30%')
          text2.setAttribute('fill', undefined)
          text2.setAttribute('class', 'letter-value')
          text2.textContent = tile.value
          svg.appendChild(text2)
          square.appendChild(svg)

          this.data.tilesOnBoardValueAndPosition.push({tileLetter: tile.letter,
            xAxis: i,
            yAxis: j
          })
        }
      }
    }
  }
})

socket.on('play', response => {
  console.log(response)

  if (response.invalid) {
    for (let i = 0; i < this.data.currentPlayTileAmount; i++) {
      var t = this.data.tilesOnBoardValueAndPosition[this.data.tilesOnBoardValueAndPosition.length - 1]
      console.log(t)
      var square = document.getElementById('square-' + t.xAxis + '-' + t.yAxis)
      this.data.tilesOnBoardValueAndPosition.pop()
      square.removeChild(square.firstChild)
    }

    this.data.currentPlayTileAmount = 0
  }
})

socket.on('gameEvent', response => {
  console.log('received gameEvent: ')
  console.log(response)

  // test data
  var gameEvent = response.action
  // gameEvent = response.action;
  document.getElementById('actualEvent').innerHTML = '<br>'
  document.getElementById('actualEvent').innerHTML = gameEvent
})

socket.on('dataUpdate', response => {
  console.log('received dataUpdate event: ')
  console.log(response)
  this.data.username = response.name
  // response.position is the position of four players on the server
  // tested data
  // var tiles = ['T', 'E', 'S', 'T'];
  // var isTurn = true;
  // if (isTurn === false) {
  if (response.isTurn === false) {
    document.getElementById('btnSwap').disabled = true
    document.getElementById('btnPlace').disabled = true
    document.getElementById('btnShuffle').disabled = true
    for (var i = 0; i < tileSlotNumber; i++) {
      this.data.tileSlots[i].tile.disabled = true
    }
  } else {
    document.getElementById('btnSwap').disabled = false
    document.getElementById('btnPlace').disabled = false
    document.getElementById('btnShuffle').disabled = false
    for (var i = 0; i < tileSlotNumber; i++) {
      this.data.tileSlots[i].tile.disabled = false
    }
  }
})

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
  selectedTileCopyId: '',
  currentRoundtileIdsOnBoard: [],
  tilesOnBoardValueAndPosition: [],
  currentPlayTileAmount: 0,
  username: ''
  // backgroundColor: ["rgb(171,171,171)", "orange", "green"]
}

function generateTableRows() {
  var tableRows = []
  for (var i = 0; i < row; i++) {
    tableRows.push(i)
  }
  return tableRows
}

function generateSquares() {
  var squares = []
  for (var i = 0; i < row; i++) {
    for (var j = 0; j < column; j++) {
      // use sum to render square background color
      // var sum = i + j
      // switch (sum) {
      // case 1: case 3: case 5: case 7: case 9: case 11: case 13: case 15: case 17: case 19:
      squares.push({id: 'square-' + i + '-' + j, xAxis: i, yAxis: j, isSquareGreen: true, isSquareYellow: false, squareBackgroundColor: 'rgb(171,171,171)'})
      // break
      // default:
      // squares.push({id: 'square-' + i + '-' + j, xAxis: i, yAxis: j, isSquareGreen: false, isSquareYellow: false, squareBackgroundColor: "orange"})
      // break
      // }
    }
  }

  for (var k = 0; k < squares.length; k++) {
    switch (squares[k].id) {
      case 'square-0-0': case 'square-0-7': case 'square-2-4': case 'square-3-7': case 'square-3-10': case 'square-4-2': case 'square-6-8': case 'square-7-0': case 'square-7-3': case 'square-8-6': case 'square-10-3': case 'square-10-10':
        squares[k].squareBackgroundColor = 'rgb(242,195,50)'
        break
      case 'square-0-3': case 'square-0-10': case 'square-2-6': case 'square-3-0': case 'square-3-3': case 'square-4-8': case 'square-6-2': case 'square-7-7': case 'square-7-10': case 'square-8-4': case 'square-10-0': case 'square-10-7':
        squares[k].squareBackgroundColor = 'rgb(24,180,76)'
        break
      case 'square-5-5':
        squares[k].squareBackgroundColor = 'rgb(84,76,76)'
        break
    }
  }
  return squares
}

// initialize slots and inside tiles
function generateTileSlots() {
  var tileSlots = []
  for (var i = 0; i < this.tileSlotNumber; i++) {
    var tile = randomTile()
    tileSlots.push({
      id: 'slot' + i,
      hasTile: true,
      tile: {
        id: ('tile' + i),
        letter: tile.letter,
        value: tile.letterValue,
        highlightedColor: tile.highlightedColor,
        visibility: tile.visibility,
        disabled: false
      }
    })
  }
  return tileSlots
}

function tileValue(tile) {
  switch (tile) {
    case 'A': case 'E': case 'I': case 'O':
    case 'U': case 'L': case 'N': case 'S': case 'T': case 'R':
      return 1
    case 'D':
    case 'G':
      return 2
    case 'B': case 'C':
    case 'M': case 'P':
      return 3
    case 'F': case 'H':
    case 'V': case 'W': case 'Y':
      return 4
    case 'K':
      return 5
    case 'J':
    case 'X':
      return 8
    case 'Q':
    case 'Z':
      return 10
  }
}

function randomTile() {
  var chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  var char = chars.substr(Math.floor(Math.random() * 26), 1)

  var value = tileValue(char)

  return {letter: char, letterValue: value, borderColor: '#000000', visibility: 'visible'}
}

var drag = function(ev) {
  ev.dataTransfer.setData('text', ev.target.id)
}

var allowDrop = function(ev) {
  ev.preventDefault()
}

var drop = function(ev) {
  ev.preventDefault()
  var data = ev.dataTransfer.getData('text')
  ev.target.innerHTML = ''; ev.target.appendChild(document.getElementById(data))

  // test
  var isDoubleScore = false
  var i
  for (i = 0; i < this.doubleScoreGameBoardBlocks.length; i++) {
    if (ev.target.id === this.doubleScoreGameBoardBlocks[i]) {
      isDoubleScore = true
      break
    }
  }

  if (isDoubleScore) {
    this.accumulator += (2 * this.scoreUnit)
  } else {
    this.accumulator += this.scoreUnit
  }
}

// when clicking on a tile in a tile slot or in the game board
var selectAndDeselectTile = function(tileId) {
  // get the tile
  var tile = document.getElementById(tileId)
  // tile.children[0].stroke = "red"; (20180304 not working)
  // set tile border color
  for (var i = 0; i < tileSlotNumber; i++) {
    isDisable = this.tileSlots[i].tile.disabled
    if (tile.id === this.tileSlots[i].tile.id && !isDisable) {
      this.tileSlots[i].tile.highlightedColor = '#d61515'
    } else {
      this.tileSlots[i].tile.highlightedColor = '#000000'
    }
  }

  if (!isDisable) {
    // get tile parent
    var wrapper = tile.parentNode.parentNode.parentNode
    // record Tile ID
    this.selectedTileId = tileId
    // select tile if it is in a slot
    if (wrapper.className === 'wrapper-slots') {
      var isDisable = false
      // get tile
      var tile = document.getElementById(tileId)
      // get tile's parent ID
      var parentId = tile.parentNode.id
      // record parent ID
      this.selectedTileParentId = parentId
      // record both tile and parent ID
      this.tilesOnBoard.push({tileId: tileId, parentId: parentId})
      // print out selected tile letter in console
      this.selectedTileCopyId = ''
    }
  }
}

// when clicking a square on the game board
var putTileInSquare = function(squareId) {
  var square = document.getElementById(squareId)
  // if a tile in a slot has been clicked
  if (this.selectedTileId !== '') {
    // get the square
    var selectedSquare = document.getElementById(squareId)
    // copy
    if (this.selectedTileCopyId === '' && selectedSquare.children.length === 0) {
      // get the tile
      var selectedTile = document.getElementById(this.selectedTileId)
      // copy the tile
      var cln = selectedTile.cloneNode(true)
      // put the clone tile on the game board
      selectedSquare.appendChild(cln)
      //
      this.selectedTileCopyId = cln.id
      // update slot information
      for (var i = 0; i < tileSlotNumber; i++) {
        if (this.selectedTileId === this.tileSlots[i].tile.id) {
          // this.tileSlots[i] = {};
          this.tileSlots[i].hasTile = false
          this.tileSlots[i].tile.visibility = 'hidden'
        }
      }

      for (var i = 0; i < this.squares.length; i++) {
        if (squareId === this.squares[i].id) {
          this.currentPlayTileAmount++
          this.tilesOnBoardValueAndPosition.push({tileLetter: document.getElementById(this.selectedTileId).children[1].innerHTML,
            xAxis: this.squares[i].xAxis,
            yAxis: this.squares[i].yAxis
          })
        }
      }
      // add tile id in the current round
      this.currentRoundtileIdsOnBoard.push(this.selectedTileCopyId)
    } else { // move around or distroy
      var selectedTileCopy = document.getElementById(this.selectedTileCopyId)
      this.tilesOnBoardValueAndPosition.pop()
      this.currentTileCount--
      if (!selectedSquare.hasChildNodes()) {
        if (selectedTileCopy.children[0].getAttribute('fill') !== '#D3D3D3') {
          selectedSquare.appendChild(selectedTileCopy)

          for (var i = 0; i < this.squares.length; i++) {
            if (squareId === this.squares[i].id) {
              this.currentPlayTileAmount++
              this.tilesOnBoardValueAndPosition.push({tileLetter: document.getElementById(this.selectedTileId).children[1].innerHTML,
                xAxis: this.squares[i].xAxis,
                yAxis: this.squares[i].yAxis
              })
            }
          }
        }
      } else {
        var childTile = selectedSquare.children[0]
        // only current round tiles can be put back
        if (childTile.children[0].getAttribute('fill') !== '#D3D3D3') {
          // selectedSquare.removeChild(selectedTileCopy);
          selectedSquare.removeChild(childTile)
          // update slot information
          for (var i = 0; i < tileSlotNumber; i++) {
            if (childTile.id === this.tileSlots[i].tile.id) {
              // if (this.selectedTileId === this.tileSlots[i].tile.id) {
              // this.tileSlots[i] = {};
              this.tileSlots[i].hasTile = true
              this.tileSlots[i].tile.visibility = 'visible'
              this.tileSlots[i].tile.highlightedColor = '#000000'
            }
          }

          this.tilesOnBoardValueAndPosition.pop()
          this.currentPlayTileAmount--
          // remove tile id in the current round
          this.currentRoundtileIdsOnBoard.pop(this.selectedTileCopyId)
        }
      }
    }
    // record the square position
    this.selectedSquareId = selectedSquare.id
  }
}

var swap = function() {
  for (var i = 0; i < tileSlotNumber; i++) {
    var tile = randomTile()
    var shouldChange = Math.floor(2 * Math.random())
    if (shouldChange) {
      this.tileSlots[i].tile.letter = tile.letter
      this.tileSlots[i].tile.value = tile.letterValue
    }
  }
}

var refillSlots = function() {
  for (var i = 0; i < tileSlotNumber; i++) {
    if (!this.tileSlots[i].hasTile) {
      // generate a tile in it (update id)
      var tile = randomTile()
      this.tileSlots[i].tile.id = 'tile' + currentTileCount
      this.tileSlots[i].tile.letter = tile.letter
      this.tileSlots[i].tile.value = tile.letterValue
      this.tileSlots[i].tile.visibility = 'visible'
      this.tileSlots[i].tile.highlightedColor = 'black'
      currentTileCount++
    }
  }

  // change color of tiles on board
  for (var i = 0; i < this.currentRoundtileIdsOnBoard.length; i++) {
    var tile = document.getElementById(this.currentRoundtileIdsOnBoard[i])
    tile.children[0].setAttribute('fill', 'rgb(251,251,227)') // #D3D3D3
    tile.children[1].setAttribute('fill', '#000000')
    tile.children[2].setAttribute('fill', '#000000')
  }

  this.currentRoundtileIdsOnBoard = []
  this.selectedTileId = ''
}

var shuffle = function() {
  this.tileSlots.sort(function() { return 0.5 - Math.random() })
}

function emitBoard() {
  var array = new Array(row)
  for (var i = 0; i < row; i++) {
    array[i] = new Array(column)
  }

  for (var i = 0; i < row; i++) {
    for (var j = 0; j < column; j++) {
      if (array[i][j] == undefined) {
        array[i][j] = null
      }
    }
  }

  var tiles = (this.data.tilesOnBoardValueAndPosition)
  for (var i = 0; i < tiles.length; i++) {
    var x = tiles[i].xAxis
    var y = tiles[i].yAxis
    array[x][y] = tiles[i].tileLetter
  }

  console.log(array)

  socket.emit('playWord', array)
}
