package be.thebeehive.htf.library.protocol.server;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class GameRoundServerMessage extends ServerMessage {

    private long round;
    private UUID roundId;
    private long startHealth;
    private int disruptorsToRemove;
    private List<Long> newDisruptors;
    private Jungle ourJungle;
    private List<Jungle> competingJungles;
    private List<JungleDisruptor> disruptors;

    public GameRoundServerMessage() {

    }

    public long getRound() {
        return round;
    }

    public void setRound(long round) {
        this.round = round;
    }

    public UUID getRoundId() {
        return roundId;
    }

    public void setRoundId(UUID roundId) {
        this.roundId = roundId;
    }

    public long getStartHealth() {
        return startHealth;
    }

    public void setStartHealth(long startHealth) {
        this.startHealth = startHealth;
    }

    public int getDisruptorsToRemove() {
        return disruptorsToRemove;
    }

    public void setDisruptorsToRemove(int disruptorsToRemove) {
        this.disruptorsToRemove = disruptorsToRemove;
    }

    public List<Long> getNewDisruptors() {
        return newDisruptors;
    }

    public void setNewDisruptors(List<Long> newDisruptors) {
        this.newDisruptors = newDisruptors;
    }

    public Jungle getOurJungle() {
        return ourJungle;
    }

    public void setOurJungle(Jungle ourJungle) {
        this.ourJungle = ourJungle;
    }

    public List<Jungle> getCompetingJungles() {
        return competingJungles;
    }

    public void setCompetingJungles(List<Jungle> competingJungles) {
        this.competingJungles = competingJungles;
    }

    public List<JungleDisruptor> getDisruptors() {
        return disruptors;
    }

    public void setDisruptors(List<JungleDisruptor> disruptors) {
        this.disruptors = disruptors;
    }

    public static class Jungle {

        private String name;
        private BigDecimal health;
        private boolean isAlive;
        private List<Long> disruptors;

        public Jungle() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getHealth() {
            return health;
        }

        public void setHealth(BigDecimal health) {
            this.health = health;
        }

        public boolean isAlive() {
            return isAlive;
        }

        public void setAlive(boolean alive) {
            isAlive = alive;
        }

        public List<Long> getDisruptors() {
            return disruptors;
        }

        public void setDisruptors(List<Long> disruptors) {
            this.disruptors = disruptors;
        }
    }

    public static class JungleDisruptor {

        private long id;
        private long initialRound;
        private long maxRounds;
        private BigDecimal activationChance;
        private List<JungleDisruptorStat> stats;

        public JungleDisruptor() {

        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getInitialRound() {
            return initialRound;
        }

        public void setInitialRound(long initialRound) {
            this.initialRound = initialRound;
        }

        public long getMaxRounds() {
            return maxRounds;
        }

        public void setMaxRounds(long maxRounds) {
            this.maxRounds = maxRounds;
        }

        public BigDecimal getActivationChance() {
            return activationChance;
        }

        public void setActivationChance(BigDecimal activationChance) {
            this.activationChance = activationChance;
        }

        public List<JungleDisruptorStat> getStats() {
            return stats;
        }

        public void setStats(List<JungleDisruptorStat> stats) {
            this.stats = stats;
        }
    }

    public static class JungleDisruptorStat {

        private BigDecimal initialDamage;
        private BigDecimal roundMultiplier;

        public JungleDisruptorStat() {

        }

        public BigDecimal getInitialDamage() {
            return initialDamage;
        }

        public void setInitialDamage(BigDecimal initialDamage) {
            this.initialDamage = initialDamage;
        }

        public BigDecimal getRoundMultiplier() {
            return roundMultiplier;
        }

        public void setRoundMultiplier(BigDecimal roundMultiplier) {
            this.roundMultiplier = roundMultiplier;
        }
    }
}
