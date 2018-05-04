var row = 11
var column = 11
var tileSlotNumber = 7
var currentTileCount = tileSlotNumber + 1
var firstTimeGeneratedTiles = []

// sockets
let ws = new WebSocket('ws://localhost:3000')
ws.onopen = function(event) {
  // send "whoAmI" event
  let whoAmI = { event: 'whoAmI', data: { client: 'CL' } }
  ws.send(JSON.stringify(whoAmI))

  ws.onmessage = function(event) {
    // NEW WEBSOCKETS STUFF -- UNABLE TO TEST, MAY BE POORLY IMPLEMENTED
    let mes = JSON.parse(event.data)
    console.log(mes.event)
    switch (mes.event) {
      case 'errorMessage':
        alert(mes.data.error)
        break
      case 'gameOver':
        alert(`${JSON.stringify(mes.data, null, 4)}`)
		    gameOver(mes.data)
        break
      case 'invalidPlay':
        invalidPlay()
        break
      case 'playTimer':
        console.log(mes)
        playTime(mes.data.time)
        break
      case 'gameEvent':
        console.log('received gameEvent: ')
        console.log(mes.data)
        var gameEvent = mes.data.action
        document.getElementById('actualEvent').innerHTML = '<br>'
        document.getElementById('actualEvent').innerHTML = gameEvent
        break
      case 'dataUpdate':
        console.log(mes)
        dataUpdate(mes.data)
        break
      default:
        console.log(mes.data)
    }
  }
}

// response to dataUpdate socket event
function dataUpdate(response) {
  this.data.isTurn = response.isTurn
  if (!response.isTurn) {
    this.data.colored = false
  }
  this.data.score = response.score
  this.data.goldScore = response.latestData.gold
  this.data.greenScore = response.latestData.green

  boardUpdate(response.latestData.board)

  console.log('received dataUpdate event: ')
  console.log(response)
  this.data.username = response.name
  this.data.tileSlots = generateTiles(response.tiles)
  // response.position is the position of four players on the server
  // tested data
  // var tiles = ['T', 'E', 'S', 'T'];
  // var isTurn = true;
  // if (isTurn === false) {
  if (response.isTurn === false) {
    this.data.playTime = 'Wait for your turn...'
    document.getElementById('btnSwap').disabled = true
    document.getElementById('btnPlace').disabled = true
    document.getElementById('btnShuffle').disabled = true
    for (var i = 0; i < tileSlotNumber; i++) {
      this.data.tileSlots[i].tile.disabled = true
    }
  } else {
    this.data.playTime = "It's your turn!"
    document.getElementById('btnSwap').disabled = false
    document.getElementById('btnPlace').disabled = false
    document.getElementById('btnShuffle').disabled = false
    for (var i = 0; i < tileSlotNumber; i++) {
      this.data.tileSlots[i].tile.disabled = false
    }
  }
}

// responds to boardUpdate socket event
function boardUpdate(board) {
  this.data.currentPlayTileAmount = 0
  for (i = 0; i < row; i++) {
    for (j = 0; j < column; j++) {
      var square = document.getElementById('square-' + i + '-' + j)
      if (!square.hasChildNodes()) {
        if (board[i][j] !== null) {
          var tile = {
            id: 'playedLetter: ' + board[i][j],
            letter: board[i][j],
            value: tileValue(board[i][j]),
            highlightedColor: undefined,
            visibility: 'visible'
          }
          
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
}

function gameOver(response) {
  for (let i = 0; i < this.data.tilesOnBoardValueAndPosition.length - 1; i++) {
    var t = this.data.tilesOnBoardValueAndPosition[this.data.tilesOnBoardValueAndPosition.length - 1]

    // if (t != undefined) {
    var square = document.getElementById('square-' + t.xAxis + '-' + t.yAxis)
    this.data.tilesOnBoardValueAndPosition.pop()
    square.removeChild(square.firstChild)
    // }
  }
  this.data.selectedTileId = ''
  for (var i = 0; i < tileSlotNumber; i++) {
    this.data.tileSlots[i].tile.highlightedColor = '#000000'
  }

  // this.data.currentPlayTileAmount = 0
}

// response to invalidPlay socket event
function invalidPlay() {
  for (let i = 0; i < this.data.currentPlayTileAmount; i++) {
    var t = this.data.tilesOnBoardValueAndPosition[this.data.tilesOnBoardValueAndPosition.length - 1]
    // console.log(t)

    if (t != undefined) {
      var square = document.getElementById('square-' + t.xAxis + '-' + t.yAxis)
      this.data.tilesOnBoardValueAndPosition.pop()
      square.removeChild(square.firstChild)
    }
  }
  this.data.selectedTileId = ''
  for (var i = 0; i < tileSlotNumber; i++) {
    this.data.tileSlots[i].tile.highlightedColor = '#000000'
  }

  this.data.currentPlayTileAmount = 0

  for (let i = 0; i < this.data.tileSlots.length; i++) {
    this.data.tileSlots[i].hasTile = true
    this.data.tileSlots[i].tile.visibility = 'visible'
  }
}

//response to playTime socket event
function playTime(response) {
// response to playTime socket event
function playTime(time) {
  this.data.playTime = time
  if (this.data.playTime % 2 === 0) {
    this.data.colored = true
  }	else {
    this.data.colored = false
  }
}

// OLD SOCKETS.IO STUFF -- LEFT COMMENTED OUT IN CASE WEBSOCKETS STUFF IS IMPLEMENTED INCORRECTLY

/* ws.on('errorMessage', response => {
  alert(response.error)
})

ws.on('gameOver', response => {
  alert(`${JSON.stringify(response, null, 4)}`)
})

ws.on('boardUpdate', response => {
  this.data.goldScore = response.yellow
  this.data.greenScore = response.green

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

ws.on('play', response => {
  console.log(response)

  if (response.invalid) {
    for (let i = 0; i < this.data.currentPlayTileAmount; i++) {
      var t = this.data.tilesOnBoardValueAndPosition[this.data.tilesOnBoardValueAndPosition.length - 1]
      // console.log(t)

      if (t != undefined) {
        var square = document.getElementById('square-' + t.xAxis + '-' + t.yAxis)
        this.data.tilesOnBoardValueAndPosition.pop()
        square.removeChild(square.firstChild)
      }
    }
    this.data.selectedTileId = ''
    for (var i = 0; i < tileSlotNumber; i++) {
      this.data.tileSlots[i].tile.highlightedColor = '#000000'
    }

    this.data.currentPlayTileAmount = 0
  }

  for (let i = 0; i < this.data.tileSlots.length; i++) {
    this.data.tileSlots[i].hasTile = true
    this.data.tileSlots[i].tile.visibility = 'visible'
  }
})

ws.on('gameEvent', response => {
  console.log('received gameEvent: ')
  console.log(response)

  // test data
  var gameEvent = response.action
  // gameEvent = response.action;
  document.getElementById('actualEvent').innerHTML = '<br>'
  document.getElementById('actualEvent').innerHTML = gameEvent
})

ws.on('dataUpdate', response => {
  this.data.isTurn = response.isTurn
  this.data.score = response.score
  this.data.playTime = 60

  let time
  if (this.data.isTurn) {
    clearInterval(time)
    time = setInterval(() => {
      this.data.playTime--
      if (this.data.playTime % 2 === 0) {
        this.data.colored = true
      } else {
        this.data.colored = false
      }
    }, 1000)
  } else {
    clearInterval(time)
    this.data.colored = false
  }

  console.log('received dataUpdate event: ')
  console.log(response)
  this.data.username = response.name
  this.data.tileSlots = generateTiles(response.tiles)
  // response.position is the position of four players on the server
  // tested data
  // var tiles = ['T', 'E', 'S', 'T'];
  // var isTurn = true;
  // if (isTurn === false) {
  if (response.isTurn === false) {
    this.data.isMyTurn = 'Wait for your turn...'
    document.getElementById('btnSwap').disabled = true
    document.getElementById('btnPlace').disabled = true
    document.getElementById('btnShuffle').disabled = true
    for (var i = 0; i < tileSlotNumber; i++) {
      this.data.tileSlots[i].tile.disabled = true
    }
  } else {
    this.data.isMyTurn = "It's your turn!"
    document.getElementById('btnSwap').disabled = false
    document.getElementById('btnPlace').disabled = false
    document.getElementById('btnShuffle').disabled = false
    for (var i = 0; i < tileSlotNumber; i++) {
      this.data.tileSlots[i].tile.disabled = false
    }
  }
}) */

// data object
var data = {
  selectedTileId: '',
  selectedTileParentId: '',
  selectedSquareId: '',
  // can change to computed property
  rows: generateTableRows(),
  squares: generateSquares(),
  tileSlots: [],
  tilesOnBoard: [],
  selectedTileCopyId: '',
  currentRoundtileIdsOnBoard: [],
  tilesOnBoardValueAndPosition: [],
  currentPlayTileAmount: 0,
  username: '',
  greenScore: 0,
  goldScore: 0,
<<<<<<< HEAD
  playTime: 60,
=======
  // backgcoresroundColor: ["rgb(171,171,171)", "orange", "green"]
  playTime: '',
>>>>>>> updating
  isTurn: false,
  score: 0,
  colored: false
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
      squares.push({id: 'square-' + i + '-' + j, xAxis: i, yAxis: j, isSquareGreen: true, isSquareYellow: false, squareBackgroundColor: 'rgb(171,171,171)'})
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
function generateTiles(tilesToGenerate) {
  var tileSlots = []
  if (firstTimeGeneratedTiles.length === 0) {
    for (var i = 0; i < tilesToGenerate.length; i++) {
      var letter = tilesToGenerate[i]
      var tileValue = this.tileValue(letter)
      tileSlots.push({
        id: 'slot' + i,
        hasTile: true,
        tile: {
          id: ('tile' + i),
          letter: letter,
          value: tileValue,
          highlightedColor: undefined,
          visibility: 'visible',
          disabled: false
        }
      })
      firstTimeGeneratedTiles.push(letter)
    }
  } else {
    for (var i = 0; i < tilesToGenerate.length; i++) {
      var letter = tilesToGenerate[i]
      var tileValue = this.tileValue(letter)

      tileSlots.push({
        id: 'slot' + i,
        hasTile: true,
        tile: {
          id: ('tile' + currentTileCount),
          letter: letter,
          value: tileValue,
          highlightedColor: undefined,
          visibility: 'visible',
          disabled: false
        }
      })
      currentTileCount++
    }
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
      this.selectedTileCopyId = ''
    } else if (wrapper.className === 'wrapper-board') {
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

      this.currentRoundtileIdsOnBoard.push(cln.id)
    } else { // move around or distroy
      if (!selectedSquare.hasChildNodes()) {
      } else {
        var childTile = selectedSquare.children[0]
        // only current round tiles can be put back
        if (childTile.children[0].getAttribute('fill') !== '#D3D3D3') {
          selectedSquare.removeChild(childTile)
          this.selectedTileCopyId = ''
          // update slot information
          for (var i = 0; i < tileSlotNumber; i++) {
            if (childTile.id === this.tileSlots[i].tile.id) {
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
  }
}

var swap = function() {
  for (let i = 0; i < this.currentPlayTileAmount; i++) {
    var t = this.tilesOnBoardValueAndPosition[this.tilesOnBoardValueAndPosition.length - 1]
    var square = document.getElementById('square-' + t.xAxis + '-' + t.yAxis)
    this.tilesOnBoardValueAndPosition.pop()
    square.removeChild(square.firstChild)
  }
  this.selectedTileId = ''
  for (var i = 0; i < tileSlotNumber; i++) {
    this.tileSlots[i].tile.highlightedColor = '#000000'
  }

  this.currentPlayTileAmount = 0

  for (let i = 0; i < this.tileSlots.length; i++) {
    this.tileSlots[i].hasTile = true
    this.tileSlots[i].tile.visibility = 'visible'
  }

  ws.send(JSON.stringify({event: 'swap'}))
}

var grey = function() {
  // change color of tiles on board
  for (var i = 0; i < this.currentRoundtileIdsOnBoard.length; i++) {
    var tile = document.getElementById(this.currentRoundtileIdsOnBoard[i])
    tile.children[0].setAttribute('fill', 'rgb(212,212,212)')
    tile.children[1].setAttribute('fill', '#000000')
    tile.children[2].setAttribute('fill', '#000000')
  }
  this.currentRoundtileIdsOnBoard = []
  this.selectedTileId = ''
  this.selectedTileCopyId = ''
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

  // socket.emit('playWord', array)
  let board = { event: 'playWord', data: {play: array} }
  ws.send(JSON.stringify(board))
}

function helpFunction() {
  alert("-To play a tile on the board, 'Tap' the tile in your hand and then 'Tap' the board where you want to play it. \n -The EXCHANGE button will replace your tiles with a new hand and move on to the next player's turn. \n -The SHUFFLE button will shuffle the tiles in your hand, but will not skip your turn. \n -The DONE button is how you place a word on the board to complete your turn. \n -You will have one minute to play a word or EXCHANGE your hand before your turn is skipped. \n -The GAME EVENT box will flash red when it is your turn and the timer is counting down.")
}
