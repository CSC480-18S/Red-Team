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

test('Gameboard size should be 15', t => {
  const g = new GB(15)

  t.deepEqual(g.size, 15)
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

test('Tile have a letter placed', t => {
  const tile = new Tile(1, 1, '0')

  tile.letter = 'Q'

  t.true(tile.letterPlaced)
})
