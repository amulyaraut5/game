package utilities.JSONProtocol.body;

import utilities.JSONProtocol.JSONBody;

public class Error extends JSONBody {
    private final String error;

    public Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
