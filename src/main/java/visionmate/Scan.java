package visionmate;

public class Scan {
  
  private final String[][] barcodes;
  private final String noTube;
  private final String noRead;
  private final int failedReads;
  private final int emptyPositions;

  public Scan(RackType rack, String data, ServerConfig config) {
    if (rack == null) throw new NullPointerException("Rack type cannot be null");
    if (data == null) throw new NullPointerException("Data cannot be null");
    if (data.isEmpty()) throw new IllegalArgumentException("Data cannot be empty");
    if (config == null) throw new NullPointerException("Server config cannot be null");
    
    int rows = rack.getRows();
    int columns = rack.getColumns();
    int expectedReads = rows*columns;
    
    String[] list = data.split(config.getDelimiter());
    if (list.length != expectedReads) throw new IllegalArgumentException("Expected " + expectedReads + " reads for this rack type, but " +
        "only " + list.length + " barcodes were found in the data");
    this.barcodes = new String[rows][columns];
    
    this.noTube = config.getNoTubeLabel();
    this.noRead = config.getNoReadLabel();
    int noTubes = 0;
    int noReads = 0;
    
    switch (config.getSortOrder()) {
    case COLUMNS:
      for (int col = 0, i = 0; col < columns; col++) {
        for (int row = 0; row < rows; row++, i++) {
          this.barcodes[row][col] = list[i];
          if (noRead.equals(list[i])) noReads++;
          if (noTube.equals(list[i])) noTubes++;
        }
      }
      break;
    case ROWS:
      for (int row = 0, i = 0; row < rows; row++) {
        for (int col = 0; row < columns; col++, i++) {
          this.barcodes[row][col] = list[i];
          if (noRead.equals(list[i])) noReads++;
          if (noTube.equals(list[i])) noTubes++;
        }
      }
      break;
    }
    
    this.failedReads = noReads;
    this.emptyPositions = noTubes;
  }

  public String getNoTubeLabel() {
    return noTube;
  }

  public String getNoReadLabel() {
    return noRead;
  }
  
  public String[][] getBarcodes() {
    String[][] copy = new String[barcodes.length][];
    for (int i = 0; i < barcodes.length; i++) {
      copy[i] = barcodes[i].clone();
    }
    return copy;
  }
  
  public String getBarcode(int row, int column) {
    return barcodes[row-1][column-1];
  }
  
  public String getBarcode(char row, int column) {
    return barcodes[getNumberForChar(row)-1][column-1];
  }
  
  public int getNoReadCount() {
    return failedReads;
  }
  
  public int getNoTubeCount() {
    return emptyPositions;
  }
  
  public boolean isFull() {
    return emptyPositions == 0;
  }
  
  public boolean isEmpty() {
    if (barcodes == null || barcodes.length == 0 || barcodes[0] == null || barcodes[0].length == 0) {
      throw new IllegalStateException("Barcodes array is not correctly populated");
    }
    return emptyPositions == barcodes.length * barcodes[0].length;
  }
  
  public static char getCharForNumber(int num) {
    if (num < 1 || num > 24) throw new IllegalArgumentException("Row number must be between 1 and 24");
    return (char) ( num + 'A' - 1);
  }
  
  public static int getNumberForChar(char letter) {
    if (letter >= 'a' && letter <= 'z') letter = Character.toUpperCase(letter);
    if (letter < 'A' || letter >= 'Z') throw new IllegalArgumentException("Row letter must be between A and Z");
    return letter - 'A' + 1;
  }

}
