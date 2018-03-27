const GB = require('../entities/Gameboard')
const Tile = require('../entities/Tile')
const GameManager = require('../entities/GameManager')
const PlayerManager = require('../entities/PlayerManager')
const FrontendManager = require('../entities/FrontendManager')
const ServerManager = require('../entities/ServerManager')

/**
 * Imports the lodash library
 */
const _ = require('lodash')

describe('Gameboard tests', () => {
  it('Gameboard should be created', () => {
    expect(new GB()).toBeTruthy()
  })

  it('Gameboard should be initialized', () => {
    const g = new GB()

    g.init()

    expect(g.initialized).toBe(true)
  })

  it('Gameboard should not be re-initialized', () => {
    const g = new GB()

    g.init()

    expect(g.init()).toBe(true)
  })

  it('Gameboard size should be 11', () => {
    const g = new GB()

    expect(g.size).toBe(11)
  })

  it('Gameboard should place word "OSWEGO" horizontally from (5,5) to (10, 5)', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'oswego',
      x: 5,
      y: 5,
      h: true
    }

    g.placeWords([word])

    expect(g.error).toBe(0)
  })

  it('Gameboard should place word "OSWEGO" vertically from (5,5) to (5, 10)', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'oswego',
      x: 5,
      y: 5,
      h: false
    }

    g.placeWords([word])

    expect(g.error).toBe(0)
  })

  it('Gameboard should place a horizontal word in the cross section of a vertical word', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'oswego',
      x: 5,
      y: 5,
      h: false
    }

    const word2 = {
      word: 'bed',
      x: 4,
      y: 8,
      h: true
    }

    g.placeWords([word, word2])

    expect(g.error).toBe(0)
  })

  it('Gameboard should not place a horizontal word in the cross section of a vertical word', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'oswego',
      x: 5,
      y: 5,
      h: false
    }

    const word2 = {
      word: 'bad',
      x: 4,
      y: 8,
      h: true
    }

    g.placeWords([word, word2])

    expect(g.error).toBe(3)
  })

  it('Gameboard should place a vertical word in the cross section of a horizontal word', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'bed',
      x: 5,
      y: 5,
      h: true
    }

    const word2 = {
      word: 'oswego',
      x: 6,
      y: 2,
      h: false
    }

    g.placeWords([word, word2])

    expect(g.error).toBe(0)
  })

  it('Gameboard should not place a vertical word in the cross section of a horizontal word', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'bad',
      x: 5,
      y: 5,
      h: true
    }

    const word2 = {
      word: 'oswego',
      x: 6,
      y: 2,
      h: false
    }

    g.placeWords([word, word2])

    expect(g.error).toBe(3)
  })

  it('Gameboard should return error trying to place a word outside the bounds of the board', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'fantastic',
      x: 5,
      y: 5,
      h: true
    }

    g.placeWords([word])

    expect(g.error).toBe(2)
  })

  it('Gamboard should place first word over the center tile', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'oswego',
      x: 5,
      y: 5,
      h: true
    }

    g.placeWords([word])

    expect(g.error).toBe(0)
  })

  it('Gamboard should not place first word anywhere but over the center tile', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'oswego',
      x: 3,
      y: 5,
      h: false
    }

    g.placeWords([word])

    expect(g.error).toBe(4)
  })

  it('Gamboard should not place a word that is not attached to previously played words', () => {
    const g = new GB()

    g.init()

    const word = {
      word: 'bad',
      x: 5,
      y: 5,
      h: true
    }

    const word2 = {
      word: 'oswego',
      x: 2,
      y: 1,
      h: false
    }

    g.placeWords([word, word2])

    expect(g.error).toBe(5)
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

describe('Game Manager tests', () => {
})

describe('Player tests', () => {
})
