import java.io.IOException;
import java.net.*;

public class Server extends Thread{
    private DatagramSocket serverSocket;
    public String serverIP;
    public int serverPort;
    public String peerIP;
    public int peerPort;
    private GamePanel gamePanel;

    public Server(GamePanel gp){
        this.gamePanel = gp;
        try {
            serverSocket = new DatagramSocket(0);
            serverIP = InetAddress.getLocalHost().getHostAddress();
            serverPort = serverSocket.getLocalPort();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        start();
    }

    @Override
    public void run(){
        System.out.println("started server");
        byte[] action = new byte[1];
        while (true){
            DatagramPacket receivePacket = new DatagramPacket(action, action.length);
            try {
                serverSocket.receive(receivePacket);

                if (action[0] == ActionTypes.CONNECT){
                    peerIP = receivePacket.getAddress().getHostAddress();
                    peerPort = receivePacket.getPort();
                }
                else
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

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void sendAction(byte i) {
        byte[] data = {i};
        try {
            serverSocket.send(new DatagramPacket(data, data.length, InetAddress.getByName(peerIP), peerPort));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getPeerIP() {
        return peerIP;
    }
}
