public class SolverM {
    //this is a multi threaded version of Solver which runs the A* algorithm in parallel.
    SNode solution;
    
    
    public SolverM(Board initial)
    {
        // find a solution to the initial board (using the A* algorithm)
        
        MinPQ<SNode> pq = new MinPQ<SNode>();
        MinPQ<SNode> pq_twin = new MinPQ<SNode>();
        SNode current = new SNode(initial,null,0);
        SNode current_twin = new SNode(initial.twin(),null,0);
        pq.insert(current);
        pq_twin.insert(current_twin);
        
        //implement A* algorithm
        //Pull min Node from pq and insert it's neighbors.
        
        boolean solved = false;
        
        while(!solved)
        {
            current = pq.delMin();
            current_twin = pq_twin.delMin();
            
            if (current.board.isGoal())
            {
                solution = current;
                break;
            }
            if (current_twin.board.isGoal())
            {
                solution = null;
                break;
            }
            
            
            for (Board b : current.board.neighbors())
            {
                if (current.prev != null && b.equals(current.prev.board))
                {
                    continue;
                }
                pq.insert(new SNode(b, current, current.moves + 1));
            }
            
            for (Board b : current_twin.board.neighbors())
            {
                if (current_twin.prev != null && b.equals(current_twin.prev.board))
                {
                    continue;
                }
                pq_twin.insert(new SNode(b, current_twin, current.moves + 1));
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
    
    class SNode implements Comparable<SNode>{
        Board board;
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
    
    public static void main(String [] args)
    {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // solve the slider puzzle
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        System.out.println(filename + ": " + solver.moves());
        
        for (Board b : solver.solution())
        {
            System.out.println(b);
        }
    }
}