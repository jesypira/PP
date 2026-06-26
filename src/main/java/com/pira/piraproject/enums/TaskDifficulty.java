package com.pira.piraproject.enums;

public enum TaskDifficulty {

    EASY(10, 5),
    MEDIUM(25, 10),
    HARD(50, 25);

    private final int xpReward;
    private final int goldReward;

    TaskDifficulty(int xpReward, int goldReward) {
        this.xpReward = xpReward;
        this.goldReward = goldReward;
    }

    public int getXpReward() { return xpReward; }
    public int getGoldReward() { return goldReward; }

}