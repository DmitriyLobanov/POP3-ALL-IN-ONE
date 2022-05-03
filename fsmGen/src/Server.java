import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket  = new ServerSocket(8089);
            int id = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                //Thread clientThread = new Thread(new ClientService(clientSocket, ++id));
                Thread clientThread = new Thread(new ClientService(clientSocket, ++id));
                System.out.println("registered new client " + id);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
