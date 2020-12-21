package Utilities.JSONProtocol.specialMessages;

import Utilities.JSONProtocol.JSONBody;

public class Error extends JSONBody {

    private String error;

    public Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
