public class Solver {
    
    SNode solution;
    
    public Solver(Board initial)
    {
        // find a solution to the initial board (using the A* algorithm)
        
        MinPQ<SNode> pq = new MinPQ<SNode>();
        int moves = 0;
        SNode current = new SNode(initial,null,moves);
        pq.insert(current);
        
        //implement A* algorithm
        //Pull min Node from pq and insert it's neighbors.
        
        boolean solved = false;
        
        while(!solved)
        {
            current = pq.min();
            if (current.board.isGoal())
            {
                solution = current;
                break;
            }
            moves++;
            for (Board b : current.board.neighbors())
            {
                if (b.equals(current.prev))
                {
                    continue;
                }
                pq.insert(new SNode(b, current, moves));
            }
        }
        
    }
    
    public boolean isSolvable()
    {
        // is the initial board solvable?
        if (solution == null)
            return false;
        return true;
    }
    
    public int moves()
    {
        // min number of moves to solve initial board; -1 if unsolvable
        if (solution == null)
            return -1;
        return solution.moves;
    }
    
    public Iterable<Board> solution()
    {
        // sequence of boards in a shortest solution; null if unsolvable
        if (solution == null)
            return null;
        
        Stack<Board> st = new Stack<Board>();
        SNode ptr = solution;
        while (ptr != null)
        {
            st.push(ptr.board);
            ptr = ptr.prev;
        }
        return st;
    }
    
    public static void main(String[] args)
    {
        // solve a slider puzzle (given below)
    }
    
    private class SNode implements Comparable<SNode>{
        private Board board;
        private SNode prev;
        private int priority;
        private int moves;
        
        public SNode(Board current, SNode prev, int moves)
        {
            this.board = current;
            this.prev = prev;
            this.priority = current.manhattan() + moves;
            this.moves = moves;
        }

        @Override
        public int compareTo(SNode o) {
            return this.priority - o.priority;
        }
        
    }
}