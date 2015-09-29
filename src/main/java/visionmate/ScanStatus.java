package visionmate;

public class ScanStatus {
  
  private final boolean initialized;
  private final boolean scanning;
  private final boolean finishedScan;
  private final boolean dataReady;
  private final boolean dataSent;
  private final boolean rack96;
  private final boolean error;
  
  public ScanStatus(byte status) {
    this.initialized = getBit(status, 0);
    this.scanning = getBit(status, 1);
    this.finishedScan = getBit(status, 2);
    this.dataReady = getBit(status, 3);
    this.dataSent = getBit(status, 4);
    this.rack96 = getBit(status, 5);
    this.error = getBit(status, 7);
  }
  
  private boolean getBit(byte source, int position) {
    if (position < 0 || position > 7) throw new IllegalArgumentException("Bit position " + position + "is out of invalid. "
        + "Must be between 0 and 7 inclusive");
    return ((source >> position) & 0x01) == 1;
  }
  
  @Override
  public String toString() {
    return "Scan status: initialized=" + initialized + ", scanning=" + scanning + ", finishedScan=" + finishedScan +
        ", dataReady=" + dataReady + ", dataSent=" + dataSent + ", rack96=" + rack96 + ", error=" + error;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public boolean isScanning() {
    return scanning;
  }

  public boolean isFinishedScan() {
    return finishedScan;
  }

  public boolean isDataReady() {
    return dataReady;
  }

  public boolean isDataSent() {
    return dataSent;
  }

  public boolean isRack96() {
    return rack96;
  }

  public boolean isError() {
    return error;
  }

}
