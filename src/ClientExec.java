
import javax.swing.JFrame;
public class ClientExec {
public static void main(String[] args){
Client client;
client = new Client("127.0.0.1");//local host ip
client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
client.startRunning();
}
}