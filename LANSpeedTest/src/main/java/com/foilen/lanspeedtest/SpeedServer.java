package com.foilen.lanspeedtest;

import java.net.Socket;
import java.net.SocketAddress;

import com.foilen.smalltools.net.discovery.DiscoverableService;
import com.foilen.smalltools.net.discovery.LocalBroadcastDiscoveryServer;
import com.foilen.smalltools.net.services.ExecutorWrappedSocketCallback;
import com.foilen.smalltools.net.services.SocketCallback;
import com.foilen.smalltools.tools.CloseableTools;

public class SpeedServer extends Thread implements SocketCallback {

    @Override
    public void run() {
        System.out.println("Starting server");

        // Create a service
        LocalBroadcastDiscoveryServer discoveryServer = new LocalBroadcastDiscoveryServer(SpeedTestDiscoveryContants.DISCOVERY_PORT);
        DiscoverableService discoverableService = new DiscoverableService(SpeedTestDiscoveryContants.APP_NAME, //
                SpeedTestDiscoveryContants.APP_VERSION, //
                SpeedTestDiscoveryContants.SERVICE_NAME, //
                SpeedTestDiscoveryContants.SERVICE_DESCRIPTION);
        discoveryServer.addTcpBroadcastService(discoverableService, new ExecutorWrappedSocketCallback(this));

    }

    @Override
    public void newClient(Socket socket) {
        SocketAddress remoteName = socket.getRemoteSocketAddress();
        System.out.println("Client connected " + remoteName);

        double uploadSpeed = CheckSpeed.upload(socket);
        double downloadSpeed = CheckSpeed.download(socket);

        System.out.println(remoteName + ": " + downloadSpeed + " / " + uploadSpeed + " mbits/sec | " + downloadSpeed / 8 + " / " + uploadSpeed / 8 + " mBytes/sec");

        CloseableTools.close(socket);
    }

}
