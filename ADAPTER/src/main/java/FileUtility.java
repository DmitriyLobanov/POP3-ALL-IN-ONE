import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtility {
    private ArrayList<String> readTestSequence = new ArrayList<>();
    private String testSequenceFromFile = null;
    private String fileName;
    private ArrayList<String> commandSequence;
    private ArrayList<String> inputDataToServerSequence;
    private Pattern patternCommand = Pattern.compile("\\b\\D[A-Z,^E]{3,}", Pattern.MULTILINE);
    private Pattern reactionPattern = Pattern.compile("_\\w{0,150}");
    private Matcher matcher;
    private File file;
    private StringBuilder stringBuilder = null;
    private String testSequenceAsString;
    private List<String> outConverted = new ArrayList<>();//ERR - OKK - > asdasd - tolik
    List<String> testReactions = new ArrayList<>();


   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileUtility that = (FileUtility) o;
        return Objects.equals(lineFromFile, that.lineFromFile) && Objects.equals(lineFromServer, that.lineFromServer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineFromFile, lineFromServer);
    }*/

    public String getTestSequenceFromFile() {
        return testSequenceFromFile;
    }

    public ArrayList<String> getCommandSequence() {
        return commandSequence;
    }

    public ArrayList<String> getInputDataToServerSequence() {
        return inputDataToServerSequence;
    }

    public FileUtility(String fileName) {

        this.fileName = fileName;
        commandSequence = new ArrayList<>(10);
        inputDataToServerSequence = new ArrayList<>(10);
        try {
            parseFile();
            responseCommands();
            responseUserInput();
            responceTestReaction();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error to read file");
        }
    }

    public List<String> initHashMap(List<String> commands) {
        HashMap<String, String> hashMapOK = new HashMap<>();
        HashMap<String, String> hashMapERR = new HashMap<>();

        hashMapERR.put("USER", "ErrorUser");
        hashMapERR.put("PASS", "ErrorPass");
        hashMapERR.put("LIST", "-100");
        hashMapERR.put("RETR", "-1");
        hashMapERR.put("TOP", "-10 0");

        hashMapOK.put("QUIT", "");
        hashMapOK.put("USER", "tolik");
        hashMapOK.put("PASS", "12345");
        hashMapOK.put("STAT", "");
        hashMapOK.put("NOOP", " ");
        hashMapOK.put("LIST", " ");
        hashMapOK.put("RETR", "1");
        hashMapOK.put("TOP", "1 50");


        for (int i = 0; i < inputDataToServerSequence.size(); ++i) {
            String s = inputDataToServerSequence.get(i);

            if (s.contains("OK")) {
                inputDataToServerSequence.set(i, hashMapOK.get(commands.get(i))).trim();
                outConverted.add("OK");
                continue;
            }
            if (s.contains("ERR")) {
                inputDataToServerSequence.set(i, hashMapERR.get(commands.get(i))).trim();
                outConverted.add("ERR");
                continue;
            }

            outConverted.add("");
        }
        return outConverted;

    }

    private void parseFile() throws Exception {
        file = new File(fileName);
        Scanner scanner = new Scanner(file);
        stringBuilder = new StringBuilder();
        String lineFromFile;
        while (scanner.hasNextLine()) {
            lineFromFile = scanner.nextLine();
            readTestSequence.add(lineFromFile);
            stringBuilder.append(lineFromFile);
            stringBuilder.append("\n");
        }
        //testSequenceFromFile = scanner.nextLine();
        testSequenceAsString = stringBuilder.toString();
        stringBuilder = null;
    }

    //+OK -ERR
    private void responceTestReaction() {
        List<String> temp = List.of(testSequenceAsString.split("\\s"));
        String str;
        for (int i = 0; i < temp.size(); ++i) {
            str = temp.get(i);
            if (!str.contains("RES")) {
                str = str.split("\\w*\\/")[1];
                testReactions.add(str);
            } else {
                testReactions.add("");
            }
        }
    }

    private void responseCommands() {
        for (String str : readTestSequence) {
            matcher = patternCommand.matcher(str);
            while (matcher.find()) {
                String S = matcher.group(0);
                commandSequence.add(matcher.group(0).trim());
            }
        }
    }

    private void responseUserInput() {
        for (String str : readTestSequence) {
            matcher = reactionPattern.matcher(str);
            while (matcher.find()) {
                String s = matcher.group(0);
                if (s.contains("_")) {
                    s = s.replace("_", " ").trim();
                }
                inputDataToServerSequence.add(s);
                //inputDataToServerSequence.add(matcher.group(0).substring(1));
            }
        }
    }

    public void printCommands() {
        for (String o : commandSequence) {
            System.out.println(o);
        }
    }

    public void printResponse() {
        for (String o : inputDataToServerSequence) {
            System.out.println(o);
        }
    }

    public void saveResponseLocal(String s) {
        file = new File("fromServer.txt");
        PrintWriter pr = null;
        try {
            pr = new PrintWriter(file);
            pr.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pr != null) {
                pr.close();
            }
        }
    }

    public void printInputSeq() {
        for (int i = 0; i < commandSequence.size(); ++i) {
            System.out.println(commandSequence.get(i) + " " + inputDataToServerSequence.get(i));
        }
    }

    public void compareInputAndOutputSeq(String serverAnswer) {
        int newLine = 0;
        String aux = testSequenceAsString.replace("RESET_", "");
        String aux2 = serverAnswer;
        System.out.println(aux);
        System.out.println(aux2);

        List<String> auxSplited1 = new ArrayList<>(List.of(aux.split("\n")));
        List<String> auxSplited2 = new ArrayList<>(List.of(aux2.split("\n")));
        //List <Integer> indexes = new ArrayList<>();
        List<List<Integer>> indexes = new ArrayList<>();
        //проверить длины
        for (int i = 0; i < auxSplited1.size(); ++i) {
            //
            List<String> auxSplitted1Line = List.of(auxSplited1.get(i).split("\\s"));
            List<String> auxSplitted2Line = List.of(auxSplited2.get(i).split("\\s"));
            indexes.add(new ArrayList<>());

            for (int j = 0; j < auxSplitted1Line.size(); ++j) {
                if (!auxSplitted1Line.get(j).equals(auxSplitted2Line.get(j))) {
                    indexes.get(i).add(j);
                }
            }
        }
        System.out.println(indexes);
    }
}
