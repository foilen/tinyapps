/*
    Tinyapps
    https://github.com/foilen/tinyapps
    Copyright (c) 2014-2024 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package ca.pgon.freenetknowledge.spiders.news;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewsUtils {
    static private final Logger logger = Logger.getLogger(NewsUtils.class.getName());

    private String host;
    private int port;
    private Socket connection;
    private InputStream in;
    private OutputStream out;

    // Temporary variables
    private int totalArticles;

    private void connect() throws Exception {
        if (connection == null || connection.isClosed()) {
            connection = new Socket(host, port);
            in = connection.getInputStream();
            out = connection.getOutputStream();

            getNextLine();
        }
    }

    private void disconnect() throws Exception {
        connection.close();
    }

    public List<String> getAllGroups() {
        logger.fine("Getting all groups");
        try {
            connect();

            List<String> result = new Vector<String>();
            totalArticles = 0;

            sendCommand("list");
            getNextLine();
            String[] groups = getLines().split("\n");

            for (String g : groups) {
                String[] groupStatus = g.split(" ");
                int nbPos = groupStatus.length - 3;
                if (nbPos != 1) {
                    continue;
                }
                totalArticles += Integer.parseInt(groupStatus[nbPos]);
                String name = "";
                for (int i = 0; i < nbPos; ++i) {
                    if (i != 0) {
                        name += " ";
                    }
                    name += groupStatus[i];
                }

                result.add(name);
            }

            disconnect();

            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error getting groups", e);
            return null;
        }
    }

    public List<String> getAllMessagesInGroup(String group) {
        logger.log(Level.FINE, "Getting all messages in the group {0}", group);
        try {
            connect();

            List<String> result = new ArrayList<String>();

            sendCommand("GROUP " + group);
            String statusLine = skipTillNumber();
            String[] status = statusLine.split(" ");

            int nbArticle = Integer.parseInt(status[1]);
            String nextArticle = status[2];

            if (nbArticle > 0) {
                while (true) {
                    result.add(getArticle(nextArticle));
                    sendCommand("NEXT");
                    status = skipTillNumber().split(" ");
                    int statusCode = Integer.parseInt(status[0]);

                    if ((statusCode < 200) || (statusCode > 299)) {
                        break;
                    }
                    nextArticle = status[1];
                }
            }

            disconnect();

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private String getArticle(String articleId) throws Exception {
        logger.log(Level.FINE, "Getting article {0}", articleId);

        sendCommand("BODY " + articleId);
        return getLines();
    }

    public String getHost() {
        return host;
    }

    private String getLines() throws Exception {
        StringBuffer result = new StringBuffer();

        boolean first = true;

        while (true) {
            String nextLine = getNextLine();
            if (nextLine.equals(".")) {
                break;
            }

            if (first) {
                first = false;
            } else {
                result.append('\n');
            }

            result.append(nextLine);
        }

        return result.toString();
    }

    private String getNextLine() throws Exception {
        StringBuffer result = new StringBuffer();

        int c;
        while ((c = in.read()) != -1) {
            if (c == '\r') {
                continue;
            }
            if (c == '\n') {
                break;
            }

            result.append((char) c);
        }

        String stringResult = result.toString();
        if (stringResult.isEmpty()) {
            return getNextLine();
        }

        return stringResult;
    }

    public int getPort() {
        return port;
    }

    public int getTotalMessagesCount() {
        if (totalArticles == 0) {
            getAllGroups();
        }
        return totalArticles;
    }

    private void sendCommand(String command) throws Exception {
        command += "\r\n";
        out.write(command.getBytes());
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private String skipTillNumber() throws Exception {
        String result = "";

        boolean found = false;

        while (!found) {
            result = getNextLine();
            try {
                Integer.parseInt(result.split(" ")[0]);
                found = true;
            } catch (Exception e) {

            }
        }

        return result;
    }
}
