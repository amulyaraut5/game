package utilities.enums;

public enum MessageType {
    HelloClient, HelloServer, Welcome,
    PlayerValues, PlayerAdded, SetStatus,
    PlayerStatus, GameStarted, SendChat,
    ReceivedChat, Error, ConnectionUpdate,
    ActivePhase, CardPlayed, CardSelected,
    CardsYouGotNow, CurrentCards, CurrentPlayer,
    DiscardHand, DrawDamage, Movement, NotYourCards,
    PickDamage, PlayCard, SelectCard, SelectDamage,
    SelectionFinished, SetStartingPoint, ShuffleCoding,
    StartingPointTaken, TimerEnded, TimerStarted,
    YourCards, PlayerTurning, PlayIt, Reboot, Energy,
    GameWon, CheckPointReached
}
