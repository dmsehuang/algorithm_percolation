
public class Percolation {
    private WeightedQuickUnionUF unionFind;
    private WeightedQuickUnionUF unionFull;
    private boolean[] grid;
    private int N;
    
    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be positive");
        }
        // declare data structure
        this.N = N;
        this.unionFind = new WeightedQuickUnionUF(N*N+2);
        this.unionFull = new WeightedQuickUnionUF(N*N+1);
        this.grid = new boolean[N*N+2];
        // initialize the array
        for (int i = 1; i <= N*N; i++) {
            this.grid[i] = false; // false means blocked
        }
        
    }
    
    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new IndexOutOfBoundsException("i,j should be between 1 and N");
        }
        if (isOpen(i, j)) return;
        // open this position
        int id = (i-1)*N+j;
        grid[id] = true;
        // connect the 1st line to the top 
        if (i == 1) {
            unionFind.union(id, 0);
            unionFull.union(id, 0);
        }
        if (i == N) unionFind.union(id, N*N+1);
        // connect it with other opened positions
        if (i+1 <= N && isOpen(i+1, j)) {
            unionFind.union(id, id+N);
            unionFull.union(id, id+N);
        }
        if (i-1 >= 1 && isOpen(i-1, j)) {
            unionFind.union(id, id-N);
            unionFull.union(id, id-N);
        }
        if (j+1 <= N && isOpen(i, j+1)) {
            unionFind.union(id, id+1);
            unionFull.union(id, id+1);
        }
        if (j-1 >= 1 && isOpen(i, j-1)) {
            unionFind.union(id, id-1);
            unionFull.union(id, id-1);
        }
        // backwash, connect the last line to the bottom only if it connects to the top
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new IndexOutOfBoundsException("i,j should be between 1 and N");
        }
        return grid[(i-1)*N+j];
    }
    
    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        if (i < 1 || i > N || j < 1 || j > N) {
            throw new IndexOutOfBoundsException("i,j should be between 1 and N");
        }
        // bug 1 if the 1st line is not open, it still connects with the top node!
        return unionFull.connected(0, (i-1)*N+j);
    }
    
    // does the system percolate?
    public boolean percolates() {
        return unionFind.connected(0, N*N+1);
    }
}
