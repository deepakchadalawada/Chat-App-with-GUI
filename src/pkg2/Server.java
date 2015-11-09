package pkg2;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
 
public class Server extends JFrame{
 
private JTextField userText;//the area where users will type messages
private JTextArea chatWindow;//the area where the chat window will be
private ObjectOutputStream output;//so that users can see the chat window
private ObjectInputStream input;//so that users can type
private ServerSocket server;//the server so that the client can work
private Socket connection;//the connection of users
 
///*constructor*///
public Server(){
super("**Server Chat Window**");
userText = new JTextField();
userText.setEditable(false);
userText.addActionListener(//whenever the user types something in the text box and hits enter this will happen
new ActionListener(){
public void actionPerformed(ActionEvent event){
sendMessage(event.getActionCommand());
userText.setText("");//after they send it, we want the text field to empty
}
}
);
add(userText,BorderLayout.SOUTH);
chatWindow = new JTextArea();
add(new JScrollPane(chatWindow));
setSize(450,500);//sets the size of the screen
setVisible(true);
}
///*set up and run the server*///
public void startRunning(){
try{
server = new ServerSocket(2000, 100);//creates the server with the port '2000' and how many people can join it
while(true){
try{
waitForConnection();
setupStreams();//sets up the output and input streams
whileChatting();//allows users to send messages back and forth during chat
}catch(EOFException EOFE){
showMessage("\n **SERVER CONNECTION ENDED*******");//if the server goes down, this message will be shown
}finally{
closeEverything();
}
}
}catch(IOException IOE){
IOE.printStackTrace();
}
}
///*wait for connection, then display connection information*///
private void waitForConnection() throws IOException{
showMessage(" Waiting for a client to connect...\n");
connection = server.accept();
showMessage("Connected to "+connection.getInetAddress().getHostName());//returns the address with the ip-address of the user that connected
}
///*get stream to send and receive data*///
private void setupStreams()throws IOException{
output = new ObjectOutputStream(connection.getOutputStream());
output.flush();
input = new ObjectInputStream(connection.getInputStream());
showMessage("\n Streams are setup! \n");
}
///*during the chat conversation*///
private void whileChatting() throws IOException{
String message = " ***You are now connected! You can chat*** ";
sendMessage(message);
ableToType(true);
do{
try{
message=(String) input.readObject();
showMessage("\n"+message);
}catch(ClassNotFoundException CNFE){
showMessage("\n ***" + connection.getInetAddress().getHostName() + "INVALID MESSAGE. TYPE A VALID MESSAGES *** \n");//if the user tries to send something other than a string
}
}while(!message.equals("Client:- END"));//if the user types 'END' and sends it to you than the program will stop
}
///*turns everything off*///
private void closeEverything(){
showMessage("\n Now Closing connections...... \n");
ableToType(false);
try{
output.close();
input.close();
connection.close();//turns off the connection socket
}catch(IOException IOE){
IOE.printStackTrace();
}
}
///*send a message to the client*///
private void sendMessage(String message){
try{
output.writeObject("Server:- " + message);//sends a message through the output stream
output.flush();
showMessage("\nServer:- " + message);
}catch(IOException IOE){
chatWindow.append("\n **ERROR WHILE SENDING MESSAGE** \n");//if you cant send your message
}
}
///*updates chat window*///
private void showMessage(final String text){
SwingUtilities.invokeLater(//allows you to make a thread that will update the GUI
new Runnable(){
public void run(){
chatWindow.append(text);
}
}
);
}
///*sets the text field so that it is editable and the user is able to type in it*///
private void ableToType(final boolean tof){//(tof = 'true or false')
SwingUtilities.invokeLater(
new Runnable(){
public void run(){
userText.setEditable(tof);
}
}
);
}
}
