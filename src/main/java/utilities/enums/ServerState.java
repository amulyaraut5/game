package utilities.enums;

import java.util.EnumSet;

public enum ServerState {
    LOBBY, RUNNING_GAME;

    static {
        LOBBY.allowedMessages = EnumSet.of(MessageType.HelloServer,
                MessageType.PlayerValues, MessageType.SendChat, MessageType.SetStatus, MessageType.MapSelected);
        RUNNING_GAME.allowedMessages = EnumSet.of(MessageType.SendChat, MessageType.SetStartingPoint,
                MessageType.SelectCard, MessageType.SelectDamage, MessageType.PlayIt);
    }

    private EnumSet<MessageType> allowedMessages;

    public EnumSet<MessageType> getAllowedMessages() {
        return allowedMessages;
    }
}
