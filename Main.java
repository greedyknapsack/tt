import java.util.*;

class Frame {
    int id;
    boolean ack;

    public Frame(int id) {
        this.id = id;
        this.ack = false;
    }
}

abstract class ARQ {
    int windowSize;
    List<Frame> frames;

    public ARQ(int windowSize) {
        this.windowSize = windowSize;
        this.frames = new ArrayList<>();
        for (int i = 0; i < 100; i++) {  // 100 frames for example
            frames.add(new Frame(i));
        }
    }

    void sendFrame(Frame frame) {
        System.out.println("Sending frame " + frame.id);
        // Here you would actually send the frame
        // and start a timer
    }

    void receiveAck(Frame frame) {
        System.out.println("Received ACK for frame " + frame.id);
        frame.ack = true;
    }

    abstract void send();
}

class SelectiveRepeatARQ extends ARQ {
    public SelectiveRepeatARQ(int windowSize) {
        super(windowSize);
    }

    @Override
    void send() {
        for (int i = 0; i < windowSize; i++) {
            sendFrame(frames.get(i));
        }

        while (frames.stream().anyMatch(frame -> !frame.ack)) {
            for (int i = 0; i < windowSize; i++) {
                if (!frames.get(i).ack) {
                    sendFrame(frames.get(i));
                }
            }
        }
    }
}

class GoBackNARQ extends ARQ {
    public GoBackNARQ(int windowSize) {
        super(windowSize);
    }

    @Override
    void send() {
        for (int i = 0; i < windowSize; i++) {
            sendFrame(frames.get(i));
        }

        while (frames.stream().anyMatch(frame -> !frame.ack)) {
            if (!frames.get(0).ack) {
                for (int i = 0; i < windowSize; i++) {
                    sendFrame(frames.get(i));
                }
            }
        }
    }
}

// Usage
public class Main {
    public static void main(String[] args) {
        SelectiveRepeatARQ srArq = new SelectiveRepeatARQ(10);
        srArq.send();

        GoBackNARQ gbnArq = new GoBackNARQ(10);
        gbnArq.send();
    }
}