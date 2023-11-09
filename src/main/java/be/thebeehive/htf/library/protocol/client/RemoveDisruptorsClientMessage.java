package be.thebeehive.htf.library.protocol.client;

import java.util.List;
import java.util.UUID;

public class RemoveDisruptorsClientMessage extends ClientMessage {

    private UUID roundId;
    private List<Long> disruptors;

    public RemoveDisruptorsClientMessage() {

    }

    public RemoveDisruptorsClientMessage(
            UUID roundId,
            List<Long> disruptors
    ) {
        this.roundId = roundId;
        this.disruptors = disruptors;
    }

    public UUID getRoundId() {
        return roundId;
    }

    public void setRoundId(UUID roundId) {
        this.roundId = roundId;
    }

    public List<Long> getDisruptors() {
        return disruptors;
    }

    public void setDisruptors(List<Long> disruptors) {
        this.disruptors = disruptors;
    }
}
