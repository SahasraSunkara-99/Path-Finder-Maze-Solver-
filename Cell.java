// src/Cell.java
import java.awt.Color;

public class Cell {
    public final int row;
    public final int col;
    private boolean isWall;
    private boolean isStart;
    private boolean isEnd;
    private Color color; 

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isWall = false;
        this.isStart = false;
        this.isEnd = false;
        this.color = Color.WHITE;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
        this.color = isWall ? Color.BLACK : Color.WHITE;
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
        this.color = isStart ? Color.BLUE : Color.WHITE;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        this.color = isEnd ? Color.RED : Color.WHITE;
    }

    public boolean isWall() { return isWall; }
    public boolean isStart() { return isStart; }
    public boolean isEnd() { return isEnd; }
    public Color getColor() { return color; }

    public void setColor(Color color) {
        // Prevent overwriting permanent markers unless explicitly animating Start/End
        if (!isStart && !isEnd && !isWall) {
            this.color = color;
        } else if (isStart || isEnd) {
             // Allow temporary animation on start/end nodes
             this.color = color;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}

