package com.csc480.stats.jsonObjects;

/**
 * This class exists solely so GSON can correctly
 * parse the JSON returned from the database representing
 * the team stats
 */

public class NestedData2 {

    private PlayerData _embedded;

    public PlayerData get_embedded() {
        return _embedded;
    }

    public void set_embedded(PlayerData _embedded) {
        this._embedded = _embedded;
    }
}
