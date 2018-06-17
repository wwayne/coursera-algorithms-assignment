import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
  private int [][] p;
  private int width;
  private int height;
  
  public SeamCarver(Picture picture) {
    width = picture.width();
    height = picture.height();
    p = new int[width][height];
    for (int col = 0; col < width; col++)
      for (int row = 0; row < height; row++)
        p[col][row] = picture.getRGB(col, row);
  }
  
  public Picture picture() {
    Picture picture = new Picture(width, height);
    for (int col = 0; col < width; col++)
      for (int row = 0; row < height; row++)
        picture.setRGB(col, row, p[col][row]);
    return picture;
  }
  
  public int width() {
    return width;
  }
  
  public int height() {
    return height;
  }
  
  public double energy(int column, int row) {
    if (row < 0 || row >= height() || column < 0 || column >= width()) throw new IllegalArgumentException();
    if (row == 0 || row == height() - 1 || column == 0 || column == width() - 1) return 1000.0;
    int rowLeft = p[column - 1][row];
    int rowRight = p[column + 1][row];
    int columnTop = p[column][row - 1];
    int columnBottom = p[column][row + 1];
    return Math.sqrt(differColor(rowLeft, rowRight) + differColor(columnTop, columnBottom));
  }
  
  private double differColor(int a, int b) {
    double redDiff = Math.pow(((a >> 16) & 0xFF) - ((b >> 16) & 0xFF), 2);
    double greenDiff = Math.pow(((a >>  8) & 0xFF) - ((b >>  8) & 0xFF), 2);
    double blueDiff = Math.pow(((a >>  0) & 0xFF) - ((b >>  0) & 0xFF), 2);
    return redDiff + greenDiff + blueDiff;
  }
  
  public int[] findHorizontalSeam() {
    transpose(true);
    int[] rowArray = findVerticalSeam();
    transpose(false);
    
    for (int i = 0; i < rowArray.length; i++)
      rowArray[i] = height() - 1 - rowArray[i];
    return rowArray;
  }
  
  public int[] findVerticalSeam() {
    int[][] pointTo = new int[height()][width()];
    double [][] distTo = new double[height()][width()];
    
    for (int row = 0; row < height(); row++) {
      for (int col = 0; col < width(); col++) {
        double e = energy(col, row);
        for (int i = -1; i <= 1; i++) {
          if (row - 1 < 0 || col + i < 0 || col + i >= width()) continue;
          if (distTo[row][col] == 0.0) {
            distTo[row][col] = e + distTo[row - 1][col + i];
            pointTo[row][col] = col + i;
            continue;
          }
          if (e + distTo[row - 1][col + i] < distTo[row][col]) {
            distTo[row][col] = e + distTo[row - 1][col + i];
            pointTo[row][col] = col + i;
          }
        }
      }
    }

    int minCol = 0;
    double minDist = Double.POSITIVE_INFINITY;
    for (int col = 0; col < width(); col++) {
      if (distTo[height() - 1][col] < minDist) {
        minDist = distTo[height() - 1][col];
        minCol = col;
      }
    }
    
    int[] colArray = new int[height()];
    for (int row = height() - 1; row >= 0; row--) {
      colArray[row] = minCol;
      minCol = pointTo[row][minCol];
    }

    return colArray;
  }
  
  private void transpose(boolean isClockwise) {
    int[][] newP = new int[height][width];
    if (!isClockwise) {
      for (int col = 0; col < width; col++)
        for (int row = 0; row < height; row++)
          newP[row][width - 1 - col] = p[col][row];
    } else {
      for (int col = 0; col < width; col++)
        for (int row = 0; row < height; row++)
          newP[height() - 1 - row][col] = p[col][row];
    }
    
    p = newP;
    int tmpW = width;
    width = height;
    height = tmpW;
  }
  
  public void removeHorizontalSeam(int[] seam) {
    if (seam == null) throw new IllegalArgumentException();
    transpose(true);
    int[] newSeam = new int[seam.length];
    for (int i = 0; i < seam.length; i++)
      newSeam[i] = width() - 1 - seam[i];
    removeVerticalSeam(newSeam);
    transpose(false);
  }
  
  public void removeVerticalSeam(int[] seam) {
    if (seam == null) throw new IllegalArgumentException();
    for (int row = 0; row < height; row++) {
      int col = seam[row];
      while (col < width() - 1) {
        p[col][row] = p[col + 1][row];
        col = col + 1;
      }
    }
    width--;
  }
}
