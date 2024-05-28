import java.io.*;
import java.net.*;

public class MulticastDateClient {
    public static void main(String[] args) throws IOException {
        // Create a MulticastSocket bound to port 1313
        MulticastSocket socket = new MulticastSocket(1313);
        
        // Get the multicast group address
        InetAddress group = InetAddress.getByName("230.0.0.1");
        
        // Join the multicast group
        socket.joinGroup(group);

        // Listen for packets from the multicast group
        for (int i = 0; i < 10; i++) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
            // Receive the packet
            socket.receive(packet);
            
            // Convert the received data to a string
            String received = new String(packet.getData(), 0, packet.getLength());
            
            // Print the received date and time
            System.out.println("Current server time: " + received);
        }

        // Leave the multicast group and close the socket
        socket.leaveGroup(group);
        socket.close();
    }
}

//Output:

//Current server time is: Mon Jul 23 17:15:06 GMT 2018
