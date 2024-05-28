import java.io.*;
import java.net.*;

public class DateClient {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;

        try {
            // Connect to the server on localhost, port 5050
            socket = new Socket("localhost", 5050);

            // Create an input stream to read data from the server
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read and print the date and time sent by the server
            String serverResponse = in.readLine();
            System.out.println("Current date and time from server: " + serverResponse);
        } catch (IOException e) {
            System.err.println("Could not connect to the server");
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
