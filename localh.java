import java.io.*;
import java.net.InetAddress;
public class jav {
    public static void main(String[] args) throws Exception
    {
        InetAddress addr = InetAddress.getLocalHost();
        InetAddress address2= InetAddress.getByName("www.stcet.ac.in");
        System.out.println("InetAddress of Named Host : "+ address2);
        System.out.println("Local HostAddress:  "
                           + addr.getHostAddress());
        System.out.println("Local host name: "
                           + addr.getHostName());
    }
}
