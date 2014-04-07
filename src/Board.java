import java.util.*;

import org.apache.hadoop.io.Text;

// Board classes are to represent the square chessboards.
// A Board object is not not complete unless it has the same width and depth

public class Board {
  private int dimension; //the dimension of the board
  private int[] queenPos;

/*
  public Board(int dim){
    dimension = dim;
    rows = new ArrayList<String>();
  }
*/
  public Board(String boardStr) {
	String[] token = boardStr.split("|");
	String[] position = token[1].split(",");
	dimension = token[0];
	queensPos = Integer.parseInt(position);
  }

  public String toString(){
    String str=Integer.toString(dimension);
	str = str+"|"+StringUtils.join(queenPos);
    return str;
  }

  private Board[] generateNewBoard(){
    StringBuffer newRow = new StringBuffer(dimension,0);
	
    for (int col = 0;col<dimension;col++){
      //check if there is conflict in the same column
	  int row=0;
	  for (;row<queensPos.length;row++)
		if (queens[row]==col) break;
	
	  if (row!=rows.length ) continue;
	  

      //check if there there are conflicts in the diagonal direction
	  
    }
  }
}
