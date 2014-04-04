import java.util.*;

import org.apache.hadoop.io.Text;

// Board classes are to represent the square chessboards.
// A Board object is not not complete unless it has the same width and depth

public class Board {
  private int dimension; //the dimension of the board
  private List<String> rows;

  public Board(int dim){
    dimension = dim;
    rows = new ArrayList<String>();
  }
  public Board(String boardLayout ) {
    String[] tempRows = boardLayout.split("|");
    rows.addAll(tempRows);
  }

  public toString(){
    String str="";
    for (String row:rows){
      str=str+row+"|";
    }
    return str;
  }
}
