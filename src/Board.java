package nqueens.util;
import java.util.*;

// Board classes are to represent the square chessboards.
// A Board object is not not complete unless it has the same width and depth
// Board can be constructed with comma sepearted string such as 'q1,q2,q3,...'.

public class Board {
  private int dimension; //the dimension of the board
  private List<Integer> queensPos;

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
    String str=StringUtils.join(queensPos,",");
    return str;
  }

  public List<String> generateNewBoards(){
    //if queensPos is not initialized, throw an exception
    //if queensPos already has enough number of elements, halt.

    boolean[] cells = new boolean[dimension];
	for(int i=0;i<cells.length;i++)
		cells[i]=true;
	
    for (int row = 0;row<queensPos.size();row++){
        //eliminate all existing columns
        cells[queensPos.get(row)]=false;

        //eliminate cells that in the diagnal
        int leftDiagonal = queensPos.get(row) - (queensPos.size()-row);
        int rightDiagonal = queensPos.get(row) + (queensPos.size()-row);

        if (leftDiagonal>=0) cells[leftDiagonal]=false;
        if (rightDiagonal<=dimension) cells[rightDiagonal]=false;
    }

    List<String> res = new ArrayList<String>();
    String oldBoard = this.toString();

    for (int col =0;col<dimension;col++){
        if (cells[col]==true){
            res.add(oldBoard.concat(","+Integer.toString(col)));
        }
    }
    return res;
  }
}
