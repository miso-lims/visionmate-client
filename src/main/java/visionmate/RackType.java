package visionmate;

public class RackType {
  
  public static enum Manufacturer {ABGENE, MATRIX, NUNC};
  
  private final Manufacturer manufacturer;
  private final int rows;
  private final int columns;
  
  public RackType(String productString) {
    if (productString == null) throw new NullPointerException("Product string must not be null");
    if (!productString.matches("^[AMN]\\d{4}$")) throw new IllegalArgumentException("Invalid product string");
    
    switch (productString.charAt(0)) {
    case 'A':
      this.manufacturer = Manufacturer.ABGENE;
      break;
    case 'M':
      this.manufacturer = Manufacturer.MATRIX;
      break;
    case 'N':
      this.manufacturer = Manufacturer.NUNC;
      break;
    default:
      throw new IllegalArgumentException("Invalid manufacturer initial. Must be A, M, or N");
    }
    
    this.rows = Integer.parseInt(productString.substring(1, 3));
    this.columns = Integer.parseInt(productString.substring(3, 5));
    if (rows < 1 || rows > 24 || columns < 0 || columns > 24) throw new IllegalArgumentException(rows + " rows or " + columns + 
        " columns is invalid. Both must be between 1 and 24");
  }
  
  public RackType(Manufacturer manufacturer, int rows, int columns) {
    if (manufacturer == null) throw new NullPointerException("Manufacturer must not be null");
    if (rows < 1 || rows > 24 || columns < 0 || columns > 24) throw new IllegalArgumentException(rows + " rows or " + columns + 
        " columns is invalid. Both must be between 1 and 24");
    this.manufacturer = manufacturer;
    this.rows = rows;
    this.columns = columns;
  }
  
  @Override
  public String toString() {
    return manufacturer.toString() + "-type, " + rows + " x " + columns;
  }

  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public int getRows() {
    return rows;
  }

  public int getColumns() {
    return columns;
  }
  
  public String getStringRepresentation() {
    StringBuilder sb = new StringBuilder();
    switch (manufacturer) {
    case ABGENE:
      sb.append('A');
      break;
    case MATRIX:
      sb.append('M');
      break;
    case NUNC:
      sb.append('N');
      break;
    }
    if (rows < 10) sb.append("0");
    sb.append(rows);
    if (columns < 10) sb.append("0");
    sb.append(columns);
    return sb.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + columns;
    result = prime * result
        + ((manufacturer == null) ? 0 : manufacturer.hashCode());
    result = prime * result + rows;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RackType other = (RackType) obj;
    if (columns != other.columns)
      return false;
    if (manufacturer != other.manufacturer)
      return false;
    if (rows != other.rows)
      return false;
    return true;
  }

}
