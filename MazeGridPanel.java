import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MazeGridPanel extends JPanel {
    private final int ROWS = 20;
    private final int COLS = 20;
    private final int CELL_SIZE = 30;

    private Cell[][] grid;
    private String currentMode = "Wall"; 
    private Cell startCell = null;
    private Cell endCell = null;

    public MazeGridPanel() {
        setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        initializeGrid();
        attachListeners();
    }

    private void initializeGrid() {
        grid = new Cell[ROWS][COLS];

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = new Cell(r, c);
            }
        }
    }

    public Cell[][] getGrid() { return grid; }
    public Cell getStartCell() { return startCell; }
    public Cell getEndCell() { return endCell; }
    public int getRows() { return ROWS; }
    public int getCols() { return COLS; }

    public void setMode(String mode) { this.currentMode = mode; }

    public void clearMaze() {
        startCell = null;
        endCell = null;
        initializeGrid();
        repaint();
    }

    public void clearPathHighlights() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Cell cell = grid[r][c];

                if (!cell.isWall() && !cell.isStart() && !cell.isEnd()) {
                    cell.setColor(Color.WHITE);
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Cell cell = grid[r][c];
                int x = c * CELL_SIZE;
                int y = r * CELL_SIZE;

                g2d.setColor(cell.getColor());
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                // Grid line
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);

                // S / E text
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 18));

                if (cell.isStart()) {
                    g2d.drawString("S", x + CELL_SIZE / 4, y + 3 * CELL_SIZE / 4);
                } else if (cell.isEnd()) {
                    g2d.drawString("E", x + CELL_SIZE / 4, y + 3 * CELL_SIZE / 4);
                }
            }
        }
    }

    private void attachListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;

                if (row < 0 || col < 0 || row >= ROWS || col >= COLS) return;

                Cell clicked = grid[row][col];

                switch (currentMode) {
                    case "Start":
                        placeStartCell(clicked);
                        break;

                    case "End":
                        placeEndCell(clicked);
                        break;

                    case "Wall":
                        toggleWall(clicked);
                        break;
                }

                repaint();
            }
        });
    }

    private void placeStartCell(Cell clicked) {
        if (startCell != null) {
            startCell.setStart(false);
            startCell.setColor(Color.WHITE);
        }

        clicked.setStart(true);
        clicked.setWall(false);
        clicked.setColor(Color.GREEN);
        startCell = clicked;
    }

    private void placeEndCell(Cell clicked) {
        if (endCell != null) {
            endCell.setEnd(false);
            endCell.setColor(Color.WHITE);
        }

        clicked.setEnd(true);
        clicked.setWall(false);
        clicked.setColor(Color.RED);
        endCell = clicked;
    }

    private void toggleWall(Cell clicked) {
        if (clicked.isStart() || clicked.isEnd()) return;

        boolean newWallState = !clicked.isWall();
        clicked.setWall(newWallState);

        clicked.setColor(newWallState ? Color.BLACK : Color.WHITE);
    }

}
