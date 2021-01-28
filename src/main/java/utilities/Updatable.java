package utilities;

import utilities.JSONProtocol.JSONMessage;

public interface Updatable {
    void update(JSONMessage message);
}