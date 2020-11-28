import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.BigInteger;

public class SecureChatClient extends JFrame implements Runnable, ActionListener {
    public static final int PORT = 8765;
    ObjectInputStream myReader;
    ObjectOutputStream myWriter;
    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName;
    Socket connection;
    SymCipher cipher;

    public SecureChatClient ()
    {
        try {

        myName = JOptionPane.showInputDialog(this, "Enter your user name: ");
        serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");
        InetAddress addr =
                InetAddress.getByName(serverName);
        //
        // Connect to server with new socket
        //
        connection = new Socket(addr, PORT);
        //
        // Get Reader
        // 
		myReader = new ObjectInputStream(connection.getInputStream());
        //
        // Get Writer
        // 
        myWriter = new ObjectOutputStream(connection.getOutputStream());
        myWriter.flush();
        //
        // Initialize server's public key(E) and server's public mod value(N)
        //
        BigInteger E = (BigInteger)myReader.readObject();
        BigInteger N = (BigInteger)myReader.readObject();
                                //
                                // FOR GRADER
                                // 
                                System.out.println("E: " + E.toString()+ "\nB: " + N.toString());
        //
        // Determine cipher preferenece ( either add or sub )
        //
        String cipherType = myReader.readObject().toString();
                                //
                                // FOR GRADER
                                // 
                                System.out.println(cipherType);
        if (cipherType.equalsIgnoreCase("Add")){
            cipher = new Add128();
        } else if (cipherType.equalsIgnoreCase("Sub")){
            cipher = new Substitute();
        }
        //  
        // Send encrypted key to Server (server already contains the cipher)
        //
        BigInteger key = new BigInteger(1, cipher.getKey());
        key = key.modPow(E,N);
                                //
                                // FOR GRADER
                                //          
                                System.out.println("client symmetric key: " + key);
        myWriter.writeObject(key);
        myWriter.flush();
        //  
        // Send (encrypted) name to Server.  Server will need
        // this to announce sign-on and sign-off of clients
        //
        myWriter.writeObject(cipher.encode(myName));
        myWriter.flush();
        //
        // At this point "handshaking is complete" / client begins normal 
        // 
        // Set title to identify chatter
        //
        this.setTitle(myName);      
        //
        // Set up graphical environment for the user
        // 
        Box b = Box.createHorizontalBox();
        outputArea = new JTextArea(8, 30);
        outputArea.setEditable(false);
        b.add(new JScrollPane(outputArea));

        outputArea.append("Welcome to the Chat Group, " + myName + "\n");
        //
        // This is where user will type input
        // 
        inputField = new JTextField("");
        inputField.addActionListener(this);

        prompt = new JLabel("Type your messages below:");
        Container c = getContentPane();

        c.add(b, BorderLayout.NORTH);
        c.add(prompt, BorderLayout.CENTER);
        c.add(inputField, BorderLayout.SOUTH);
        // 
        // thread is to receive strings from Server
        //
        Thread outputThread = new Thread(this);
        outputThread.start();

	    addWindowListener(
            new WindowAdapter(){
                public void windowClosing(WindowEvent e){ 
                    //
                    // write encryted msg to server
                    // 
                    try{
                        myWriter.writeObject(cipher.encode("CLIENT CLOSING"));
                        myWriter.flush();
                    } catch (IOException e2){
                        System.out.println("Issue sending message to server");
                    }
                    System.exit(0);
                }
            }
        );

        setSize(500, 200);
        setVisible(true);

        }
        catch (Exception e)
        {
            System.out.println("Problem starting client!");
        }
    }

    public void run(){
        while (true){
            try {
                //
                // read encrypted msg (byte array)
                //
                byte [] encBytes = (byte[])myReader.readObject();
                //
                // decrypt msg
                //
                String curMsg = cipher.decode(encBytes);
                                    //
                                    // FOR GRADER
                                    //
                                    System.out.println("The array of bytes received: " + encBytes);
                                    System.out.println("The decrypted array of bytes: " + curMsg.getBytes());
                                    System.out.println("The corresponding String: " + curMsg);
                //
                // output msg
                //
			    outputArea.append(curMsg+"\n");
            }
            catch (Exception e){
                System.out.println(e +  ", closing client!");
                break;
            }
        }
        System.exit(0);
    }

    public void actionPerformed(ActionEvent event){
        //
        // Get input msg
        //
        String currMsg = event.getActionCommand();
        inputField.setText("");
        //
        // encryt msg
        //
        byte[] encBytes = cipher.encode(myName.concat(":").concat(currMsg));
                                    //
                                    // FOR GRADER
                                    //
                                    System.out.println("The original String message: " + currMsg);
                                    System.out.println("The corresponding array of bytes: " + currMsg.getBytes());
                                    System.out.println("The encrypted array of bytes: " + encBytes);
        //
        // Send msg to server 
        //
        try{
            myWriter.writeObject(encBytes);  
            myWriter.flush();
        } catch (IOException e){
            System.out.println("Issue sending your message!");
        }
    }

    public static void main(String [] args){
        SecureChatClient JR = new SecureChatClient();
        JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}
