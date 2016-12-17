package com.foilen.lanspeedtest;

import java.net.Socket;
import java.util.List;

import com.foilen.smalltools.net.discovery.DiscoverableService;
import com.foilen.smalltools.net.discovery.LocalBroadcastDiscoveryClient;
import com.foilen.smalltools.tools.CloseableTools;
import com.foilen.smalltools.tools.ThreadTools;

public class SpeedClient extends Thread {

    @Override
    public void run() {

        // Start the discovery
        LocalBroadcastDiscoveryClient discoveryClient = new LocalBroadcastDiscoveryClient(SpeedTestDiscoveryContants.DISCOVERY_PORT);

        System.out.println("Waiting 10 secs to discover the servers");
        ThreadTools.sleep(10000);

        // Retrieve the services that were seen
        List<DiscoverableService> services = discoveryClient.retrieveServicesList(SpeedTestDiscoveryContants.SERVICE_NAME);
        System.out.println("Found " + services.size() + " services to test");

        for (DiscoverableService service : services) {

            String remoteName = service.getServerHost();

            Socket socket = service.connecToTcpService();
            System.out.println("Calculating download speed for: " + service.getServerHost());
            double downloadSpeed = CheckSpeed.download(socket);
            System.out.println("Calculating upload speed for: " + service.getServerHost());
            double uploadSpeed = CheckSpeed.upload(socket);

            System.out.println(remoteName + ": " + downloadSpeed + " / " + uploadSpeed + " mbits/sec | " + downloadSpeed / 8 + " / " + uploadSpeed / 8 + " mBytes/sec");

            CloseableTools.close(socket);

        }

    }

}
