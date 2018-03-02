const GB = require('../entities/Gameboard')
const Tile = require('../entities/Tile')
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

    const word = 'OSWEGO'
    const h = true

    g.placeWord({x: 5, y: 5}, {x: 10, y: 5}, word, h)

    expect(g.error).toBe(0)
  })

  it('Gameboard should place word "OSWEGO" vertically from (5,5) to (5, 10)', () => {
    const g = new GB()

    g.init()

    const word = 'OSWEGO'
    const h = false

    g.placeWord({x: 5, y: 5}, {x: 5, y: 10}, word, h)

    expect(g.error).toBe(0)
  })

  it('Gameboard should place a horizontal word in the cross section of a vertical word', () => {
    const g = new GB()

    g.init()

    const word = 'OSWEGO'
    const h = false

    g.placeWord({x: 5, y: 5}, {x: 5, y: 10}, word, h)

    const word2 = 'BED'
    const h2 = true

    g.placeWord({x: 4, y: 8}, {x: 6, y: 8}, word2, h2)

    expect(g.error).toBe(0)
  })

  it('Gameboard should not place a horizontal word in the cross section of a vertical word', () => {
    const g = new GB()

    g.init()

    const word = 'OSWEGO'
    const h = false

    g.placeWord({x: 5, y: 5}, {x: 5, y: 10}, word, h)

    const word2 = 'BAD'
    const h2 = true

    g.placeWord({x: 4, y: 8}, {x: 6, y: 8}, word2, h2)

    expect(g.error).toBe(3)
  })

  it('Gameboard should place a vertical word in the cross section of a horizontal word', () => {
    const g = new GB()

    g.init()

    const word = 'BED'
    const h = true

    g.placeWord({x: 4, y: 5}, {x: 6, y: 5}, word, h)

    const word2 = 'OSWEGO'
    const h2 = false

    g.placeWord({x: 5, y: 2}, {x: 5, y: 7}, word2, h2)

    expect(g.error).toBe(0)
  })

  it('Gameboard should not place a vertical word in the cross section of a horizontal word', () => {
    const g = new GB()

    g.init()

    const word = 'BED'
    const h = true

    g.placeWord({x: 4, y: 5}, {x: 6, y: 5}, word, h)

    const word2 = 'OSWAGO'
    const h2 = false

    g.placeWord({x: 5, y: 2}, {x: 5, y: 7}, word2, h2)

    expect(g.error).toBe(3)
  })

  it('Gameboard should return error trying to place a word outside the bounds of the board', () => {
    const g = new GB()

    g.init()

    const word = 'BED'
    const h = true

    g.placeWord({x: 9, y: 9}, {x: 11, y: 9}, word, h)

    expect(g.error).toBe(2)
  })

  it('Gamboard should place first word over the center tile', () => {
    const g = new GB()

    g.init()

    const word = 'BED'
    const h = false

    g.placeWord({x: 5, y: 5}, {x: 5, y: 7}, word, h)

    expect(g.error).toBe(0)
  })

  it('Gamboard should not place first word anywhere but over the center tile', () => {
    const g = new GB()

    g.init()

    const word = 'BED'
    const h = false

    g.placeWord({x: 4, y: 5}, {x: 4, y: 7}, word, h)

    expect(g.error).toBe(4)
  })

  it('Gamboard should place a word that is attached to previously played words', () => {
    const g = new GB()

    g.init()

    const word = 'BED'
    const h = true

    g.placeWord({x: 4, y: 5}, {x: 6, y: 5}, word, h)

    const word2 = 'OSWEGO'
    const h2 = false

    g.placeWord({x: 5, y: 2}, {x: 5, y: 7}, word2, h2)

    expect(g.error).toBe(0)
  })

  it('Gamboard should not place a word that is not attached to previously played words', () => {
    const g = new GB()

    g.init()

    const word = 'BED'
    const h = true

    g.placeWord({x: 4, y: 5}, {x: 6, y: 5}, word, h)

    const word2 = 'OSWEGO'
    const h2 = false

    g.placeWord({x: 1, y: 2}, {x: 1, y: 7}, word2, h2)

    expect(g.error).toBe(5)
  })

  it('Gamboard should have an error of 0', () => {
    const g = new GB()

    g.handleResponse('Oswego')

    expect(g.error).toBe(0)
  })

  it('Gamboard should have an error of 1', () => {
    const g = new GB()

    g.error = 1

    g.handleResponse('Oswego')

    expect(g.error).toBe(1)
  })

  it('Gamboard should have an error of 2', () => {
    const g = new GB()

    g.error = 2

    g.handleResponse('Oswego')

    expect(g.error).toBe(2)
  })

  it('Gamboard should have an error of 3', () => {
    const g = new GB()

    g.error = 3

    g.handleResponse('Oswego')

    expect(g.error).toBe(3)
  })

  it('Gamboard should have an error of 4', () => {
    const g = new GB()

    g.error = 4

    g.handleResponse('Oswego')

    expect(g.error).toBe(4)
  })

  it('Gamboard should have an error of 5', () => {
    const g = new GB()

    g.error = 5

    g.handleResponse('Oswego')

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
