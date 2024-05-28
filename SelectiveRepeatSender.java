/* 
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class SelectiveRepeatSender {
    private static final int WINDOW_SIZE = 4;
    private static final int TIMEOUT = 200;

    private DatagramSocket socket;
    private InetAddress receiverAddress;
    private int receiverPort;
    private int base = 0;
    private int nextSeqNum = 0;
    private String[] window;
    private boolean[] acked;
    private Timer[] timers;

    public SelectiveRepeatSender(String receiverAddress, int receiverPort) throws IOException {
        this.socket = new DatagramSocket();
        this.receiverAddress = InetAddress.getByName(receiverAddress);
        this.receiverPort = receiverPort;
        this.window = new String[WINDOW_SIZE];
        this.acked = new boolean[WINDOW_SIZE];
        this.timers = new Timer[WINDOW_SIZE];
    }

    public void send(String[] data) throws IOException {
        while (base < data.length) {
            while (nextSeqNum < base + WINDOW_SIZE && nextSeqNum < data.length) {
                sendFrame(data[nextSeqNum], nextSeqNum);
                window[nextSeqNum % WINDOW_SIZE] = data[nextSeqNum];
                acked[nextSeqNum % WINDOW_SIZE] = false;
                startTimer(nextSeqNum % WINDOW_SIZE);
                nextSeqNum++;
            }

            DatagramPacket ackPacket = receiveAck();
            if (ackPacket != null) {
                int ack = Integer.parseInt(new String(ackPacket.getData(), 0, ackPacket.getLength()).trim());
                System.out.println("Received ACK for frame: " + ack);
                if (ack >= base && ack < base + WINDOW_SIZE) {
                    acked[ack % WINDOW_SIZE] = true;
                    while (base < nextSeqNum && acked[base % WINDOW_SIZE]) {
                        stopTimer(base % WINDOW_SIZE);
                        base++;
                    }
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
            // Timeout - handled by individual timers
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startTimer(int index) {
        stopTimer(index);
        timers[index] = new Timer();
        timers[index].schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timeout, resending frame: " + (base + index));
                try {
                    sendFrame(window[index], base + index);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, TIMEOUT);
    }

    private void stopTimer(int index) {
        if (timers[index] != null) {
            timers[index].cancel();
            timers[index] = null;
        }
    }

    public static void main(String[] args) throws IOException {
        SelectiveRepeatSender sender = new SelectiveRepeatSender("localhost", 9876);
        String[] data = {"Frame1", "Frame2", "Frame3", "Frame4", "Frame5", "Frame6", "Frame7", "Frame8"};
        sender.send(data);
    }
}
*/


import java.io.*;
import java.net.*;
import java.util.Arrays;

public class SelectiveRepeatSender {
    private static final int FROM_PORT = 9870;
    private static final int TO_PORT = 9871;    

    public static void main(String[] args) throws Exception {
        InetAddress ip=InetAddress.getLocalHost();
        byte[] receiveData=new byte[1024];

        DatagramSocket ds=new DatagramSocket(FROM_PORT);

        int frames[]={1,2,3,4,5,6,7,8,9};
        boolean received[]=new boolean[frames.length];
        Arrays.fill(received,false);
        int window=4;
        int frames_sent=0;

        String str=(frames.length+"");
        ds.send(new DatagramPacket(str.getBytes(),str.length(),ip,TO_PORT));

        str=(window+"");
        ds.send(new DatagramPacket(str.getBytes(),str.length(),ip,TO_PORT));
        
        ds.setSoTimeout(5000);

        int l=0,r=window-1;
        while(frames_sent<frames.length)
        {
            boolean flag=true;
            for(int i=l;i<=r;i++)
            {
                if(received[i])
                    continue;
                flag=false;
                str=(frames[i]+"");
                ds.send(new DatagramPacket(str.getBytes(),str.length(),ip,TO_PORT));
                System.out.println("Sent frame: "+frames[i]);
            }
            if(flag)
            {
                System.out.println("All frames in window"+l+","+r+" sent");
                l=r+1;
                r=Math.min(l+window-1,frames.length-1);
                continue;
            }
            while(true){
                try
                {
                    ds.receive(new DatagramPacket(receiveData, receiveData.length));
                    int ack=Integer.parseInt(new String(receiveData).trim());
                    //System.out.println("Received ACK for frame: "+ack);
                    if(!received[ack])
                    {
                        received[ack]=true;
                        System.out.println("Received ACK for frame: "+ack);
                        frames_sent++;
                    }
                    
                }
                catch(SocketTimeoutException e)
                {
                    System.out.println("Timeout, resending frames: "+l+" to "+r);
                    break;
                }
            }
        }
        ds.close();
    }
}


