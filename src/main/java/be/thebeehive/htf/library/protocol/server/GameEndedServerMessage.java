package be.thebeehive.htf.library.protocol.server;

import java.math.BigDecimal;
import java.util.List;

public class GameEndedServerMessage extends ServerMessage {

    private long round;
    private List<LeaderboardUser> leaderboard;

    public GameEndedServerMessage() {

    }

    public long getRound() {
        return round;
    }

    public void setRound(long round) {
        this.round = round;
    }

    public List<LeaderboardUser> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<LeaderboardUser> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public static class LeaderboardUser {

        private String name;
        private BigDecimal lastHealth;
        private long lastRound;

        public LeaderboardUser() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getLastHealth() {
            return lastHealth;
        }

        public void setLastHealth(BigDecimal lastHealth) {
            this.lastHealth = lastHealth;
        }

        public long getLastRound() {
            return lastRound;
        }

        public void setLastRound(long lastRound) {
            this.lastRound = lastRound;
        }
    }
}
