import java.io.*;
import java.util.*;

public class Boggle
{
    Set<String> wordDictionary; //Set to store the Dictionary
    List<List<Character>> puzzleGrid; //Nested ArrayList to store the Puzzle Grid
    List<char[]> printPuzzle; //List of Arrays to print the Puzzle
    String outputString;
    List<String> wordsFound; //List of Strings which display the words that are found and the path traversed to find them
    int numRows,numColumns; //Object to dynamically store the number of rows and Columns for future use
    Map<String,String> sequenceMap = new HashMap<>();


    public Boggle()
    {
        wordDictionary= new HashSet<String>(); //List of words to be found
        puzzleGrid = new ArrayList(); //2D ArrayList which stores the Puzzle Grid
        printPuzzle=new ArrayList(); //2D ArrayList which contains a list of character arrays to be printed
        outputString="";
        wordsFound = new ArrayList<String>(); //List of Words found
        numRows=numColumns=0; //Number of rows and columns in the Puzzle Grid
        /* Map the change in coordinates with a Direction
        For example if we move in a Left direction from one cell to another The X coordinate
        decreases by 1 and the Y coordinate remains the same, therefore (-1,0)
         */
        sequenceMap.put("-10","L");
        sequenceMap.put("10","R");
        sequenceMap.put("01","U");
        sequenceMap.put("0-1","D");
        sequenceMap.put("-11","N");
        sequenceMap.put("11","E");
        sequenceMap.put("1-1","S");
        sequenceMap.put("-1-1","W");
    }
    boolean getDictionary(BufferedReader stream) throws IOException {
        String word=null;

        while((word=stream.readLine()) != null)//While the end of the file is not reached [&& !(word.isBlank())]   !(word=stream.readLine()).isBlank()
        {
            if(word.isEmpty()) {break;}
            wordDictionary.add(word); //Add the word to the Dictionary if it's valid
        }
        System.out.println(wordDictionary.toString());
        return true;
    }

    boolean getPuzzle(BufferedReader stream)
    {
        String line; //Object to store each input line from the user
        int index=0; //Object which acts as the outer-index of the puzzle grid
        try
            {
                while((line=stream.readLine()) != null)

                {
                    if(line.isEmpty()) {break;}
                    puzzleGrid.add(new ArrayList<Character>()); //Add a new row of cells to the grid

                    printPuzzle.add(line.toCharArray()); //Store each line of the puzzle to print it using the method print()

                    for(char c: line.toCharArray())
                    {
                        puzzleGrid.get(index).add(c);
                    }
                    index+=1; //Add the next line of characters in another nested ArrayList
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        int rowLength=puzzleGrid.get(0).size();
        numRows=puzzleGrid.size(); //Store the number of rows
        numColumns=rowLength; //Store the number of columns
        for(int i=1;i<puzzleGrid.size();i++)
        {
            if(puzzleGrid.get(i).size()!=rowLength) //If all the rows do not have the same row length do not create the grid and return false
            {
                return false;
            }
        }
        System.out.println(puzzleGrid.toString());
        return true;
    }

    List<String> solve() {
        //List<String> wordsFound = new ArrayList<String>(); //Object to store the words that can be found and the path traversed
        List<List<Boolean>> traversed = new ArrayList<>(); //Nested ArrayList to log if a cell is visited

        for(int r=0;r<puzzleGrid.size();r++)
        {
            traversed.add(new ArrayList<Boolean>(puzzleGrid.get(0).size())); //Create a structure to record traversed cells
            for(int c=0;c<puzzleGrid.get(0).size();c++)
            {
                traversed.get(r).add(false); //Initialize traversed with false to indicate that none of the cells have been visited
            }
        }
//        System.out.println("TRAVERSED");
//        System.out.println(traversed.toString());
        String dictionaryWord=""; //String to store each word of the input dictionary
        String sequence="";
        for(int rows=0;rows<puzzleGrid.size();rows++)
        {
            for(int columns=0;columns<puzzleGrid.get(rows).size();columns++)
            {
                String path=String.valueOf(numRows-rows) + String.valueOf(columns+1); //Formula to start lower left-most cell at (1,1) as instructed in the assignment handout
                //System.out.println("Path: " + path);
                traverseRecursively(puzzleGrid,traversed,rows,columns,dictionaryWord,path,sequence); //Start from a cell in the grid and traverse to other cells, do this for all the cells
            }
        }
        //System.out.println(wordsFound.toString());
        return wordsFound;
    }

    private void traverseRecursively(List<List<Character>> puzzleGrid,List<List<Boolean>> traversed,int rows,int columns,String word,String path,String sequence)
    {
        traversed.get(rows).set(columns,true); //Mark this cell as visited in the Nested Array list
        //path+=String.valueOf(numRows-rows) + String.valueOf(columns+1);
        int currCoordinateRow=(numRows-rows);
        //System.out.println("CCROW: " + currCoordinateRow);
        int currCoordinateColumn=(columns+1);
        //System.out.println("CCcol: " + currCoordinateColumn);
        int prevCoordinateRow=Character.getNumericValue(path.charAt(path.length()-2));
        //System.out.println("PCRow: " + prevCoordinateRow);
        int prevCoordinateColumn=Character.getNumericValue(path.charAt(path.length()-1));
        //System.out.println("PCcol: " + prevCoordinateColumn);
        int xTraversal=currCoordinateRow-prevCoordinateRow;
        //System.out.println("xChange: " + xTraversal);
        int yTraversal=currCoordinateColumn-prevCoordinateColumn;
        //System.out.println("yChange: " + yTraversal);

        String key=String.valueOf(yTraversal)+String.valueOf(xTraversal);
        //System.out.println("Key: " + key);
        //System.out.println(prevCoordinateRow + "///" + prevCoordinateColumn);

        if(xTraversal==0 && yTraversal==0)  //That is starting from a new cell
        {
            sequence = "";
        }
        else
        {
            String directionLetter = sequenceMap.get(key);
            //System.out.println(directionLetter);
            sequence += directionLetter;
        }

        path+=String.valueOf(numRows-rows) + String.valueOf(columns+1);

        String newWord;
        newWord=word+puzzleGrid.get(rows).get(columns);//Append the character in current cell to the string and check if it's present in the input list of words

        if(wordDictionary.contains(newWord)) //If a word is encountered record the path, the starting cell's s
        {
            //System.out.println(newWord+"\t"+path.charAt(0)+"\t"+path.charAt(1)+"\t" + sequence);
            outputString=newWord+"\t"+path.charAt(0)+"\t"+path.charAt(1)+"\t" + sequence;
            wordsFound.add(outputString);
            //wordsFound.add();
        }
        /*Attempt to traverse in all 8 directions from the current cell
        * Return if the indices are beyond the edges of the grid or out of bounds
        * That is,
        * if row or column < 0
        * If row or column > the number of rows or columns in the puzzle respectively */
        for(int r=rows-1;r<=rows+1;r++)
        {
            for(int c=columns-1;c<=columns+1;c++)
            {
                /*If the new cell is within the boundaries of the grid
                    and has not been traversed in this iteration yet, navigate to the cell*/
                if(r>=0 && r<puzzleGrid.size() && c>=0 && c<puzzleGrid.get(rows).size() && traversed.get(r).get(c)==false)
                {
                    traverseRecursively(puzzleGrid,traversed,r,c,newWord,path,sequence);
                }
            }
        }
        traversed.get(rows).set(columns,false);
    }


    String print()
    {
        String puzzleString=""; //Object to temporarily store each line of the puzzle grid
        if(puzzleGrid.size()>0)
        {
            for (int i = 0; i < printPuzzle.size() - 1; i++)
            {
                StringBuilder tempString = new StringBuilder(); //Stringbuilder stores the character array of each line in the form of a string (without commas)
                for(Character c:printPuzzle.get(i))
                {
                    tempString.append(c);
                }
                puzzleString+=tempString.toString();
                puzzleString+="\n";
            }
            StringBuilder tempString = new StringBuilder();
            for(Character c:printPuzzle.get(puzzleGrid.size() - 1)) //To store the last line of the puzzle without a new line after it
            {
                tempString.append(c);
            }
            puzzleString+=tempString.toString();
        }
        return puzzleString;
    }
}
/* Exceptions:
For Print() (if Puzzlegrid is empty)
 */