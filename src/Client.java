import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client extends Thread{
    private String serverIP;
    private int serverPort;
    private DatagramSocket socket;
    private GamePanel gamePanel;

    public Client(GamePanel gamePanel, String serverIP, int serverPort){
        this.gamePanel = gamePanel;
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        try {
            socket = new DatagramSocket(0);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        start();
    }
    @Override
    public void run() {
        System.out.println("started client server");
        sendAction(ActionTypes.CONNECT);
        byte[] action = new byte[1];
        while (true){
            // listening for actions from server
            DatagramPacket receivePacket = new DatagramPacket(action, action.length);
            try {
                socket.receive(receivePacket);
                gamePanel.handleMultiplayer(action[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    public void sendAction(byte i) {
        byte[] data = {i};
        try {
            socket.send(new DatagramPacket(data, data.length, InetAddress.getByName(serverIP), serverPort));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
