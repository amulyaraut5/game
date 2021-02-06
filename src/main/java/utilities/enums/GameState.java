package utilities.enums;

import com.google.gson.annotations.SerializedName;

import java.util.EnumSet;

public enum GameState {
    @SerializedName("0") CONSTRUCTION,
    @SerializedName("1") UPGRADE,
    @SerializedName("2") PROGRAMMING,
    @SerializedName("3") ACTIVATION;

    static {
        CONSTRUCTION.next = UPGRADE;
        UPGRADE.next = PROGRAMMING;
        PROGRAMMING.next = ACTIVATION;
        ACTIVATION.next = PROGRAMMING;

        CONSTRUCTION.allowedMessages = EnumSet.of(MessageType.SendChat, MessageType.SetStartingPoint);
        PROGRAMMING.allowedMessages = EnumSet.of(MessageType.SendChat, MessageType.SelectCard);
        ACTIVATION.allowedMessages = EnumSet.of(MessageType.SendChat, MessageType.SelectDamage, MessageType.PlayIt);
    }

    private GameState next;
    private EnumSet<MessageType> allowedMessages;

    public GameState getNext() {
        return next;
    }

    public EnumSet<MessageType> getAllowedMessages() {
        return allowedMessages;
    }
}
