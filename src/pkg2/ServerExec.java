package pkg2;

import javax.swing.JFrame;

public class ServerExec {
public static void main(String[] args){
Server server = new Server();
server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
server.startRunning();
}
}
