import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientService implements Runnable {
    private ClientServiceContext _fsm;

    private PrintWriter out;

    private String fromClient;

    private String regexCommand;

    private Map<String, String> users;

    private List<Integer> toDelete;

    private int clientId;

    private List<Integer> actualMessages;

    private String userName;


    private Matcher matcher;

    private Pattern pattern;

    private String regexArg;

    private BufferedReader in;

    private boolean running;

    private Socket clientSocket;

    private List<Integer> localMessages;

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    private String arg;
    private String command;

    public ClientService() {
        _fsm = new ClientServiceContext(this);
    }

    public ClientServiceContext getContext() {
        return _fsm;
    }

    public ClientService(Socket clientSocket, int clientId)  throws IOException {
        this.clientSocket = clientSocket;
        this.clientId =clientId;
        running  = true;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(),true);
        users = new HashMap<>();
        users.put("Dima","1234");
        users.put("Maksim","asd");
        users.put("Andrew","RFF");
        users.put("tolik", "12345");
        users.put("1","1");
        actualMessages = new ArrayList<>(Arrays.asList(134, 24,234,123,12));
        toDelete = new ArrayList<>();
        localMessages = new ArrayList<>(actualMessages);
        regexCommand = "^[A-Z]{3,4}";
        regexArg = "\\s.{0,40}";
        _fsm = new ClientServiceContext(this);
    }

    @Override
    public void run() {
        try {
            Pair<String, String>  commandAndArg;
            out.println("+OK server ready");
            while (running) {
                fromClient = in.readLine();
                if (fromClient == null) {
                    running = false;
                    break;
                }
                commandAndArg = parseCommandAndArgument(fromClient);
                command = commandAndArg.getFirst();
                arg = commandAndArg.getSecond();

                switch (command) {
                    //doUser - просто user
                    case "USER":User(arg); break;
                    case "PASS":Pass(arg); break;
                    case "LIST":List(arg); break;
                    case "STAT":Stat(); break;
                    case "NOOP":Noop(); break;
                    case "DELE":Delete(arg); break;
                    case "RETR":Retr(arg); break;
                    case "RSET":Rset(); break;
                    case "QUIT":Quit(); break;
                    default: out.println("-ERR wrong command " + command + " " + arg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAcceptablePassword(String pass) {
        String tmp = users.get(userName);
        return pass.equals(tmp);
    }

    public Pair<String, String> parseCommandAndArgument(String comAndArg) {
        Pair<String, String> pair = new Pair<>();
        pattern = Pattern.compile(regexCommand);
        matcher = pattern.matcher(comAndArg);
        if(matcher.find()) {
            pair.setFirst(matcher.group(0).trim());
        } else {
            pair.setFirst("");
            System.err.println("Eror to parse Commad");
        }
        pattern = Pattern.compile(regexArg);
        matcher = pattern.matcher(comAndArg);
        if(matcher.find()) {
            pair.setSecond(matcher.group(0).trim());
        } else {
            pair.setSecond("");
            System.err.println("Eror to parse arg");
        }
        return pair;
    }

    public boolean isUserNameExist(String username) {
        return users.containsKey(username);
    }

    public int doUser(String userName) {
        if (isUserNameExist(userName)) {
            out.println("+OK");
           // currentState = States.NOT_AUTHORIZED;
            this.userName = userName;
            return  1;
        }
        else  {
            out.println("-ERR");
            return 0;
        }
    }

    public int doPass(String pass) {
        if ( isAcceptablePassword(pass)) {
            out.println("+OK");
            return  1;
        } else {
            out.println("-ERR");
            return 0;
        }
    }

    public void doNoop() {
        out.println("+OK");
    }

    public void doRetr(String arg) {
        int tnp = Integer.parseInt(arg);
        if ( !(tnp < 0 || tnp >= actualMessages.size())) {
            out.println("+OK " + actualMessages.get(tnp) + " octets");
        } else {
            out.println("-ERR");
        }
    }

    public void doList(String arg) {
        if (!arg.equals("") ) {
            int messageNumber = Integer.parseInt(arg);
            if (!(messageNumber < 0 || messageNumber >= actualMessages.size()) && actualMessages.get(messageNumber)!=-1 ) {
                out.println("+OK " + actualMessages.get(messageNumber) + " octets");
            }
            else {
                out.println("-ERR");
            }
            //} else if ((currentState == State.AUTHENTICATED && msg.equals(""))) {
        } else  {

            int octets = 0;
            for (int i = 0; i < actualMessages.size();++i) {
                octets += actualMessages.get(i);
            }
            out.println("+OK " + actualMessages.size() + " messages (" + octets +")");
        }

    }

    public void doRset() {
            out.println("+OK");
            toDelete.clear();
            actualMessages = localMessages;
    }

    public void doStat() {

            int lettersSize = actualMessages.size();
            int messagesCountOctets = 0;
            for (int i = 0; i < lettersSize;++i) {
                messagesCountOctets += actualMessages.get(i);
            }
            out.println("+OK " + lettersSize + " "+ messagesCountOctets  );

    }

    public void doDelete(String arg) {
        int tnp = Integer.parseInt(arg);
        if (!(tnp < 0 || tnp >= actualMessages.size()) ) {
            toDelete.add(tnp);
            actualMessages.set(tnp, -1);
            toDelete.add(tnp);
            out.println("+OK");
        } else {
            out.println("-ERR");
        }
    }

    public void doQuit() {
        out.println("+OK");
        for (int i =0; i < toDelete.size(); ++i) {
            localMessages.remove(toDelete.get(i));
        }
        actualMessages = localMessages;
        running = false;
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void List(String arg) {
        _fsm.List(arg);
    }

    public void ERR() {
        out.println("-ERR");
    }

    public void User(String arg) {
        _fsm.User(arg);
    }

    public void Pass(String pass) {
        _fsm.Pass(pass);
    }

    public void Noop() {
        _fsm.Noop();
    }

    public void Retr(String msg) {
        _fsm.Retr(msg);
    }

    public void Rset() {
        _fsm.Rset();
    }

    public void Stat() {
        _fsm.Stat();
    }

    public void Delete(String msg) {
        _fsm.Delete(msg);
    }

    public void Quit() {
        _fsm.Quit();
    }

}
