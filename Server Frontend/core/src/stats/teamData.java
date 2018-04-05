package stats;

/**
 * This class exists so GSON can parse the JSON from database
 * representing the team stats and populate the
 * appropriate fields in the GUI
 */

public class teamData {
    private String name;
    private String topValueWord;
    private String highestValue;
    private String longestWord;
    //typo is on purpose to replicate the DB typo
    private String higestSingleGameScore;
    private String freqPlayedWord;
    private String amountBonusesUsed;
    private String totalScore;
    private String winCount;
    private String loseCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopValueWord() {
        return topValueWord;
    }

    public void setTopValueWord(String topValueWord) {
        this.topValueWord = topValueWord;
    }

    public String getHighestValue() {
        return highestValue;
    }

    public void setHighestValue(String highestValue) {
        this.highestValue = highestValue;
    }

    public String getLongestWord() {
        return longestWord;
    }

    public void setLongestWord(String longestWord) {
        this.longestWord = longestWord;
    }

    public String getHigestSingleGameScore() {
        return higestSingleGameScore;
    }

    public void setHigestSingleGameScore(String higestSingleGameScore) {
        this.higestSingleGameScore = higestSingleGameScore;
    }

    public String getFreqPlayedWord() {
        return freqPlayedWord;
    }

    public void setFreqPlayedWord(String freqPlayedWord) {
        this.freqPlayedWord = freqPlayedWord;
    }

    public String getAmountBonusesUsed() {
        return amountBonusesUsed;
    }

    public void setAmountBonusesUsed(String amountBonusesUsed) {
        this.amountBonusesUsed = amountBonusesUsed;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getWinCount() {
        return winCount;
    }

    public void setWinCount(String winCount) {
        this.winCount = winCount;
    }

    public String getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(String loseCount) {
        this.loseCount = loseCount;
    }
}
