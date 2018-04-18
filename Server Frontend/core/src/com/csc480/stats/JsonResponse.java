package com.csc480.stats;

/**
 * This class exists solely so GSON can correctly
 * parse the JSON returned from the database representing
 * the team stats
 */

public class JsonResponse {
    private Metadata meta;
    private teamData data;

    public teamData getData() {
        return data;
    }

    public void setData(teamData data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return meta;
    }

    public void setMetadata(Metadata meta) {
        this.meta = meta;
    }
}
