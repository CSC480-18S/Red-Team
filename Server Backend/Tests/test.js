/*eslint-disable */ /* ava needs to be imported, by eslint doesn't like it, so its ignored */
import test, { ava } from 'ava'
/* eslint-enable */

const GB = require('../helpers/Gameboard')
const Tile = require('../helpers/Tile')

test('Gameboard should be created', t => {
  t.truthy(new GB(15))
})

test('Gameboard should be initialized', t => {
  const g = new GB(15)

  g.init()

  t.true(g.initialized)
})

test('Gameboard should not be re-initialized', t => {
  const g = new GB(15)

  g.init()

  t.true(g.init())
})

test('Gameboard size should be 15', t => {
  const g = new GB(15)

  t.deepEqual(g.size, 15)
})

test('Gameboard should place word "OSWEGO" horizontally from (2,2) to (7, 2)', t => {
  const g = new GB(15)

  g.init()

  const word = 'OSWEGO'
  const startX = 2
  const startY = 2
  const endX = 7
  const endY = 2

  g.placeWord({ x: startX, y: startY }, { x: endX, y: endY }, word)

  for (let i = startX; i <= endX; i++) {
    for (let j = startY; j <= endY; j++) {
      t.deepEqual(g.board[j][i].letter.toUpperCase(), word[i - startX].toUpperCase())
    }
  }
})

test('Gameboard should place word "OSWEGO" vertically from (2,2) to (2, 7)', t => {
  const g = new GB(15)

  g.init()

  const word = 'OSWEGO'
  const startX = 2
  const startY = 2
  const endX = 2
  const endY = 7

  g.placeWord({ x: startX, y: startY }, { x: endX, y: endY }, word)

  for (let i = startX; i <= endX; i++) {
    for (let j = startY; j <= endY; j++) {
      t.deepEqual(g.board[j][i].letter.toUpperCase(), word[j - startY].toUpperCase())
    }
  }
})

test('Tile should be created', t => {
  const tile = new Tile(1, 1, '0')

  t.truthy(tile)
})

test('Tile should be at x = 1', t => {
  const tile = new Tile(1, 1, '0')

  t.deepEqual(tile.x, 1)
})

test('Tile should be at y = 1', t => {
  const tile = new Tile(1, 1, '0')

  t.deepEqual(tile.y, 1)
})

test('Tile should be have a multiplier of 2W', t => {
  const tile = new Tile(1, 1, '2W')

  t.deepEqual(tile.multiplier, '2W')
})

test('Tile should be the letter "Q"', t => {
  const tile = new Tile(1, 1, '0')

  tile.letter = 'Q'

  t.deepEqual(tile.letter, 'Q')
})

test('Tile should have a letter placed', t => {
  const tile = new Tile(1, 1, '0')

  tile.letter = 'Q'

  t.true(tile.letterPlaced)
})

test('Tile should not have a letter placed', t => {
  const tile = new Tile(1, 1, '0')

  t.false(tile.letterPlaced)
})
