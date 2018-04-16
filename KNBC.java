import java.io.FileNotFoundException;
import java.io.IOException;

public class KNBC {

    public static void main(String[] args) throws IOException, FileNotFoundException{

        int     chunkSize, numLines;
        double  successRate, accumulatedSuccessRate;

        numLines               = Utils.countNumLines();
        chunkSize              = numLines/4;
        accumulatedSuccessRate = 0;

        for (Round round : Round.values()) {
            
            Lesson.fillTables(Utils.EXAMPLES_FILE, chunkSize, round);
            Lesson.count();

            /*CHECK SECTION*/
            // System.out.println(Lesson.wordsTables.get(0).get("sentence"));
            // System.out.println(Lesson.kinsTable.get("spam")+" + "+Lesson.kinsTable.get("ham")+" = "+Lesson.examples);
            // System.out.println(Lesson.vocabulary);
            // System.out.println("# Times 'sentence' appears in ham : "+Lesson.wordsTables.get(1).get("sentence"));

            successRate             = Calculation.getSuccessRate(Utils.EXAMPLES_FILE, chunkSize, round);
            accumulatedSuccessRate += successRate;
            System.out.println(round + " success rate: " + successRate);

        }//end for

        System.out.println("TOTAL success rate: " + accumulatedSuccessRate/4);

    }//end main
    
}//end KNBC