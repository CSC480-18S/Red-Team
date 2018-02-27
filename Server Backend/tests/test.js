const GB = require('../helpers/Gameboard')
const Tile = require('../helpers/Tile')
/**
 * Imports the lodash library
 */
const _ = require('lodash')

describe('Gameboard tests', () => {
  it('Gameboard should be created', () => {
    expect(new GB(11)).toBeTruthy()
  })

  it('Gameboard should be initialized', () => {
    const g = new GB(11)

    g.init()

    expect(g.initialized).toBe(true)
  })

  it('Gameboard should not be re-initialized', () => {
    const g = new GB(11)

    g.init()

    expect(g.init()).toBe(true)
  })

  it('Gameboard size should be 11', () => {
    const g = new GB(11)

    expect(g.size).toBe(11)
  })

  it('Gameboard should place word "OSWEGO" horizontally from (2,2) to (7, 2)', () => {
    const g = new GB(11)

    g.init()

    const word = 'OSWEGO'
    const x = 2
    const y = 2
    const h = true

    g.consumeInput(x, y, h, word)

    for (let i = x; i < word.length; i++) {
      for (let j = y; j <= y; j++) {
        expect(g.board[j][i].letter.toUpperCase()).toEqual(word[i - x].toUpperCase())
      }
    }
  })

  it('Gameboard should place word "OSWEGO" vertically from (2,2) to (2, 7)', () => {
    const g = new GB(11)

    g.init()

    const word = 'OSWEGO'
    const x = 2
    const y = 2
    const h = false

    g.consumeInput(x, y, h, word)

    for (let i = x; i <= x; i++) {
      for (let j = y; j < word.length; j++) {
        expect(g.board[j][i].letter.toUpperCase()).toEqual(word[j - y].toUpperCase())
      }
    }
  })

  it('Gameboard should not place a horizontal word in the cross section of a vertical word', () => {
    const g = new GB(11)

    g.init()

    // Vertical Word
    const word = 'OSWEGO'
    const x = 2
    const y = 2
    const h = false

    // Horizontal Word
    const word2 = 'BAD'
    const x2 = 1
    const y2 = 5
    const h2 = true

    g.consumeInput(x, y, h, word)
    const validBoard = _.cloneDeep(g.board)
    g.consumeInput(x2, y2, h2, word2)

    for (let i = 0; i < g.board.length; i++) {
      for (let j = 0; j < g.board[0].length; j++) {
        expect(g.board[j][i].letter.toUpperCase()).toEqual(validBoard[j][i].letter.toUpperCase())
      }
    }
  })

  it('Gameboard should not place a word on top of the same word', () => {
    const g = new GB(11)

    g.init()

    // Vertical Word
    const word = 'DOGE'
    const x = 0
    const y = 0
    const h = true

    // Horizontal Word
    const word2 = 'DOGE'
    const x2 = 0
    const y2 = 0
    const h2 = true

    g.consumeInput(x, y, h, word)
    const result = g.consumeInput(x2, y2, h2, word2)

    expect(result).toEqual({
      reason: 'Word: ' + word + ' placed on top of same word: ' + word,
      word: word
    })
  })

  it('Gameboard should place a horizontal word in the cross section of a vertical word', () => {
    const g = new GB(11)

    g.init()

    // Vertical Word
    const word = 'OSWEGO'
    const x = 2
    const y = 2
    const h = false

    // Horizontal Word
    const word2 = 'BED'
    const x2 = 1
    const y2 = 5
    const h2 = true

    g.consumeInput(x, y, h, word)
    g.consumeInput(x2, y2, h2, word2)

    for (let i = x2; i < word2.length; i++) {
      for (let j = y2; j <= y2; j++) {
        expect(g.board[j][i].letter.toUpperCase()).toEqual(word2[i - x2])
      }
    }
  })

  it('Gameboard should not place a vertical word in the cross section of a horizontal word', () => {
    const g = new GB(11)

    g.init()

    // Horitontal Word
    const word = 'BAD'
    const x = 1
    const y = 5
    const h = true

    // Vertical Word
    const word2 = 'OSWEGO'
    const x2 = 2
    const y2 = 2
    const h2 = false

    g.consumeInput(x, y, h, word)
    const validBoard = _.cloneDeep(g.board)
    g.consumeInput(x2, y2, h2, word2)

    for (let i = 0; i < g.board.length; i++) {
      for (let j = 0; j < g.board[0].length; j++) {
        expect(g.board[j][i].letter.toUpperCase()).toEqual(validBoard[j][i].letter.toUpperCase())
      }
    }
  })

  it('Gameboard should place a vertical word in the cross section of a horizontal word', () => {
    const g = new GB(11)

    g.init()

    // Horizontal Word
    const word = 'BED'
    const x = 1
    const y = 5
    const h = true

    // Vertical Word
    const word2 = 'OSWEGO'
    const x2 = 2
    const y2 = 2
    const h2 = false

    g.consumeInput(x, y, h, word)
    g.consumeInput(x2, y2, h2, word2)

    for (let i = x2; i <= x2; i++) {
      for (let j = y2; j < word2.length; j++) {
        expect(g.board[j][i].letter.toUpperCase()).toEqual(word2[j - y2])
      }
    }
  })

  it('Gameboard should return error trying to place an invalid word', () => {
    const g = new GB(11)

    g.init()

    const word = ''
    const x = 9
    const y = 5
    const h = true

    const result = g.consumeInput(x, y, h, word)

    expect(result).toEqual({
      reason: 'Not a valid word',
      word: word
    })
  })

  it('Gameboard should return error trying to place a word outside the bounds of the board', () => {
    const g = new GB(11)

    g.init()

    const word = 'BED'
    const x = 9
    const y = 5
    const h = true

    const result = g.consumeInput(x, y, h, word)

    expect(result).toEqual({
      reason: 'Placed out of the bounds of the board',
      word: word
    })
  })
})

describe('Tile tests', () => {
  it('Tile should be created', () => {
    const tile = new Tile(1, 1, '0')

    expect(tile).toBeTruthy()
  })

  it('Tile should be at x = 1', () => {
    const tile = new Tile(1, 1, '0')

    expect(tile.x).toBe(1)
  })

  it('Tile should be at y = 1', () => {
    const tile = new Tile(1, 1, '0')

    expect(tile.y).toBe(1)
  })

  it('Tile should be have a multiplier of 2W', () => {
    const tile = new Tile(1, 1, '2W')

    expect(tile.multiplier).toBe('2W')
  })

  it('Tile should be the letter "Q"', () => {
    const tile = new Tile(1, 1, '0')

    tile.letter = 'Q'

    expect(tile.letter).toBe('Q')
  })

  it('Tile should have a letter placed', () => {
    const tile = new Tile(1, 1, '0')

    tile.letter = 'Q'

    expect(tile.letterPlaced).toBe(true)
  })

  it('Tile should not have a letter placed', () => {
    const tile = new Tile(1, 1, '0')

    expect(tile.letterPlaced).toBe(false)
  })
})
