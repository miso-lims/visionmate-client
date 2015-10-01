package visionmate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class MockScannerServer implements Runnable {
  
  private final Logger log = Logger.getLogger(this.getClass());
  
  private static final int defaultPort = 8000;
  
  private ServerConfig config = new ServerConfig();
  private final int port;
  
  private RackType currentProduct = new RackType("M0812");
  private ScanStatus currentStatus = new ScanStatus(33); // initialized, rack96
  private Scan currentData = null;
  
  public MockScannerServer() {
    this.port = defaultPort;
  }
  
  public MockScannerServer(int port) {
    this.port = port;
  }
  
  @Override
  public void run() {
    log.info("Mock server started");
    try (
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = serverSocket.accept();
        
        PrintStream output = new PrintStream(clientSocket.getOutputStream());
        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    ) {
      String request, response;
      
      while ((request = input.readLine()) != null) {
        try {
          response = handleRequest(request);
          output.print(response);
          output.flush();
        }
        catch (IllegalStateException e) {
          // real server just doesn't respond to this.
          log.warn("No response due to illegal state", e);
        }
      }
      log.info("Mock server stopped");
    } catch (IOException e) {
      log.error("Mock server crashed due to I/O failure", e);
    }
  }

  public ServerConfig getConfig() {
    return config;
  }

  public void setConfig(ServerConfig config) {
    this.config = config;
  }
  
  public RackType getCurrentProduct() {
    return currentProduct;
  }
  
  public void setCurrentProduct(RackType product) {
    this.currentProduct = product;
    resetStatus();
  }
  
  public void emulateScan(String[] barcodes) {
    if (barcodes.length != currentProduct.getRows() * currentProduct.getColumns()) {
      throw new IllegalArgumentException("Number of barcodes must match number of positions in current product");
    }
    StringBuilder sb = new StringBuilder();
    for (String barcode : barcodes) {
      sb.append(barcode).append(config.getDelimiter());
    }
    currentData = new Scan(currentProduct, sb.toString(), config);
    if (barcodes.length == 96) {
      currentStatus = new ScanStatus(45); // rack96, data ready, finished scan, initialized 
    }
    else {
      currentStatus = new ScanStatus(13); // data ready, finished scan, initialized
    }
  }
  
  public void clearData() {
    currentData = null;
    resetStatus();
  }
  
  private String handleRequest(String request) {
    String response = processCommand(request);
    return decorateResponse(request, response);
  }
  
  /**
   * @param request the command issued
   * @return the appropriate response, an empty string if the command has no response, or null if the command is not valid
   */
  private String processCommand(String request) {
    if (request.startsWith(config.getGetStatusCommand())) {
      return "" + currentStatus.toInt();
    }
    else if (request.startsWith(config.getGetProductCommand())) {
      return currentProduct.getStringRepresentation();
    }
    else if (request.startsWith(config.getSetProductCommand())) {
      String setting = request.replaceFirst(config.getSetProductCommand(), "");
      currentProduct = new RackType(setting);
      return "";
    }
    else if (request.startsWith(config.getResetStatusCommand())) {
      resetStatus();
      return "";
    }
    else if (request.startsWith(config.getGetDataCommand())) {
      // Real server sets status as if data was available and retrieved no matter what
      if (currentProduct.getRows() * currentProduct.getColumns() == 96) {
        currentStatus = new ScanStatus(61); // rack96, data sent, data ready, finished scan, initialized
      }
      else {
        currentStatus = new ScanStatus(29); // data sent, data ready, finished scan, initialized
      }
      if (currentData == null) throw new IllegalStateException("No data available"); // real server sends no response at all in this state
      else return getCurrentDataString();
    }
    return null;
  }
  
  private void resetStatus() {
    if (currentProduct.getRows() * currentProduct.getColumns() == 96) currentStatus = new ScanStatus(33); // rack96, initialized
    else currentStatus = new ScanStatus(1); // initialized
  }
  
  private String getCurrentDataString() {
    StringBuilder sb = new StringBuilder();
    String[][] barcodes = currentData.getBarcodes();
    
    switch (config.getSortOrder()) {
    case ROWS:
      for (int row = 0; row < barcodes.length; row++) {
        for (int col = 0; col < barcodes[row].length; col++) {
          sb.append(barcodes[row][col]).append(config.getDelimiter());
        }
      }
      break;
    case COLUMNS:
      for (int col = 0; col < barcodes[0].length; col++) {
        for (int row = 0; row < barcodes.length; row++) {
          sb.append(barcodes[row][col]).append(config.getDelimiter());
        }
      }
      break;
    }
    
    return sb.toString();
  }
  
  private String decorateResponse(String request, String response) {
    StringBuilder sb = new StringBuilder();
    if (config.getPrefixChar() != null) sb.append(config.getPrefixChar());
    sb.append(config.getAcknowledge());
    if (response == null) {
      sb.append("?");
      sb.append(request);
    }
    else {
      sb.append(request);
      sb.append(response);
    }
    if (config.getSuffixChar() != null) sb.append(config.getSuffixChar());
    return sb.toString();
  }

}
