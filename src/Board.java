import java.util.*;

import org.apache.hadoop.io.Text;

// Board classes are to represent the square chessboards.
// A Board object is not not complete unless it has the same width and depth
// Board can be constructed with comma sepearted string such as 'q1,q2,q3,...'.

public class Board {
  private int dimension; //the dimension of the board
  private List<Integer> queenPos;

/*
  public Board(int dim){
    dimension = dim;
    rows = new ArrayList<String>();
  }
*/
  public Board(String boardStr) {
	String[] position = boardStr.split(",");
    queensPos = new ArrayList<Integer>();
    for (String item:position)
        queensPos.add(Integer.parseInt(item));
  }

  public String toString(){
    String str=StringUtils.join(queenPos,",");
    return str;
  }

  public List<String> generateNewBoards(){
    //if queenPos is not initialized, throw an exception
    //if queenPos already has enough number of elements, halt.

    boolean[] cells = new boolean[dimension]{true};
    for (int row = 0;row<queenPos.size();row++){
        //eliminate all existing columns
        cells[queenPos.get(row)]=false;

        //eliminate cells that in the diagnal
        int leftDiagonal = queenPos.get(row) - (queensPos.size()-row);
        int rightDiagonal = queenPos.get(row) + (queensPos.size()-row);

        if (leftDiagonal>=0) cells[leftDiagnonal]=false;
        if (rightDiagonal<=dimension) cells[rightDiagonal]=false;
    }

    List<String> res = new ArrayList<String>();
    String oldBoard = this.toString();

    for (int col =0;col<dimension;col++){
        if (cells[col]==true){
            res.add(oldBoard.append(","+Integer.toString(col)));
        }
    }
    return res;
  }
}
