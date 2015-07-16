import java.util.Arrays;
import java.util.Iterator;

public class Board {
    
    private int [][] board;
    private int N;
    
    public Board(int[][] blocks)
    {
        // construct a board from an N-by-N array of blocks
        // (where blocks[i][j] = block in row i, column j)
        if (blocks == null)
            throw new NullPointerException("Can't construct board from null array");
        board = blocks.clone();
        
        N = blocks.length;
    }
                                           
    public int dimension()
    {   
        // board dimension N
        return N;
    }
    
    public int hamming()
    {
        // number of blocks out of place
        
        int hamming = 0;
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (board[i][j] != i*N + j + 1 )
                {
                    hamming++;
                }
            }
        }
        
        return hamming;
    }
    
    public int manhattan()
    {
        // sum of Manhattan distances between blocks and goal
        int manhattan = 0;
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (board[i][j] != i*N + j + 1 )
                {
                    manhattan += Math.abs(i*N + j + 1 - board[i][j]) ;
                }
            }
        }
        
        return manhattan;
    }
    
    public boolean isGoal()
    {
        // is this board the goal board?
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                if (board[i][j] != i*N + j + 1 )
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public synchronized Board twin()
    {
        // a board that is obtained by exchanging two adjacent blocks in the same row
        // method is synchronized since it temporarily modifies the underlying board to avoid
        // having to duplicate the board array.
        
        // try swapping first 2 positions on row 1 or row 2
        
        int row = 1;
        if (board[0][0] != 0 && board[0][1] != 0)
        {
            row = 0;
        }
        
        // temporarily swap two adjacent blocks;
        int tmp0 = board[row][0];
        int tmp1 = board[row][1];
        board[row][0] = tmp1;
        board[row][1] = tmp0;
        
        // create new board with the two swapped blocks;
        Board twin = new Board(board);
        
        // return board to it's original state
        board[row][0] = tmp0;
        board[row][1] = tmp1;
        
        return twin;
    }
    
    public boolean equals(Object y)
    {
        // does this board equal y?
        
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (!Arrays.deepEquals(this.board, that.board)) return false;
        
        return true;       
    }
    
    public Iterable<Board> neighbors()
    {
        // all neighboring boards
        
        return new NeighborIterator();
    }  
    
    private class NeighborIterator implements Iterator<Board>
    {
        private Board [] neighbors;
        private int current = 0;
        
        
        public NeighborIterator()
        {
            int [][] tmp = board.clone();
            
            // need to find 0 in the board
            int n = 2;          // there are at least 2 neighbors
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
            
            if (i != 0 && i != N - 1)
                n++;
            if (j != 0 && j != N - 1)
                n++;
            
            neighbors = new Board[n];
            
            n = 0;
            
            if (i != 0)
            {
                // move 0 to previous row
                swap(tmp, i, j, i - 1, j);
                neighbors[n++] = new Board(tmp);
                // move 0 back to original row
                swap(tmp, i, j, i - 1, j);
            }
            
            if (i != N - 1)
            {
                // move 0 to next row
                swap(tmp, i, j, i + 1, j);
                neighbors[n++] = new Board(tmp);
                // move 0 back to original row
                swap(tmp, i, j, i + 1, j);
            }
            
            if(j != 0)
            {
                // move 0 to column to the left
                swap(tmp, i, j, i, j - 1);
                neighbors[n++] = new Board(tmp);
                // move 0 back
                swap(tmp, i, j, i, j - 1);
            }
            
            if(j != N - 1)
            {
                // move 0 to the column to the right
                swap(tmp, i, j, i, j + 1);
                neighbors[n++] = new Board(tmp);
                // move 0 back
                swap(tmp, i, j, i, j + 1);
            }
            
                
        }
        @Override
        public boolean hasNext() {
            if (current < neighbors.length)
                return true;
            
            return false;
        }
        @Override
        public Board next() {
            if (current >= neighbors.length)
                return null;
            
            return neighbors[current++];
        }
        
        private void swap(int [][] a, int row1, int col1, int row2, int col2)
        {
            int tmp = a[row1][col1];
            a[row1][col1] = a[row2][col2];
            a[row2][col2] = tmp;
        }
        
    }
    
    public String toString()
    {
        // string representation of this board (in the output format specified below)
        
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                sb.append(" ");
                sb.append(board[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }

    public static void main(String[] args)
    {
        // unit tests (not graded)
    }
}