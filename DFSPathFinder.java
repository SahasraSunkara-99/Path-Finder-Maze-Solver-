import java.util.*;

public class DFSPathFinder implements PathFinder {
 
 @Override
 public List<Cell> findPath(Cell start, Cell end, HashMap<Cell, List<Cell>> adjacencyList, MainWindow.AnimationEngine engine) {
     if (start == null || end == null || start.isWall() || end.isWall()) return Collections.emptyList();
     
     Stack<Cell> stack = new Stack<>();
     Map<Cell, Cell> parentMap = new HashMap<>();
     Set<Cell> visited = new HashSet<>();

     stack.push(start);
     parentMap.put(start, null);
     
     try {
         while (!stack.isEmpty()) {
             Cell current = stack.pop();

             if (visited.contains(current)) continue;
             visited.add(current);
             
             if (!current.isStart() && !current.isEnd()) {
                 engine.animateVisit(current);
             }
             
             if (current.equals(end)) {
                 return reconstructPath(start, end, parentMap);
             }

             List<Cell> neighbors = adjacencyList.getOrDefault(current, Collections.emptyList());
             // Reverse neighbors to promote a "branch first" exploration look
             
             List<Cell> neighborsToVisit = new ArrayList<>(neighbors);
             Collections.reverse(neighborsToVisit);

             for (Cell neighbor : neighborsToVisit) {
                 if (!visited.contains(neighbor)) {
                     parentMap.put(neighbor, current);
                     stack.push(neighbor);
                 }
             }
         }
     } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
     }
     
     return Collections.emptyList();
 }

 private List<Cell> reconstructPath(Cell start, Cell end, Map<Cell, Cell> parentMap) {
     List<Cell> path = new ArrayList<>();
     Cell current = end;
     while (current != null) {
         path.add(current);
         current = parentMap.get(current);
     }
     Collections.reverse(path);
     return path;
 }
}