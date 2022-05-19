import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientSession {
    static Socket socket;
    static PrintWriter out;
    static BufferedReader in;
    static BufferedReader reader;

    public static void reset() {
        try {
            socket.close();
            socket = new Socket("localhost", 8089);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//принять
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileUtility fileUtility = new FileUtility("C:\\Users\\guard\\Desktop\\demo\\JavaPOP3Client\\src\\toServerTransitionTour.txt");
        ArrayList<String> command = fileUtility.getCommandSequence();
        ArrayList<String> input = fileUtility.getInputDataToServerSequence();
        ArrayList<String> commandFromServer = new ArrayList<>();
        ArrayList<String> inputFromServer = new ArrayList<>();
        List<String> convertedListOutput = fileUtility.initHashMap(command);

        StringBuilder stringBuilder = null;
        try {
            socket = new Socket("localhost", 8089);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//принять
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Введите сообщение с клиента");
            System.out.println(in.readLine());
            String toServer;
            
            String fromServer;
            String commandToServer;
            String commandArg;
            stringBuilder = new StringBuilder();

            for (int i = 0, j = 0; j < command.size(); ++i, ++j) {
                if (command.get(j).contains("RES")) {
                    command.remove(j);
                    reset();
                    in.readLine();
                    stringBuilder.append("\n");
                    j--;
                    continue;
                }

                commandToServer = command.get(j);
                commandArg = input.get(i);
                toServer = commandToServer + " " + commandArg;
                toServer = toServer.trim();
                out.println(toServer);

                if ((fromServer = in.readLine()).trim() == null) {
                    inputFromServer.add("-ERR/" + " ");
                    stringBuilder.append(commandToServer + "_" + convertedListOutput.get(i) + "/" + "-ERR" + " ");
                    continue;
                }
                //fromServer = in.readLine().trim();
                System.out.println(fromServer);
                commandFromServer.add(commandToServer + " ");
                List <String> arg =  List.of(fromServer.split("\\s"));
                inputFromServer.add(arg.get(0) + "/" + " ");
                stringBuilder.append(commandToServer + "_" + convertedListOutput.get(i) + "/" + arg.get(0) + " ");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            fileUtility.saveResponseLocal(stringBuilder.toString());
            fileUtility.compareInputAndOutputSeq(stringBuilder.toString());
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
