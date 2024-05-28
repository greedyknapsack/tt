import java.io.*;
import java.net.*;
import java.util.Date;

public class MulticastDateServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Create a DatagramSocket bound to any available port
        DatagramSocket socket = new DatagramSocket();

        for (int i = 0; i < 10; i++) {
            // Prepare data to send
            byte[] buf = new Date().toString().getBytes();
            
            // Get the multicast group address
            InetAddress group = InetAddress.getByName("230.0.0.1");
            
            // Create a DatagramPacket to send to the multicast group at port 1313
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 1313);
            
            // Send the packet
            socket.send(packet);
            
            // Wait for 1.5 seconds before sending the next packet
            Thread.sleep(1500);
        }

        // Close the socket
        socket.close();
    }
}

