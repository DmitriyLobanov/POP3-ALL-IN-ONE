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
        FileUtility fileUtility = new FileUtility("C:\\Users\\guard\\Desktop\\demo\\JavaPOP3Client\\src\\toServerWp.txt");
        ArrayList<String> command = fileUtility.getCommandSequence();
        ArrayList<String> input = fileUtility.getInputDataToServerSequence();
        ArrayList<String> commandFromServer = new ArrayList<>();
        ArrayList<String> inputFromServer = new ArrayList<>();
        List<String> convertedListOutput = fileUtility.initHashMap(command);
        ArrayList<String> testSeqAsStrings = fileUtility.getReadTestSequence();
        String toServer;

        String fromServer =null;
        String commandToServer =null;
        String commandArg =null;
        StringBuilder stringBuilder = null;
        int index = 0;
        in = null;
        out = null;
        int k = 0;
        try {
            socket = new Socket("localhost", 8089);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//принять
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Введите сообщение с клиента");
            System.out.println(in.readLine());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

            /*System.out.println("Введите сообщение с клиента");
            System.out.println(in.readLine());
*/
            stringBuilder = new StringBuilder();
            int lineNumber = 0;
            for (int i = 0, j = 0; j < command.size(); ++i, ++j) {
                try {
                    if (command.get(j).contains("RES")) {
                        command.remove(j);
                        reset();
                        in.readLine();
                        stringBuilder.append("\n");
                        j--;
                        ++index;
                        ++lineNumber;
                        continue;
                    }

                    commandToServer = command.get(j);
                    commandArg = input.get(i);
                    toServer = commandToServer + " " + commandArg;
                    toServer = toServer.trim();
                    ++index;
                    out.println(toServer);

                    if ((fromServer = in.readLine()) == null) {
                        inputFromServer.add("-ERR/" + " ");
                        stringBuilder.append(commandToServer + "_" + convertedListOutput.get(i) + "/" + "-ERR" + " ");
                        continue;
                    }
                    //fromServer = in.readLine().trim();
                    System.out.println(fromServer.trim());
                    commandFromServer.add(commandToServer + " ");
                    List<String> arg = List.of(fromServer.split("\\s"));
                    inputFromServer.add(arg.get(0) + "/" + " ");
                    stringBuilder.append(commandToServer + "_" + convertedListOutput.get(i) + "/" + arg.get(0) + " ");

            } catch (IOException ex) {

          //  for (int k = 0; j < command.size(); ++k) {

                if (testSeqAsStrings.get(lineNumber).contains("QUIT") && (i < index) ) {
                    commandFromServer.add(commandToServer + " ");
                    //List <String> arg =  List.of(fromServer.split("\\s"));
                    inputFromServer.add("-ERR" + "/" + " ");
                    stringBuilder.append(commandToServer + "_" + convertedListOutput.get(i) + "/" + "-ERR" + " ");
                }
                k  = j;
                //index = 0;
               // break;
           // }
        }
       /* finally {
            fileUtility.saveResponseLocal(stringBuilder.toString());
            fileUtility.compareInputAndOutputSeq(stringBuilder.toString());
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }
        fileUtility.saveResponseLocal(stringBuilder.toString());
        fileUtility.compareInputAndOutputSeq(stringBuilder.toString());
    }
}
