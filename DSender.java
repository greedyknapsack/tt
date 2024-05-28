import java.net.*;  
public class DSender
{  
    DatagramSocket ds; 
    InetAddress ip;  
    DSender()
    {
        try
        {
            ds = new DatagramSocket();
            ip = InetAddress.getByName("127.0.0.1");
        }
        catch(Exception e)
        {}
    } 

    public void send(String str)
    {
        try{
            DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(), ip, 3000);  
            ds.send(dp);
        }
        catch(Exception e)
        {}
        
    }
  public static void main(String[] args) throws Exception 
  {  
    
    String str = "Welcome java";  
    
     
      
    ds.close();  
  }  
}  