package utilities.enums;

import java.util.EnumSet;

public enum ServerState {
    ADD_PLAYERS, RUNNING_GAME;

    static {
        ADD_PLAYERS.allowedMessages = EnumSet.of(MessageType.HelloServer,
                MessageType.PlayerValues, MessageType.SendChat, MessageType.SetStatus);
        RUNNING_GAME.allowedMessages = EnumSet.of(MessageType.SendChat, MessageType.SetStartingPoint,
                MessageType.SelectCard, MessageType.SelectDamage, MessageType.PlayIt);
    }

    private EnumSet<MessageType> allowedMessages;

    public EnumSet<MessageType> getAllowedMessages() {
        return allowedMessages;
    }
}
