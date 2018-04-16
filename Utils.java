import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public final class Utils {

    public static final String EXAMPLES_FILE = "ExamplesII_Test.csv";

    public static String getKin(String line) {

        String kin;

        kin = line.substring(0, line.indexOf(','));
        kin = kin.toLowerCase();

        return kin;

    }//end getKin

    public static String getExample(String line) {

        String example;

        example = line.substring(line.indexOf(',') + 1, line.length());
        example = example.replaceAll("<p>", "");
        example = example.replaceAll("</p>", "");
        example = example.replaceAll(",,,", "");
        example = example.toLowerCase();

        return example;

    }//end getExample

    public static String getTest(String line) {

        String test;

        test = line.substring(line.indexOf(',') + 1, line.length());
        test = test.replaceAll("<p>", "");
        test = test.replaceAll("</p>", "");
        test = test.replaceAll(",,,", "");
        test = test.toLowerCase();

        return test;

    }//end getTest

    public static String[] getWords(String example){

        String[] words;
        String tempExample;

        words = null;

        example     = example.toLowerCase();
        tempExample = example.replaceAll("<p>", "");
        tempExample = tempExample.replaceAll("</p>", "");
        words       = tempExample.split("\\W+");

        return words;

    }//end getWords

    public static void ignoreFirstLine(BufferedReader br) throws IOException, FileNotFoundException{

        br.readLine();

    }//end ignoreFirstLine

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


