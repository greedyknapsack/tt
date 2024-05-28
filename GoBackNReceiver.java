import java.io.*;
import java.net.*;

public class GoBackNReceiver {
    private static final int PORT = 9876;
    private static final int EXPECTED_SEQ_NUM = 0; // Expected sequence number

    private DatagramSocket socket;
    private int expectedSeqNum = EXPECTED_SEQ_NUM;

    public GoBackNReceiver() throws SocketException {
        socket = new DatagramSocket(PORT);
    }

    public void receive() throws IOException {
        while (true) {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());
            int seqNum = Integer.parseInt(received.split(":")[0]);

            if (seqNum == expectedSeqNum) {
                System.out.println("Received: " + received);
                expectedSeqNum++;
                sendAck(seqNum);
            } else {
                System.out.println("Out of order frame: " + received);
                sendAck(expectedSeqNum - 1); // Send ACK for last in-order frame
            }
        }
    }

    private void sendAck(int seqNum) throws IOException {
        String ack = String.valueOf(seqNum);
        byte[] buf = ack.getBytes();
        DatagramPacket ackPacket = new DatagramPacket(buf, buf.length, InetAddress.getByName("localhost"), 5050);
        socket.send(ackPacket);
        System.out.println("Sent ACK for frame: " + seqNum);
    }

    public static void main(String[] args) throws IOException {
        GoBackNReceiver receiver = new GoBackNReceiver();
        receiver.receive();
    }
}
