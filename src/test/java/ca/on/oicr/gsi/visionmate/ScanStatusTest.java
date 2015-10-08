package ca.on.oicr.gsi.visionmate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.on.oicr.gsi.visionmate.ScanStatus;

public class ScanStatusTest {

  @Test
  public void testAllBitsSet() {
    ScanStatus status = new ScanStatus(Integer.parseInt("255"));
    assertTrue(status.isInitialized());
    assertTrue(status.isScanning());
    assertTrue(status.isFinishedScan());
    assertTrue(status.isDataReady());
    assertTrue(status.isDataSent());
    assertTrue(status.isRack96());
    assertTrue(status.isError());
    assertTrue(status.toInt() == 255 || status.toInt() == 191); // bit 6 (64) is unused, so tracking doesn't matter
    assertNotNull(status.toString());
  }
  
  @Test
  public void testNoBitsSet() {
    ScanStatus status = new ScanStatus(Integer.parseInt("0"));
    assertFalse(status.isInitialized());
    assertFalse(status.isScanning());
    assertFalse(status.isFinishedScan());
    assertFalse(status.isDataReady());
    assertFalse(status.isDataSent());
    assertFalse(status.isRack96());
    assertFalse(status.isError());
    assertEquals(0, status.toInt());
    assertNotNull(status.toString());
  }
  
  @Test
  public void testResetStatus() {
    ScanStatus status = new ScanStatus(Integer.parseInt("33"));
    assertTrue(status.isInitialized());
    assertFalse(status.isScanning());
    assertFalse(status.isFinishedScan());
    assertFalse(status.isDataReady());
    assertFalse(status.isDataSent());
    assertTrue(status.isRack96());
    assertFalse(status.isError());
    assertEquals(33, status.toInt());
    assertNotNull(status.toString());
  }

}
