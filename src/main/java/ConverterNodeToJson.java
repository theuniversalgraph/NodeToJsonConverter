import com.google.gson.Gson;

import java.io.*;
import java.util.Hashtable;

public class ConverterNodeToJson {

    final public static String FILE_ND_POSTFIX = ".nd";
    final public static String FILE_JSON_POSTFIX = ".json";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 1) {
            System.out.println("Please provide the folder destination. Folder must contain *.nd files.");
            return;
        }

        final String path = args[0];

        final File file = new File(path);
        if (file.exists()) {
            final File[] files = file.listFiles((dir, name) -> name.toLowerCase().endsWith(FILE_ND_POSTFIX));

            for (File ndFile : files) {
                final Hashtable hashtable = ConverterNodeToJson.inputNodesFromFile(ndFile.getAbsolutePath());

                final String nameJsonPostfix = ndFile.getName().replaceAll(FILE_ND_POSTFIX, FILE_JSON_POSTFIX);

                final File json = new File(path + nameJsonPostfix);
                if (!json.exists()) {
                    boolean wasFileCreated = json.createNewFile();
                    if (!wasFileCreated) throw new IllegalStateException("Unable to create file. Check permissions.");
                }

                try (FileOutputStream fo = new FileOutputStream(json, false)) {
                    ConverterNodeToJson.exportFileToJson(fo, hashtable);
                }
            }
        } else {
            System.out.println(file.getAbsolutePath() + "Folder does not exist. Try another one.");
        }
    }

    private static void exportFileToJson(final OutputStream out, final Hashtable saved_nodes) throws IOException {
        final Gson gson = new Gson();
        final String json = gson.toJson(saved_nodes);
        try (OutputStreamWriter outputStreamWriterWriter = new OutputStreamWriter(out)) {
            outputStreamWriterWriter.append(json);
            outputStreamWriterWriter.flush();
        }
    }

    //method to read *.nd file
    private static Hashtable inputNodesFromFile(final String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (Hashtable) objectInputStream.readObject();
        }
    }

}
