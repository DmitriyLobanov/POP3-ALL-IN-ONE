import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
        // ArrayList<String> outFromServer = new ArrayList<>(10);
        ArrayList<String> commandFromServer = new ArrayList<>();
        ArrayList<String> inputFromServer = new ArrayList<>();
        List<String> convertedListOutput = fileUtility.initHashMap(command);

        ; //чтобы из tolik -> собрать в овтеты сервера USER_OK/+OK PASS_OK/+OK

        try {
            socket = new Socket("localhost", 8089);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//принять
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Введите сообщение с клиента");
            System.out.println(in.readLine());

            String line;
            String toServer = null;
            String s;
            String fromServer;
            String commandToServer;
            String commandArg;
            String serverResponce;
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0, j = 0; j < command.size(); ++i, ++j) {
                s = command.get(j);
                if (command.get(j).contains("RES")) {
                    command.remove(j);
                    reset();
                    //command.set(j,"");
                    s = in.readLine(); // responce +ok SERVER ready
                    stringBuilder.append("\n");
                    j--;
                    continue;
                }

                commandToServer = command.get(j);
                commandArg = input.get(i);

                //toServer = command.get(j) + " " + input.get(i);
                toServer = commandToServer + " " + commandArg;
                toServer = toServer.trim();
                out.println(toServer);

                fromServer = in.readLine().trim();
                System.out.println(fromServer);

                if (commandArg.contains(" ") ) {
                    input.set(i, commandArg.replace(" ", "_"));
                }

               // commandFromServer.add(command.get(j) + " ");
                commandFromServer.add(commandToServer + " ");
               // inputFromServer.add(input.get(i) + "/" + " ");
                //разбить по пробелу и брать 1 только ок и ерр

                List <String> arg =  List.of(fromServer.split("\\s"));
                inputFromServer.add(arg.get(0) + "/" + " ");
             //   inputFromServer.add(commandArg + "/" + " ");
               // stringBuilder.append(command.get(j) + "_" + convertedListOutput.get(i) + "/" + line + " ");
                stringBuilder.append(commandToServer + "_" + convertedListOutput.get(i) + "/" + arg.get(0) + " ");
            }

            fileUtility.saveResponseLocal(stringBuilder.toString());
            fileUtility.compareInputAndOutputSeq(stringBuilder.toString());
            socket.close();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }
}



