import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class GoBackNSender {
    private static final int WINDOW_SIZE = 4; // Window size
    private static final int TIMEOUT = 2000; // Timeout in milliseconds

    private DatagramSocket socket;
    private InetAddress receiverAddress;
    private int receiverPort;
    private int base = 0; // Base of the window
    private int nextSeqNum = 0; // Next sequence number
    private Timer timer;

    public GoBackNSender(String receiverAddress, int receiverPort) throws IOException {
        this.socket = new DatagramSocket();
        this.receiverAddress = InetAddress.getByName(receiverAddress);
        this.receiverPort = receiverPort;
    }

    public void send(String[] data) throws IOException {
        while (base < data.length) {
            while (nextSeqNum < base + WINDOW_SIZE && nextSeqNum < data.length) {
                sendFrame(data[nextSeqNum], nextSeqNum);
                if (base == nextSeqNum) {
                    startTimer();
                }
                nextSeqNum++;
            }

            DatagramPacket ackPacket = receiveAck();
            if (ackPacket != null) {
                int ack = Integer.parseInt(new String(ackPacket.getData(), 0, ackPacket.getLength()).trim());
                System.out.println("Received ACK for frame: " + ack);
                base = ack + 1;
                if (base == nextSeqNum) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        }
        socket.close();
    }

    private void sendFrame(String data, int seqNum) throws IOException {
        String frame = seqNum + ":" + data;
        byte[] buf = frame.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, receiverAddress, receiverPort);
        socket.send(packet);
        System.out.println("Sent frame: " + frame);
    }

    private DatagramPacket receiveAck() {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.setSoTimeout(TIMEOUT);
            socket.receive(packet);
            return packet;
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout, resending frames from: " + base);
            resendFrames();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timeout, resending frames from: " + base);
                resendFrames();
            }
        }, TIMEOUT);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void resendFrames() {
        for (int i = base; i < nextSeqNum; i++) {
            try {
                sendFrame("Resending: " + i, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        startTimer();
    }

    public static void main(String[] args) throws IOException {
        GoBackNSender sender = new GoBackNSender("localhost", 9876);
        String[] data = {"Frame1", "Frame2", "Frame3", "Frame4", "Frame5", "Frame6", "Frame7", "Frame8"};
        sender.send(data);
    }
}
