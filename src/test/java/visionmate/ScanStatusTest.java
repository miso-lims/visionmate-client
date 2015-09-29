package visionmate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScanStatusTest {

  @Test
  public void testAllBitsSet() {
    ScanStatus status = new ScanStatus((byte) Integer.parseInt("255"));
    assertTrue(status.isInitialized());
    assertTrue(status.isScanning());
    assertTrue(status.isFinishedScan());
    assertTrue(status.isDataReady());
    assertTrue(status.isDataSent());
    assertTrue(status.isRack96());
    assertTrue(status.isError());
  }
  
  @Test
  public void testNoBitsSet() {
    ScanStatus status = new ScanStatus((byte) Integer.parseInt("0"));
    assertFalse(status.isInitialized());
    assertFalse(status.isScanning());
    assertFalse(status.isFinishedScan());
    assertFalse(status.isDataReady());
    assertFalse(status.isDataSent());
    assertFalse(status.isRack96());
    assertFalse(status.isError());
  }
  
  @Test
  public void testResetStatus() {
    ScanStatus status = new ScanStatus((byte) Integer.parseInt("33"));
    assertTrue(status.isInitialized());
    assertFalse(status.isScanning());
    assertFalse(status.isFinishedScan());
    assertFalse(status.isDataReady());
    assertFalse(status.isDataSent());
    assertTrue(status.isRack96());
    assertFalse(status.isError());
  }

}
