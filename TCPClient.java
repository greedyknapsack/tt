import java.net.*;

import java.io.*;

class TCPClient

{

public static void main(String args[])throws Exception

{

String sentence;

String modifiedSentence;

System.out.println("Enter the sentence:");

BufferedReader inFromUser=new BufferedReader(new InputStreamReader(System.in));

Socket clientSocket=new Socket("localhost",2345);

DataOutputStream outToServer=new DataOutputStream(clientSocket.getOutputStream());

BufferedReader inFromServer=new BufferedReader(new

InputStreamReader(clientSocket.getInputStream()));

sentence=inFromUser.readLine();

outToServer.writeBytes(sentence+'\n');

modifiedSentence=inFromServer.readLine();

System.out.println("From Server: "+modifiedSentence);

clientSocket.close();

}
}
/*Output:

First Open a command prompt window & run the server program in the following manner:

Prompt>java TCPServer and the server will run endlessly.

Then open another command prompt window & run the client program in the following manner:

Prompt> java TCPEchoClient & press enter in the keyboard. Then a prompt appears like the

following:

Enter the sentence:

If you write “hello” & press enter in the keyboard, the output will be:

From Server: HELLO*/
