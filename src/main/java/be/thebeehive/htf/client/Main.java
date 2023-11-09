package be.thebeehive.htf.client;

import be.thebeehive.htf.library.EnvironmentType;
import be.thebeehive.htf.library.HtfClient;
import be.thebeehive.htf.library.protocol.client.RemoveDisruptorsClientMessage;

import java.net.URISyntaxException;
import java.rmi.server.UID;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        HtfClient client = new HtfClient(
                "wss://htf.b9s.dev/ws",
                "Felicitas5507",
                EnvironmentType.SIMULATION,
                new MyClient()
        );
        Runtime.getRuntime().addShutdownHook(new Thread(client::close));
        client.connect();
    }
}
