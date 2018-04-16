import java.io.FileNotFoundException;
import java.io.IOException;

/*DEBUGGING NOTES:

I found out that the lesson works just fine, it fills the tables as it must,
but the problem resides in Calculation.setProbability when it comes to making the calculation
before even putting it in the table, it is already 0. It is important to note that the probability 
before making such calculation is fine and correct. This leads me to think that maybe a double is not enough...

1. Make another check case. That is, create a relatively small Examples.csv file and compute the probability by hand
    If you end up with the same probability number as your program, then...

2. Learn more about the nature of double numbers. Actually know their limit. Omar says it is improbable, but
    I think there are just too many words per example in this new file.

*/

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