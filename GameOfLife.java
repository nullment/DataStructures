package conwaygame;

import java.util.ArrayList;

import javax.lang.model.type.UnionType;

/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many
 * iterations/generations.
 *
 * Rules
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * 
 Dead cells with exactly 3 neighbors become alive by reproduction.
 * 
 * @author Seth Kelley
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean DEAD = false;

    private boolean[][] grid; // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
     * Default Constructor which creates a small 5x5 grid with five alive cells.
     * This variation does not exceed bounds and dies off after four iterations.
     */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
     * Constructor used that will take in values to create a grid with a given
     * number
     * of alive cells
     * 
     * @param file is the input file with the initial game pattern formatted as
     *             follows:
     *             An integer representing the number of grid rows, say r
     *             An integer representing the number of grid columns, say c
     *             Number of r lines, each containing c true or false values (true
     *             denotes an ALIVE cell)
     */
    public GameOfLife(String file) {
        StdIn.setFile(file);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        grid = new boolean [r][c];
        for(int i=0; i<r; i++){
            for(int j=0; j<c; j++){
                grid[i][j] = StdIn.readBoolean();
            }
        }
    }   

    /**
     * Returns grid
     * 
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid() {
        return grid;
    }

    /**
     * Returns totalAliveCells
     * 
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells() {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * 
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState(int row, int col) {
        if(grid[row][col] == true)
        return true; 
        else
        return false;
        // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * 
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive() {
        for (int i = 0; i<grid.length; i++){
            for(int j = 0; j< grid[i].length; j++){
                if(grid[i][j] == true){
                    return true;
                }
            }
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors(int row, int col) {
        int count = 0;
        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                int r = row+i;
                int c = col+j;
                if(r<0)
                r = grid.length-1;
                if(r>grid.length-1)
                r = 0;
                if(c<0)
                c = grid[0].length-1;
                if(c>grid[r].length-1)
                c = 0;
                if(r==row && c==col)
                continue;
                if(grid[r][c]){
                        count++;

                }

                }
            }
        return count;
    }

    private void rules(int row, int col, boolean[][] arr){
        if(grid[row][col]==true && numOfAliveNeighbors(row, col) < 2)
        arr[row][col] = false;
        if(numOfAliveNeighbors(row, col) == 3 && grid[row][col] == false)
        arr[row][col] = true;
        if(grid[row][col]==true &&( numOfAliveNeighbors(row, col) == 2 || numOfAliveNeighbors(row, col) == 3))
        arr[row][col] = true;
        if(grid[row][col]==true && numOfAliveNeighbors(row, col) > 3)
        arr[row][col] = false;
    }
    
    private void rulesNeighbors(int row, int col, boolean[][] arr){
        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                int r = row+i;
                int c = col+j;
                if(r<0)
                r = grid.length-1;
                if(r>grid.length-1)
                r = 0;
                if(c<0)
                c = grid[0].length-1;
                if(c>grid[r].length-1)
                c = 0;
                if(r==row && c==col)
                continue;
                rules(r, c, arr); 
            }
        }
    }
    /**
     * Creates a new grid with the next generation of the current grid using
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid() {
        boolean[][] newgrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < newgrid.length; i++){
            for (int j = 0; j < newgrid.length; j++){
                    rules(i, j, newgrid);
                    rulesNeighbors(i, j, newgrid);
            }
        }
         return newgrid;// update this line, provided so that code compiles
    }
    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration() {
        grid = computeNewGrid();
    }

    /**
     * Updates the current grid with the grid computed after multiple (n)
     * generations.
     * 
     * @param n number of iterations that the grid will go through to compute a new
     *          grid
     */
    public void nextGeneration(int n) {
        for (int i = 0; i < n ;i++){
            grid = computeNewGrid();
        }
        return;

    }
    private void helpCommunities(int row, int col, ArrayList<Integer> com, WeightedQuickUnionUF obj){
        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                int r = row+i;
                int c = col+j;
                if(r<0)
                r = grid.length-1;
                if(r>grid.length-1)
                r = 0;
                if(c<0)
                c = grid[0].length-1;
                if(c>grid[r].length-1)
                c = 0;
                if(r==row && c==col)
                continue;
                if(grid[r][c] == true){
                    if(com.contains(obj.find(r,c))){
                        obj.union(r,c,row,col);
                        com.remove(Integer.valueOf(obj.find(row,col)));
                    }
                    else 
                    obj.union(row,col,r,c);
                }
            }
        }
        if(!com.contains(obj.find(row,col)))
        com.add(obj.find(row,col));
    }
    /**
     * Determines the number of separate cell communities in the grid
     * 
     * @return the number of communities in the grid, communities can be formed from
     *         edges
     */
    public int numOfCommunities() {
        ArrayList<Integer> com = new ArrayList<>();
        WeightedQuickUnionUF obj = new WeightedQuickUnionUF(grid.length,grid[0].length);
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[0].length; j++){
                if(grid[i][j] == true){
                helpCommunities(i,j, com, obj);
                }
            }
        }
        com.clear();
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[0].length; j++){
                if(grid[i][j] == true){
                helpCommunities(i,j, com, obj);
                }
            }
        }
        return com.size(); // update this line, provided so that code compiles
    }
}
