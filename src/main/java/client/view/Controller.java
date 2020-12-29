package client.view;

import client.ViewManager;
import client.model.Client;

/**
 * Abstract super class of all view-controller
 *
 * @author simon
 */
public abstract class Controller {
    /**
     * instance of the ViewManager to call the next or previous scene
     */
    protected ViewManager viewManager = ViewManager.getInstance();
    /**
     * instance of the client to transfer inputs
     */
    protected Client client = Client.getInstance();
}
