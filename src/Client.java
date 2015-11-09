
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Client extends JFrame {
 
private JTextField userText;//the user text area
private JTextArea chatWindow;// the chat window area
private ObjectOutputStream output;//the output stream
private ObjectInputStream input;// the input stream
private String message = "";//starts out the text area with a blank message
private String serverIP;// ip address of the server (person you are talking to)
private Socket connection;//the main connection between the client and source
 
///*constructor*///
public Client(String host){
super("**Client Chat Window**");
serverIP = host;
userText = new JTextField();
userText.setEditable(false);
userText.addActionListener(
new ActionListener(){
public void actionPerformed(ActionEvent event){
sendMessage(event.getActionCommand());
userText.setText("");
}
}
);
add(userText, BorderLayout.SOUTH);
chatWindow = new JTextArea();//creates the chat window
add(new JScrollPane(chatWindow), BorderLayout.CENTER);//adds the chat window to the screen
setSize(450,600);//sets the size of the screen
setVisible(true);
}

///*Connect to server*///
public void startRunning(){
try{
connectToServer();//Connects to the server
setupStreams();//Sets up the input and output stream
whileChatting();//While the conversation is going on
}catch(EOFException EOFE){
showMessages("\n Client Terminated his Session");
}catch(IOException IOE){
IOE.printStackTrace();//
}finally{
closeEverything();//turns everything off and shuts down the client
}
}
///*Connect to server*///
private void connectToServer() throws IOException{
showMessages("Trying to make connection...\n");
connection = new Socket(InetAddress.getByName(serverIP),2000);//This sets up a connection with the host IP and the port '2000'
showMessages("Connected to: "+ connection.getInetAddress().getHostName());
}
///*set up streams to send and receive messages*///
private void setupStreams() throws IOException{
output = new ObjectOutputStream(connection.getOutputStream());//sets up the output stream (flows from client to server)
output.flush();
input = new ObjectInputStream(connection.getInputStream());//sets up the input stream
showMessages("\n Input and Output streams are now connected  \n");
}
///*While chat with server*///
private void whileChatting() throws IOException{
ableToType(true);
do{
try{
message = (String) input.readObject();
showMessages("\n" + message);//displays what the user types to the chat window
}catch(ClassNotFoundException CNFE){
showMessages("\n **" + connection.getInetAddress().getHostName() + "INVALID MESSAGE.TYPE A VALID MESSAGE** \n");
}
}while(!message.equals("Server:- END"));//if the server types 'END' then the program will turn off
}
///*turns off everything*///
private void closeEverything(){
showMessages("\n Shutting everything down......");
ableToType(false);
try{
output.close();
input.close();
connection.close();//turns off the main connection socket
}catch(IOException IOE){
IOE.printStackTrace();
}
}
///*send messages to server///
private void sendMessage(String message){
try{
output.writeObject("Client:- " + message);
output.flush();
showMessages("\nClient:- " + message);
}catch(IOException IOE){
chatWindow.append("\n ERROR: UNABLE TO SEND MESSAGE");//if something goes wrong when trying to send a message
}
}
///*updates GUI so that your message is displayed in the chat window*///
private void showMessages(final String m){
SwingUtilities.invokeLater(//allows you to make a thread that will update the GUI
new Runnable(){
public void run(){
chatWindow.append(m);//displays the message 
}
}
);
}
///*gives the user the ability to type*///
private void ableToType(final boolean tof){//(tof = true or false)
SwingUtilities.invokeLater(
new Runnable(){
public void run(){
userText.setEditable(tof);
}
}
);
}
}