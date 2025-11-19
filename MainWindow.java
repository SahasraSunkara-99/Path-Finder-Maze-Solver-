import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MainWindow extends JFrame {
 
 private MazeGridPanel mazePanel;
 private GraphBuilder graphBuilder;
 private JLabel statusLabel;
 private JButton bfsButton, dfsButton, startButton, endButton, wallButton, clearButton;

 public MainWindow() {
     super("🔥 Path Finder Game (Maze Solver) 5AD,5AV,5BH");
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
     mazePanel = new MazeGridPanel();
     graphBuilder = new GraphBuilder(mazePanel.getRows(), mazePanel.getCols());

     initUI();
     pack();
     setLocationRelativeTo(null);
     setVisible(true);
 }

 private void initUI() {
     setLayout(new BorderLayout());

     JPanel controlPanel = new JPanel();
     controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

     startButton = new JButton("Set Start (S)");
     endButton = new JButton("Set End (E)");
     wallButton = new JButton("Draw Walls (W)");
     clearButton = new JButton("Clear Maze");
     bfsButton = new JButton("▶ BFS Solve");
     dfsButton = new JButton("▶ DFS Solve");

     wallButton.setBackground(Color.LIGHT_GRAY); 

     controlPanel.add(startButton);
     controlPanel.add(endButton);
     controlPanel.add(wallButton);
     controlPanel.add(clearButton);
     controlPanel.add(new JSeparator(SwingConstants.VERTICAL));
     controlPanel.add(bfsButton);
     controlPanel.add(dfsButton);
     
     statusLabel = new JLabel("Status: Ready. Click 'Draw Walls' and then the grid.", SwingConstants.CENTER);
     statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

     add(controlPanel, BorderLayout.NORTH);
     add(mazePanel, BorderLayout.CENTER);
     add(statusLabel, BorderLayout.SOUTH);

     attachListeners();
 }
 
 public class AnimationEngine extends SwingWorker<List<Cell>, Cell> {
     private final PathFinder pathFinder;
     private final boolean isBFS;
     
     private final int VISITED_DELAY_MS = 60; 
     private final int PATH_DELAY_MS = 40;

     public AnimationEngine(PathFinder pathFinder, boolean isBFS) {
    	    this.pathFinder = pathFinder;
    	    this.isBFS = isBFS;
    	   
    	    mazePanel.clearPathHighlights();

    	    setSolverButtonsEnabled(false);
    	    statusLabel.setText("Status: " + (isBFS ? "BFS" : "DFS") + " running...");
    	}


     public void animateVisit(Cell cell) throws InterruptedException {
         publish(cell);
         TimeUnit.MILLISECONDS.sleep(VISITED_DELAY_MS);
     }

     @Override
     protected List<Cell> doInBackground() {
         Cell start = mazePanel.getStartCell();
         Cell end = mazePanel.getEndCell();
         
         if (start == null || end == null) return null;

         HashMap<Cell, List<Cell>> adjList = graphBuilder.buildGraph(mazePanel.getGrid());
         return pathFinder.findPath(start, end, adjList, this);
     }

     @Override
     protected void process(List<Cell> chunks) {
         for (Cell cell : chunks) {
             // Highlight visited cells (Yellow)
             if (!cell.isStart() && !cell.isEnd()) {
                 cell.setColor(Color.YELLOW);
             }
             mazePanel.repaint();
         }
     }
     @Override
     protected void done() {
         try {
             List<Cell> path = get();
             if (path == null) {
                 statusLabel.setText("Status: Please set both a Start (S) and End (E) point.");
             } else if (path.isEmpty()) {
                 statusLabel.setText("Status: No path found! Maze is blocked.");
             } else {
                 animatePath(path);
                 statusLabel.setText("Status: Path found! Total steps: " + (path.size() - 1) + (isBFS ? " (Optimal - BFS)" : " (Depth-First - DFS)"));
             }
         } catch (Exception e) {
             statusLabel.setText("Status: Error during pathfinding. Check console.");
             e.printStackTrace();
         } finally {
             setSolverButtonsEnabled(true);
         }
     }

     private void animatePath(List<Cell> path) throws InterruptedException {
         for (Cell cell : path) {
             if (!cell.isStart() && !cell.isEnd()) {
                 cell.setColor(Color.GREEN);
                 mazePanel.repaint();
                 TimeUnit.MILLISECONDS.sleep(PATH_DELAY_MS);
             }
         }
         mazePanel.repaint();
     }
 }
 
 private void attachListeners() {
    
     startButton.addActionListener(e -> setMode("Start", startButton));
     endButton.addActionListener(e -> setMode("End", endButton));
     wallButton.addActionListener(e -> setMode("Wall", wallButton));

     clearButton.addActionListener(e -> {
         mazePanel.clearMaze();
         statusLabel.setText("Status: Maze cleared. Ready.");
     });

     bfsButton.addActionListener(e -> new AnimationEngine(new BFSPathFinder(), true).execute());
     
     dfsButton.addActionListener(e -> new AnimationEngine(new DFSPathFinder(), false).execute());
 }
 
 private void setMode(String mode, JButton selectedButton) {
     mazePanel.setMode(mode);

     startButton.setBackground(UIManager.getColor("Button.background"));
     endButton.setBackground(UIManager.getColor("Button.background"));
     wallButton.setBackground(UIManager.getColor("Button.background"));
    
     selectedButton.setBackground(Color.LIGHT_GRAY);
     statusLabel.setText("Status: Ready to set *" + mode.toUpperCase() + "* cells.");
 }
 
 private void setSolverButtonsEnabled(boolean enabled) {
     bfsButton.setEnabled(enabled);
     dfsButton.setEnabled(enabled);
     clearButton.setEnabled(enabled);
     startButton.setEnabled(enabled);
     endButton.setEnabled(enabled);
     wallButton.setEnabled(enabled);
 }

 public static void main(String[] args) {
     try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
     } catch (Exception e) {
         e.printStackTrace();
     }
     
     SwingUtilities.invokeLater(MainWindow::new);
 }

}
