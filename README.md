# Boggle-Puzzle-Solver
Java Project to solve Boggle puzzles.

<center> The objective of this problem is to accept a set of words along with a puzzle grid and display
the words that appear in the puzzle, along with the path traversed to encounter the word, to
an user [1]. The general solution is as follows: Start at one point in the puzzle grid and traverse
through the grid in any one of 8 directions. Whenever a word in the set of words is
encountered through a (non-repetitive) path, the word is considered as “Present”.
The program functions as follows. Users can input a set of words and a puzzle grid. The set
contains words that might appear in the grid. The program traverses through the puzzle and
generates a sorted list of words which can be located in the grid along with the path
traversed to obtain the word.
</center>

## Classes:
Boggle.java
This class represents instances of a Boggle Puzzle:
Each object of type Boggle is associated with the following attributes:
Methods:
• boolean getDictionary(BufferedReader stream):
  ✓ Return Type: boolean
• boolean getPuzzle(BufferedReader stream)
  ✓ Return Type: boolean
• List solve()
  ✓ Return Type: List of Strings
• String print( )
  ✓ Return Type: String
  
The initial approach that I implemented was a Brute-Force approach where the code starts
from each cell in the puzzle grid and recursively travels in all 8 directions (if possible).
Stopping Conditions (Grid’s Boundary):
1. When the next cell’s X-coordinate is lesser than zero.
2. When the next cell’s Y-coordinate is lesser than zero.
3. When the next cell is outside the last row (index is greater than the number of columns).
4. When the next cell is outside the last column (index is greater than the number of rows).
Subsequently, in order to improve the efficiency of the code the following objective was
achieved:
“Traverse only cells that are relevant to words in the dictionary”
In order to achieve this objective, the following conditions were included in the code:
1. Instead of traversing all the cells in the puzzle grid, for each word in the dictionary, start
traversal from a cell only if the character in the cell matches the starting letter of the word.
2. During traversal of each cell, and comparison of the new word with the expected word,
move on to the next traversal only when the recently appended word matches the substring
of the expected word.
Ex: Word to be found is book. Current traversal has formed a word – “bo”. Since “bo” is a
valid substring of “book” we can continue traversal.
If the current traversal had formed a word “bi” no further traversals can be made since the
current word is not a valid substring of the expected word – “book”.
3. During traversal of each cell the length of the current word and expected words are
compared. If the length of the current word is GREATER than that of the expected word, no
further traversals will be made. (It would not yield any result).  
