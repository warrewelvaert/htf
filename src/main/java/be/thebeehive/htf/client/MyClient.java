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
import java.util.Comparator;
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
        // Get the list of disruptors.
        List<GameRoundServerMessage.JungleDisruptor> disruptors = msg.getDisruptors();
        int w1 = 23;
        int w2 = 10;
        int w3 = 54;
        int w4 = 30;
        int w5 = 1000;

        List<WeightedDisruptor> weightedDisruptors = disruptors.stream()
                .map(disruptor -> {
                    double weight = (disruptor.getStats().get(0).getInitialDamage().doubleValue() * w1) +
                            (disruptor.getStats().get(0).getRoundMultiplier().doubleValue() * w2) +
                            (disruptor.getActivationChance().doubleValue() * w3);

                    // If max rounds is -1, treat it as infinite and assign a high value
                    if (disruptor.getMaxRounds() == -1) {
                        weight += w4 * w5;  // Assign a high value for infinite duration
                    } else {
                        weight += disruptor.getMaxRounds() * w4;
                    }

                    return new WeightedDisruptor(disruptor, weight);
                })
                .collect(Collectors.toList());



        // Sort the disruptors in descending order of their weights
        List<WeightedDisruptor> sortedDisruptors = weightedDisruptors.stream()
                .sorted(Comparator.comparing(WeightedDisruptor::getWeight).reversed())
                .collect(Collectors.toList());

        // Select the disruptors to remove based on the sorted list
        List<Long> disruptorsToRemove = sortedDisruptors.stream()
                .filter(disruptor -> msg.getOurJungle().getDisruptors().contains(disruptor.getDisruptor().getId())) // Only include disruptors that are in the client's jungle
                .limit(msg.getDisruptorsToRemove())
                .map(disruptor -> disruptor.getDisruptor().getId())
                .collect(Collectors.toList());


        // Log the disruptors to be removed
        //LOGGER.info("Disruptors to be removed: " + disruptorsToRemove);

        // Validate the disruptors to remove
        DisruptorValidator disruptorValidator = new DisruptorValidator();
        if (disruptorValidator.validateDisruptors(msg, disruptorsToRemove)) {
            // Send a RemoveDisruptorsClientMessage with the IDs of the disruptors to remove.
            client.send(new RemoveDisruptorsClientMessage(msg.getRoundId(), disruptorsToRemove));
        } else {
            // handle error
            System.out.println("Error: Cannot remove the selected disruptors.");
        }

    }

    public static class WeightedDisruptor {
        private final GameRoundServerMessage.JungleDisruptor disruptor;
        private final double weight;

        public WeightedDisruptor(GameRoundServerMessage.JungleDisruptor disruptor, double weight) {
            this.disruptor = disruptor;
            this.weight = weight;
        }

        public GameRoundServerMessage.JungleDisruptor getDisruptor() {
            return disruptor;
        }

        public double getWeight() {
            return weight;
        }
    }

    /**
     * You tried to perform an action that is not allowed.
     * An error occurred but you can still play along.
     * You will NOT be disconnected.
     */
    @Override
    public void onWarningServerMessage(HtfClient client, WarningServerMessage msg) throws Exception {
        LOGGER.log(Level.INFO, msg.getMsg());
    }

    private void logDisruptors(List<GameRoundServerMessage.JungleDisruptor> dis) {
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

    public class DisruptorValidator {

        public boolean validateDisruptors(GameRoundServerMessage gameRoundServerMessage, List<Long> disruptorsToBeRemoved) {
            int disruptorsToRemove = gameRoundServerMessage.getDisruptorsToRemove();
            List<GameRoundServerMessage.JungleDisruptor> disruptors = gameRoundServerMessage.getDisruptors();

            // Check the number of disruptors to remove
            if (disruptorsToBeRemoved.size() > disruptorsToRemove) {
                System.out.println("Error: Cannot remove more disruptors than allowed.");
                return false;
            }

            // Check if disruptors are initialized
            for (Long disruptorId : disruptorsToBeRemoved) {
                boolean disruptorExists = disruptors.stream()
                        .anyMatch(disruptor -> disruptor.getId() == disruptorId);

                if (!disruptorExists) {
                    System.out.println("Error: Cannot remove a disruptor that is not initialized.");
                    return false;
                }
            }
            return true;
        }
    }
}
