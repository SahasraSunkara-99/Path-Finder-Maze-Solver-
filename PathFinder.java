// src/PathFinder.java
import java.util.HashMap;
import java.util.List;

public interface PathFinder {
    List<Cell> findPath(Cell start, Cell end, HashMap<Cell, List<Cell>> adjacencyList, MainWindow.AnimationEngine engine);
}