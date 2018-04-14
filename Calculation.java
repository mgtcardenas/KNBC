import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class Calculation {

    static public String                    prediction;
    static Hashtable<String, Double> probabilitiesTable; // Probabilities Table -> The probability that Test is any kin

    private static double testLines(BufferedReader br, int numLines)throws FileNotFoundException, IOException {
        
        double successRate;       
        int    successes, attempts;
        String line, kin, test;

        successes = 0;
        attempts  = 0;

        for (int i = 0; i < numLines; i++) {
            
            line = br.readLine();
            kin  = Utils.getKin(line);
            test = Utils.getTest(line);

            setProbability(test);
            predict();

            if (kin.equals(prediction)) {
                successes++;
            }//end if

            attempts++;

        }//end for

        successRate = (double) successes / (double) attempts;

        return successRate;

    }//end testLines

    private static double testRestOfFile(BufferedReader br)throws FileNotFoundException, IOException {
        
        double successRate;       
        int    successes, attempts;
        String line, kin, test;

        successes = 0;
        attempts  = 0;
        line      = br.readLine();

        while (line != null) {
        
            kin  = Utils.getKin(line);
            test = Utils.getTest(line);

            setProbability(test);
            predict();

            if (kin.equals(prediction)) {
                successes++;
            }//end if

            attempts++;

            line = br.readLine();

        }//end while

        successRate = (double) successes / (double) attempts;

        return successRate;

    }//end testRestOfFile

    private static void setProbability(String test) {
        
        String          kin, word;
        String[]        testWords;
        Enumeration     kins, kinWords;
        double          probability;
        long            numKinWords;

        probabilitiesTable  = new Hashtable<>();
    
        // Cycle through all kins to compute each of their respective probabilities in respect to Test
        kins = Lesson.kinsTable.keys();

        while (kins.hasMoreElements()) {

            kin         = (String) kins.nextElement();
            numKinWords = 0;
            
            // Get the number of total words (even repeated), n for each kin. Here numKinWords
            kinWords = Lesson.wordsTables.get(Lesson.linksTable.get(kin)).keys();

            while (kinWords.hasMoreElements()) {
                word           = (String) kinWords.nextElement();
                // System.out.println("Word: "+ word + " in kin: " + kin + " is # times: " + Lesson.wordsTables.get(Lesson.linksTable.get(kin)).get(word));
                numKinWords   += Lesson.wordsTables.get(Lesson.linksTable.get(kin)).get(word);
            }//end while (words in kins)

            /*THIS IS FOR CHECKING THAT TOTAL NUMBER OF WORDS IN KIN IS CORRECT*/
            //System.out.println("Words in "+ kin + ": " + numKinWords);

            //Prepare first value of probability
            probability = (double) Lesson.kinsTable.get(kin)/ (double) Lesson.examples;

            //Calculate probabilities of each word, therefore, get the words as an array
            testWords = test.split(" ");
            
            for (String w : testWords) {
                if (Lesson.vocabulary.contains(w)) {

                    if (Lesson.wordsTables.get(Lesson.linksTable.get(kin)).containsKey(w)) {
                        probability *=  (double) (Lesson.wordsTables.get(Lesson.linksTable.get(kin)).get(w) + 1) / (double) ( numKinWords + Lesson.vocabulary.size() );
                    } else {
                        probability *= (double) 1.0 / (double) ( numKinWords + Lesson.vocabulary.size() );
                    }//end if-else (¿Word is in kin's known words?)

                } else {
                    probability *= (double) 0.001 / (double) ( numKinWords + Lesson.vocabulary.size() );
                }//end if-else (¿Word is in the vocabulary?)
            }//end for

            probabilitiesTable.put(kin, probability);

        }//end while - (kins)
  
    }//end setProbability

    private static void predict() {
        
        // Get the Biggest Probability
        Enumeration kins;
        String      kin, pred;
        double      biggest;

        kins        = probabilitiesTable.keys();
        kin         = "";
        pred        = "";
        biggest     = 0;

        while (kins.hasMoreElements()) {

            kin     = (String) kins.nextElement();

            if (probabilitiesTable.get(kin) > biggest) {
                biggest = probabilitiesTable.get(kin);
                pred    = kin;
            }//end if (¿Is there a new champion?)

        }//end while

        prediction = pred;
    }//end predict

    public static double getSuccessRate(String filePath, int chunkSize, Round r) throws FileNotFoundException, IOException{
        
        FileReader      fr;
        BufferedReader  br;
        double          successRate;

        fr          = new FileReader(filePath);
        br          = new BufferedReader(fr);
        successRate = 0;

        switch (r) {
            case FIRST:
                
                successRate = testLines(br, chunkSize);
                br.close();

                break;

            case SECOND:
                
                Utils.ignoreLines(br, chunkSize);
                successRate = testLines(br, chunkSize);
                br.close();

                break;

            case THIRD:
                
                Utils.ignoreLines(br, chunkSize*2);
                successRate = testLines(br, chunkSize);
                br.close();

                break;

            case FOURTH:
                
                Utils.ignoreLines(br, chunkSize*3);
                successRate = testRestOfFile(br);
                br.close();

                break;
        
            
        }//end switch

        /* Everything below this will be part of the testLines methods.
           One of them will use a for loop and will receive a number of lines to be tested.
           The other will use a while loop for testing till the end of file.
           getSuccessRate must read the file, since it must skip parts of it, depending the case.
           */

        return successRate;

    }//end getSuccessRate

}//end Calculation