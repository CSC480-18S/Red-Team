package com.csc480.stats.jsonObjects;

/**
 * This class exists solely so GSON can correctly
 * parse the JSON returned from the database representing
 * the team stats
 */

public class JsonResponse5 {
    private Metadata meta;
    private Word data;

    public Word getData() {
        return data;
    }

    public void setData(Word data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return meta;
    }

    public void setMetadata(Metadata meta) {
        this.meta = meta;
    }
}
