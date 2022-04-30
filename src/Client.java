import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private Socket socket;
    private String username;

    private Requests requests;

    public Client(String username) throws IOException {
        this.socket = new Socket("Localhost", 20111);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.username = username;
        this.requests = new Requests();
        listen();
        send();
    }

    public void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = bufferedReader.readLine();
                        if (msgFromGroupChat != null){
                            System.out.println(msgFromGroupChat);
                            String response = requests.input(msgFromGroupChat);

                            if(response != null){
                                System.out.println(response);
                                bufferedWriter.write(response);
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                            }
                        }

                    } catch (IOException e) {
                        System.out.println("LISTEN EXCEPTION");
                    }
                }
            }
        }).start();
    }

    public void send() {
        try {
            // Initially send the username of the client.
            bufferedWriter.write("HELLO? DISTTER/1.0 <"+username+">");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            // Create a scanner for user input.
            Scanner scanner = new Scanner(System.in);
            // While there is still a connection with the server, continue to scan the terminal and then send the message.
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                System.out.println("message sent: "+messageToSend);
            }
        } catch (IOException e) {
            // Gracefully close everything.
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        // Note you only need to close the outer wrapper as the underlying streams are closed when you close the wrapper.
        // Note you want to close the outermost wrapper so that everything gets flushed.
        // Note that closing a socket will also close the socket's InputStream and OutputStream.
        // Closing the input stream closes the socket. You need to use shutdownInput() on socket to just close the input stream.
        // Closing the socket will also close the socket's input stream and output stream.
        // Close the socket after closing the streams.
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}