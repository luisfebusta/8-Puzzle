import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    
    private int [][] tiles;
    private int N;
    private int hamming;
    private int manhattan;
    private boolean hammingFlag;
    private boolean manhattanFlag;
    
    public Board(int[][] blocks)
    {
        // construct a board from an N-by-N array of blocks
        // (where blocks[i][j] = block in row i, column j)
        if (blocks == null)
            throw new NullPointerException("Can't construct board from null array");
        tiles = deepCopyIntMatrix(blocks);
        N = tiles.length;
    }
                                           
    public int dimension()
    {   
        // board dimension N
        return N;
    }
    
    public int hamming()
    {
        // number of blocks out of place
        if (hammingFlag)
            return hamming;
        
        hamming = 0;
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (tiles[i][j] != i*N + j + 1)
                {
                    hamming++;
                }
            }
        }
        hammingFlag = true;
        return hamming;
    }
    
    public int manhattan()
    {
        // sum of Manhattan distances between blocks and goal
        if (manhattanFlag)
            return manhattan;
        manhattan = 0;
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (tiles[i][j] == 0)
                    continue;   
                if (tiles[i][j] != i*N + j + 1)
                {
                    //calculate right row and column
                    int row = (tiles[i][j] - 1) / N;
                    int col = (tiles[i][j] - 1) % N;
                    manhattan += Math.abs(i-row) + Math.abs(j-col);
                }
            }
        }
        manhattanFlag = true;
        return manhattan;
    }
    
    public boolean isGoal()
    {
        // is this board the goal board?
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (tiles[i][j] != 0 && tiles[i][j] != i*N + j + 1)
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public Board twin()
    {
        //a board that is obtained by exchanging two adjacent blocks in the same row
        // method is synchronized since it temporarily modifies the underlying board
        // to avoid having to duplicate the board array.
        
        // try swapping first 2 positions on row 1 or row 2
        int [][] tmpTiles = deepCopyIntMatrix(tiles);
        int row = 1;
        if (tmpTiles[0][0] != 0 && tmpTiles[0][1] != 0)
        {
            row = 0;
        }
        
        // temporarily swap two adjacent blocks;
        int tmp0 = tmpTiles[row][0];
        int tmp1 = tmpTiles[row][1];
        tmpTiles[row][0] = tmp1;
        tmpTiles[row][1] = tmp0;
        
        // create new board with the two swapped blocks;
        Board twin = new Board(tmpTiles);
        
        return twin;
    }
    
    public boolean equals(Object y)
    {
        // does this board equal y?
         if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (!Arrays.deepEquals(this.tiles, that.tiles)) return false;
        
        return true;       
    }
    
    public Iterable<Board> neighbors()
    {
        ArrayList<Board> neighbors = new ArrayList<Board>();
        // all neighboring boards
        int [][] tmp = tiles.clone();
        
        // need to find 0 in the board
        int i = 0;
        int j = 0;
        
        outerloop:
        for (i = 0; i < N; i++)
        {
            for (j = 0; j < N; j++)
            {
                if (tmp[i][j] == 0)
                    break outerloop;
            }
        }

        if (i != 0)
        {
            // move 0 to previous row
            swap(tmp, i, j, i - 1, j);
            neighbors.add(new Board(tmp));
            // move 0 back to original row
            swap(tmp, i, j, i - 1, j);
        }
        
        if (i != N - 1)
        {
            // move 0 to next row
            swap(tmp, i, j, i + 1, j);
            neighbors.add(new Board(tmp));
            // move 0 back to original row
            swap(tmp, i, j, i + 1, j);
        }
        
        if (j != 0)
        {
            // move 0 to column to the left
            swap(tmp, i, j, i, j - 1);
            neighbors.add(new Board(tmp));
            // move 0 back
            swap(tmp, i, j, i, j - 1);
        }
        
        if (j != N - 1)
        {
            // move 0 to the column to the right
            swap(tmp, i, j, i, j + 1);
            neighbors.add(new Board(tmp));
            // move 0 back
            swap(tmp, i, j, i, j + 1);
        }
        return neighbors;
    }  
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
   /* public String toString()
    {
        // string representation of this board (in the output format specified below)
        
        
        int width = Integer.toString(N*N).length();
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                sb.append(" ");
                sb.append(String.format("%" + width + "d",board[i][j]));
                sb.append(" ");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }*/
    
    private static int[][] deepCopyIntMatrix(int[][] input) {
        if (input == null)
            return null;
        int[][] result = new int[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
    
    private static void swap(int [][] a, int row1, int col1, int row2, int col2)
    {
        int tmp = a[row1][col1];
        a[row1][col1] = a[row2][col2];
        a[row2][col2] = tmp;
    }

    public static void main(String[] args)
    {
        // unit tests (not graded)
        //int[][] a = {{0, 1, 3} , {4, 2, 5} , {7, 8, 6}};
        int[][] a = {{1, 2, 3} , {4, 5, 0} , {7, 8, 6}};
        
        Board tst = new Board(a);
        
        System.out.println("The Board is: ");
        System.out.println(tst.toString());
        
        System.out.println("Manhattan: " + tst.manhattan());
        
        System.out.println("The twin is: ");
        System.out.println(tst.twin());
        
        System.out.println("Neighbors are: ");
        for (Board i : tst.neighbors())
        {
            if (i.isGoal())
                System.out.println("GOAL!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(i);
        }
    }
}