package com.csc480.stats.jsonObjects;

/**
 * This class exists solely so GSON can correctly
 * parse the JSON returned from the database representing
 * the team stats
 */

public class NestedData {

    private ScoreData _embedded;

    public ScoreData get_embedded() {
        return _embedded;
    }

    public void set_embedded(ScoreData _embedded) {
        this._embedded = _embedded;
    }
}
