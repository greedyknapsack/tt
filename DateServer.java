import java.io.*;
import java.net.*;
import java.util.Date;

public class DateServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            // Create a server socket listening on port 5050
            serverSocket = new ServerSocket(5050);
            System.out.println("Server is listening on port 5050");

            while (true) {
                // Accept client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                // Create an output stream to send data to the client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Send the current date and time to the client
                out.println(new Date().toString());

                // Close the client socket
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port 5050");
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
