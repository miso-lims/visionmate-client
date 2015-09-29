package visionmate;

/**
 * This class encapsulates the server configuration options that affect the client. It is imperative that the settings provided to 
 * a VisionMateClient match the actual settings on the server. If they do not, the VisionMateClient's behaviour is unspecified. 
 * Depending on the discrepencies, likely symptoms include timeouts, failed requests, and misrepresented data (e.g. "No Tube" may not 
 * be recognized as a missing tube, instead being understood as a barcode String).
 * 
 * Note that the default constructor sets all options to their default values EXCEPT the Suffix Character, which is set to '#' rather than 
 * the server's default of null. This suffix character is required for the VisionMateClient to detect end of line in the server's 
 * responses.
 * 
 * It is recommended to keep with all default settings EXCEPT for the suffix character, which must be set to something. '#' is an ideal 
 * suffix character, because the server will not use the character for anything else.
 */
public class ServerConfig {
  
  public static enum SortOrder {ROWS, COLUMNS};
  
  private String acknowledge = "OK";
  private Character prefixChar = null;
  private Character suffixChar = '#'; // Note: default is nothing, but we need something to tell us when we've reached EOL
  private String delimiter = ",";
  private SortOrder sortOrder = SortOrder.COLUMNS;
  
  private String noTubeLabel = "No Tube";
  private String noReadLabel = "No Read";
  
  private String getStatusCommand = "L";
  private String getDataCommand = "D";
  private String getLastErrorCommand = "E";
  private String getProductCommand = "C";
  private String setProductCommand = "P";
  private String resetStatusCommand = "Q";

  /**
   * Creates a new ServerConfig with all default server settings EXCEPT for the suffix character, which is set to '#'
   */
  public ServerConfig() {
    // Auto-generated constructor stub
  }
  
  /**
   * Combines the start of response pattern, which is {@code "^[prefixChar]<acknowledge>\??(<command>)?"}
   * 
   * @param command the command issued
   * @return the pattern that the response should begin with
   */
  public String getResponseStartPattern(String command) {
    StringBuilder sb = new StringBuilder("^");
    if (prefixChar != null) sb.append(prefixChar);
    if (acknowledge != null) sb.append(acknowledge);
    sb.append("\\??"); // for unknown command
    sb.append("(");
    sb.append(command);
    sb.append(")?");
    return sb.toString();
  }

  public String getAcknowledge() {
    return acknowledge;
  }

  public void setAcknowledge(String acknowledge) {
    this.acknowledge = acknowledge;
  }

  public Character getPrefixChar() {
    return prefixChar;
  }

  public void setPrefixChar(Character prefixChar) {
    this.prefixChar = prefixChar;
  }

  public Character getSuffixChar() {
    return suffixChar;
  }

  public void setSuffixChar(Character suffixChar) {
    if (suffixChar == null) throw new NullPointerException("Suffix character must not be null");
    this.suffixChar = suffixChar;
  }

  public String getDelimiter() {
    return delimiter;
  }

  public void setDelimiter(String delimiter) {
    if (delimiter == null) throw new NullPointerException("Delimiter must not be null");
    if (delimiter.isEmpty()) throw new IllegalArgumentException("Delimiter must not be empty");
    this.delimiter = delimiter;
  }

  public String getGetStatusCommand() {
    return getStatusCommand;
  }

  public void setGetStatusCommand(String command) {
    if (command == null) throw new NullPointerException("Command must not be null");
    if (command.isEmpty()) throw new IllegalArgumentException("Command must not be empty");
    this.getStatusCommand = command;
  }

  public String getGetDataCommand() {
    return getDataCommand;
  }

  public void setGetDataCommand(String command) {
    if (command == null) throw new NullPointerException("Command must not be null");
    if (command.isEmpty()) throw new IllegalArgumentException("Command must not be empty");
    this.getDataCommand = command;
  }

  public String getGetLastErrorCommand() {
    return getLastErrorCommand;
  }

  public void setGetLastErrorCommand(String command) {
    if (command == null) throw new NullPointerException("Command must not be null");
    if (command.isEmpty()) throw new IllegalArgumentException("Command must not be empty");
    this.getLastErrorCommand = command;
  }

  public String getGetProductCommand() {
    return getProductCommand;
  }

  public void setGetProductCommand(String command) {
    if (command == null) throw new NullPointerException("Command must not be null");
    if (command.isEmpty()) throw new IllegalArgumentException("Command must not be empty");
    this.getProductCommand = command;
  }

  public String getSetProductCommand(RackType product) {
    if (product == null) throw new NullPointerException("Product must not be null");
    return setProductCommand + product.getStringRepresentation();
  }

  public void setSetProductCommand(String command) {
    if (command == null) throw new NullPointerException("Command must not be null");
    if (command.isEmpty()) throw new IllegalArgumentException("Command must not be empty");
    this.setProductCommand = command;
  }

  public String getResetStatusCommand() {
    return resetStatusCommand;
  }

  public void setResetStatusCommand(String command) {
    if (command == null) throw new NullPointerException("Command must not be null");
    if (command.isEmpty()) throw new IllegalArgumentException("Command must not be empty");
    this.resetStatusCommand = command;
  }

  public String getNoTubeLabel() {
    return noTubeLabel;
  }

  public void setNoTubeLabel(String noTubeLabel) {
    if (noTubeLabel == null) throw new NullPointerException("Label must not be null");
    if (noTubeLabel.equals(this.noReadLabel)) throw new IllegalArgumentException("No Read and No Tube conditions cannot share the " +
        "same label");
    this.noTubeLabel = noTubeLabel;
  }

  public String getNoReadLabel() {
    return noReadLabel;
  }

  public void setNoReadLabel(String noReadLabel) {
    if (noReadLabel == null) throw new NullPointerException("Label must not be null");
    if (noReadLabel.equals(this.noTubeLabel)) throw new IllegalArgumentException("No Read and No Tube conditions cannot share the " +
        "same label");
    this.noReadLabel = noReadLabel;
  }

  public SortOrder getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(SortOrder sortOrder) {
    if (sortOrder == null) throw new NullPointerException("Sort order cannot be null");
    this.sortOrder = sortOrder;
  }

}
