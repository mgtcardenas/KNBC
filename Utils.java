import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public final class Utils {

    public static final String EXAMPLES_FILE = "Examples.csv";

    public static String getKin(String line) {

        String kin;

        kin = line.substring(0, line.indexOf(','));
        kin = kin.toLowerCase();

        return kin;

    }//end getKin

    public static String getExample(String line) {

        String example;

        example = line.substring(line.indexOf(',') + 1, line.length() - 3);
        example = example.toLowerCase();

        return example;

    }//end getExample

    public static String getTest(String line) {

        String test;

        test = line.substring(line.indexOf(',') + 1, line.length() - 3);
        test = test.toLowerCase();

        return test;

    }//end getTest

    public static String[] getWords(String example) {

        String[] words;

        words = example.split(" ");

        return words;

    }//end getWords

    public static int countNumLines() throws IOException, FileNotFoundException{
        
        FileReader      fr;
        BufferedReader  br;

        int             numLines;

        fr       = new FileReader(EXAMPLES_FILE);
        br       = new BufferedReader(fr);

        numLines = 0;

        while (br.readLine() != null) {
            numLines++;
        }//end while

        br.close();

        return numLines;

    }//end countNumLines

    public static void ignoreLines(BufferedReader br, int numLines) throws IOException, FileNotFoundException {
        
        for (int i = 0; i < numLines; i++) {
            br.readLine();
        }//end for

    }//end ignoreLines

    private void Utils() {
    }//end Utils
    
}


