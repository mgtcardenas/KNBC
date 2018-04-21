import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class Test {

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

            /*CHECK THAT IT IS READING THE TESTS CORRECTLY*/
            // System.out.println(test);

            setProbability(test);
            setPrediction();

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

            /*CHECK THAT IT IS READING THE TESTS CORRECTLY*/
            // System.out.println(test);

            setProbability(test);
            setPrediction();

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
        kins = Training.kinsTable.keys();

        while (kins.hasMoreElements()) {

            kin         = (String) kins.nextElement();
            numKinWords = 0;
            
            // Get the number of total words (even repeated), n for each kin. Here numKinWords
            kinWords = Training.wordsTables.get(Training.linksTable.get(kin)).keys();

            while (kinWords.hasMoreElements()) {
                word           = (String) kinWords.nextElement();
                // System.out.println("Word: "+ word + " in kin: " + kin + " is # times: " + Lesson.wordsTables.get(Lesson.linksTable.get(kin)).get(word));
                numKinWords   += Training.wordsTables.get(Training.linksTable.get(kin)).get(word);
            }//end while (words in kins)

            /*THIS IS FOR CHECKING THAT TOTAL NUMBER OF WORDS IN KIN IS CORRECT*/
            // System.out.println("Words in "+ kin + ": " + numKinWords);

            //Prepare first value of probability
            probability = Math.log(Training.kinsTable.get(kin)) - Math.log(Training.examples);

            /*CHECK THAT PROBABILITY IS FINE BEFORE CALCULATIONS*/
            // System.out.println("Probability of " + kin + ": " + probability);

            //Calculate probabilities of each word, therefore, get the words as an array
            testWords = Utils.getWords(test);

            /*CHECK THAT THE WORDS ARE CORRECT */
            // for (String w : testWords) {
            //     System.out.println(w);
            // }//end for

            for (String w : testWords) {
                if (Training.vocabulary.contains(w)) {

                    if (Training.wordsTables.get(Training.linksTable.get(kin)).containsKey(w)) {
                        probability +=  Math.log(Training.wordsTables.get(Training.linksTable.get(kin)).get(w) + 1) - Math.log( numKinWords + Training.vocabulary.size() );
                    } else {
                        probability += Math.log(1.0) - Math.log( numKinWords + Training.vocabulary.size() );
                    }//end if-else (¿Word is in kin's known words?)

                } else {
                    probability += Math.log(0.001) - Math.log( numKinWords + Training.vocabulary.size() );
                }//end if-else (¿Word is in the vocabulary?)
            }//end for

            /*CHECK THAT THE PROBABILITIES ARE NOT 0*/
            // System.out.println("Probability of " + kin + ": " + probability);
            probabilitiesTable.put(kin, probability);

        }//end while - (kins)
  
    }//end setProbability

    private static void setPrediction() {
        
        // Get the Biggest Probability
        Enumeration kins;
        String      kin, pred;
        double      biggest;

        kins        = probabilitiesTable.keys();
        kin         = "";
        pred        = "";
        biggest     = - Double.MAX_VALUE;

        while (kins.hasMoreElements()) {

            kin     = (String) kins.nextElement();

            /*CHECK THAT THE PROBABILITIES ARE NOT 0*/
            // System.out.println("Probability of " + kin + ": " + probabilitiesTable.get(kin));
            

            if (probabilitiesTable.get(kin) > biggest) {
                biggest = probabilitiesTable.get(kin);

                /*CHECK THAT THE BIGGEST PROBABILITY IS CORRECT */
                // System.out.println("The biggest now is: " + biggest);
                
                pred    = kin;
            }//end if (¿Is there a new champion?)

        }//end while

        prediction = pred;
        // System.out.println("My prediction is: " + prediction);
    }//end setPrediction

    public static double getSuccessRate(String filePath, int chunkSize, Round r) throws FileNotFoundException, IOException{
        
        FileReader      fr;
        BufferedReader  br;
        double          successRate;

        fr          = new FileReader(filePath);
        br          = new BufferedReader(fr);
        successRate = 0;

        Utils.ignoreFirstLine(br);

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

        return successRate;

    }//end getSuccessRate

}//end Test