package be.thebeehive.htf.client;

import be.thebeehive.htf.library.HtfClient;
import be.thebeehive.htf.library.HtfClientListener;
import be.thebeehive.htf.library.protocol.client.RemoveDisruptorsClientMessage;
import be.thebeehive.htf.library.protocol.server.ErrorServerMessage;
import be.thebeehive.htf.library.protocol.server.GameEndedServerMessage;
import be.thebeehive.htf.library.protocol.server.GameRoundServerMessage;
import be.thebeehive.htf.library.protocol.server.WarningServerMessage;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MyClient implements HtfClientListener {
    private static final Logger LOGGER = Logger.getLogger(MyClient.class.getName());
    /**
     * You tried to perform an action that is not allowed.
     * An error occurred, and we are unable to recover from this.
     * You will also be disconnected.
     */
    @Override
    public void onErrorServerMessage(HtfClient client, ErrorServerMessage msg) throws Exception {
        LOGGER.log(Level.SEVERE, msg.getMsg());
    }

    /**
     * The game finished. Did you win?
     */
    @Override
    public void onGameEndedServerMessage(HtfClient client, GameEndedServerMessage msg) throws Exception {
        LOGGER.log(Level.INFO, "Game ended!");
    }

    /**
     * A new round has started.
     * You must reply within 1 second!
     */
    @Override
    public void onGameRoundServerMessage(HtfClient client, GameRoundServerMessage msg) throws Exception {
        LOGGER.info("Disruptor this round");
        logDisruptors(msg.getDisruptors(), msg.getRoundId());

        List<GameRoundServerMessage.JungleDisruptor> disruptors = msg.getDisruptors();

        Collections.sort(disruptors, (d1, d2) -> d2.getStats().get(0).getInitialDamage().compareTo(d1.getStats().get(0).getInitialDamage()));

        // For example, you might decide to remove the disruptors with the highest initial damage.
        List<Long> disruptorsToRemove = disruptors.stream()
                .limit(msg.getDisruptorsToRemove())
                .map(GameRoundServerMessage.JungleDisruptor::getId)
                .collect(Collectors.toList());

        LOGGER.info("Disruptor to remove");
        logDisruptors(disruptors.stream()
                .filter(disruptor -> disruptorsToRemove.contains(disruptor.getId()))
                .collect(Collectors.toList()), msg.getRoundId());

        // Send a RemoveDisruptorsClientMessage with the IDs of the disruptors to remove.
        client.send(new RemoveDisruptorsClientMessage(msg.getRoundId(), disruptorsToRemove));
    }


    /**
     * You tried to perform an action that is not allowed.
     * An error occurred but you can still play along.
     * You will NOT be disconnected.
     */
    @Override
    public void onWarningServerMessage(HtfClient client, WarningServerMessage msg) throws Exception {
        LOGGER.log(Level.INFO, "Invalid move");
    }

    private void logDisruptors(List<GameRoundServerMessage.JungleDisruptor> dis, UUID roundId) {

        for (GameRoundServerMessage.JungleDisruptor disruptor : dis) {
            StringBuilder disruptorLog = new StringBuilder();
            disruptorLog.append("Disruptor Details:\n")
                    .append("ID: ").append(disruptor.getId()).append("\n")
                    .append("Init rounds: ").append(disruptor.getInitialRound()).append("\n")
                    .append("Max rounds: ").append(disruptor.getMaxRounds()).append("\n")
                    .append("Activation chance: ").append(disruptor.getActivationChance()).append("\n");

            List<GameRoundServerMessage.JungleDisruptorStat> stats = disruptor.getStats();
            for (GameRoundServerMessage.JungleDisruptorStat stat : stats) {
                disruptorLog.append("Init damage: ").append(stat.getInitialDamage()).append("\n")
                        .append("Round multiplier: ").append(stat.getRoundMultiplier()).append("\n");
            }
            LOGGER.info(disruptorLog.toString());
        }
    }

    private BigDecimal calculateTotalDamage(GameRoundServerMessage.JungleDisruptor disruptor, int rounds) {
        BigDecimal totalDamage = BigDecimal.ZERO;
        BigDecimal damage = disruptor.getStats().get(0).getInitialDamage();

        for (int i = 0; i < rounds; i++) {
            totalDamage = totalDamage.add(damage);
            damage = damage.multiply(disruptor.getStats().get(0).getRoundMultiplier());
        }

        return totalDamage;
    }

}
