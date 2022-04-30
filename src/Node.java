import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Node {
    int port = 20111;

    public Node () {
    }

    public void startNode() {

    }

    public void awaitInput() {
        Boolean await = true; //used to wait for state selection
        int type = 0; //0 = unassigned, 1 = client, 2 = server
        String username = null;
        String command = null;

        while (await) {
            Scanner myObj = new Scanner(System.in);
            System.out.println("username:");
            username = myObj.nextLine();

            System.out.println("type:");
            command = myObj.nextLine();

            switch (command) {
                case "host":
                    type = 2;
                    await = false;
                    username += "_server";
                    break;

                case "client":
                    type = 1;
                    await = false;
                    username += "_client";
                    break;

                default:
                    System.out.println("invalid command - please try again.");
            }
        }

        switch (type){
            case 1:
                System.out.println("client");
                try {
                    client(username);
                } catch (IOException e) {
                    System.out.println("ERROR INITIATING CLIENT");
                }
                break;

            case 2:
                System.out.println("host");
                host();
        }
    }

    public void host() {
        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            server.setReuseAddress(false);

            while (true) {
                Socket client = server.accept();

                System.out.println("New client connected"
                        + client.getInetAddress()
                        .getHostAddress());

                Thread object = new Thread(new InboundHandler(client));
                object.start();
            }
        }
        catch (IOException e) {
            System.out.println("EXCEPTION");
        }
    }

    public void client(String username) throws IOException {
        Client client = new Client(username);
    }

    public static void main(String[] args) {
        Node node = new Node();
        node.awaitInput();
    }
}
