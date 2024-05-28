import java.util.Scanner;

public class CRC {

    // Function that performs XOR operation
    public static void XOR(char[] checkValue, String genPoly) {
        for (int j = 1; j < genPoly.length(); j++) {
            checkValue[j] = (checkValue[j] == genPoly.charAt(j)) ? '0' : '1';
        }
    }

    // Function to check for errors on the receiver side
    public static void receiver(String data, String genPoly) {
        Scanner scanner = new Scanner(System.in);

        // Get the received data
        System.out.print("Enter the received data: ");
        String receivedData = scanner.next();
        System.out.println("\n-----------------------------");
        System.out.println("Data received: " + receivedData);

        // Cyclic Redundancy Check
        char[] checkValue = crc(data, genPoly);

        // Check if the remainder is zero to find the error
        for (int i = 0; i < genPoly.length() - 1; i++) {
            if (checkValue[i] != '0') {
                System.out.println("\nError detected\n");
                return;
            }
        }
        System.out.println("\nNo error detected\n");
    }

    // Function to perform CRC
    public static char[] crc(String data, String genPoly) {
        char[] checkValue = data.substring(0, genPoly.length()).toCharArray();
        int i = genPoly.length();
        int dataLength = data.length();

        while (i <= dataLength) {
            // Check if the first bit is 1 and call XOR function
            if (checkValue[0] == '1') {
                XOR(checkValue, genPoly);
            }

            // Move the bits by 1 position for the next computation
            if (i < dataLength) {
                char[] newCheckValue = new char[genPoly.length()];
                System.arraycopy(checkValue, 1, newCheckValue, 0, genPoly.length() - 1);
                newCheckValue[genPoly.length() - 1] = data.charAt(i);
                checkValue = newCheckValue;
            } else {
                char[] newCheckValue = new char[genPoly.length() - 1];
                System.arraycopy(checkValue, 1, newCheckValue, 0, genPoly.length() - 1);
                checkValue = newCheckValue;
            }
            i++;
        }

        return checkValue;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get the data to be transmitted
        System.out.print("Enter data to be transmitted: ");
        String data = scanner.next();

        // Get the generator polynomial
        System.out.print("Enter the Generating polynomial: ");
        String genPoly = scanner.next();

        // Find the length of data
        int dataLength = data.length();

        // Appending n-1 zeros to the data
        data += "0".repeat(genPoly.length() - 1);

        System.out.println("\n----------------------------------------");
        // Print the data with padded zeros
        System.out.println("Data padded with n-1 zeros: " + data);
        System.out.println("\n----------------------------------------");

        // Cyclic Redundancy Check
        char[] checkValue = crc(data, genPoly);

        // Print the computed check value
        System.out.println("CRC or Check value is: " + new String(checkValue));

        // Append data with checkValue(CRC)
        data = data.substring(0, dataLength) + new String(checkValue).substring(genPoly.length() - 1);

        System.out.println("\n----------------------------------------");
        // Print the final data to be sent
        System.out.println("Final data to be sent: " + data);
        System.out.println("\n----------------------------------------");

        // Call the receiver function to check errors
        receiver(data, genPoly);

        scanner.close();
    }
}
