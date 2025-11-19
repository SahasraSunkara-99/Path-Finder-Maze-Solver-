import java.util.*;

public class BFSPathFinder implements PathFinder {
 
 @Override
 public List<Cell> findPath(Cell start, Cell end, HashMap<Cell, List<Cell>> adjacencyList, MainWindow.AnimationEngine engine) {
     if (start == null || end == null || start.isWall() || end.isWall()) return Collections.emptyList();
     
     Queue<Cell> queue = new LinkedList<>();
     Map<Cell, Cell> parentMap = new HashMap<>();
     Set<Cell> visited = new HashSet<>();

     queue.add(start);
     visited.add(start);
     parentMap.put(start, null);
     
     try {
         while (!queue.isEmpty()) {
             Cell current = queue.poll();
             
             if (!current.isStart() && !current.isEnd()) {
                 engine.animateVisit(current);
             }

             if (current.equals(end)) {
                 return reconstructPath(start, end, parentMap);
             }

             List<Cell> neighbors = adjacencyList.getOrDefault(current, Collections.emptyList());
             for (Cell neighbor : neighbors) {
                 if (!visited.contains(neighbor)) {
                     visited.add(neighbor);
                     parentMap.put(neighbor, current);
                     queue.add(neighbor);
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