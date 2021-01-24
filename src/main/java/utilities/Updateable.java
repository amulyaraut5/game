package utilities;

import utilities.JSONProtocol.JSONMessage;

public interface Updateable {
    public abstract void update(JSONMessage message);
}
