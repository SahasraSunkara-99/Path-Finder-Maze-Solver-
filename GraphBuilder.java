// src/GraphBuilder.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphBuilder {
    
    private final int rows;
    private final int cols;

    public GraphBuilder(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public HashMap<Cell, List<Cell>> buildGraph(Cell[][] grid) {
        HashMap<Cell, List<Cell>> adjacencyList = new HashMap<>();

        int[] dr = {-1, 1, 0, 0}; 
        int[] dc = {0, 0, -1, 1}; 

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell currentCell = grid[r][c];
                
                if (currentCell.isWall()) continue;

                adjacencyList.put(currentCell, new ArrayList<>());

                for (int i = 0; i < 4; i++) {
                    int nr = r + dr[i];
                    int nc = c + dc[i];

                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        Cell neighbor = grid[nr][nc];
                        if (!neighbor.isWall()) {
                            adjacencyList.get(currentCell).add(neighbor);
                        }
                    }
                }
            }
        }
        return adjacencyList;
    }

}
