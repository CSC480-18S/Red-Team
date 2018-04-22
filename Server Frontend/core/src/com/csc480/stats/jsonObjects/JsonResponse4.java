package com.csc480.stats.jsonObjects;

/**
 * This class exists solely so GSON can correctly
 * parse the JSON returned from the database representing
 * the team stats
 */

public class JsonResponse4 {
    private Metadata meta;
    private NestedData3 data;

    public NestedData3 getData() {
        return data;
    }

    public void setData(NestedData3 data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return meta;
    }

    public void setMetadata(Metadata meta) {
        this.meta = meta;
    }
}
