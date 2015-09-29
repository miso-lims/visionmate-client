package visionmate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import visionmate.RackType.Manufacturer;

public class VisionMateClientTest {
  
  private static final String defaultServerIp = "10.0.28.57";
  private static final int defaultServerPort = 8000;
  
  private static VisionMateClient scanner;
  
  @BeforeClass
  public static void setup() throws UnknownHostException {
    String hostArg = System.getProperty("host");
    String portArgString = System.getProperty("port");
    if (hostArg == null) {
      scanner = new VisionMateClient(defaultServerIp, defaultServerPort);
    }
    else {
      scanner = new VisionMateClient(hostArg, portArgString == null ? defaultServerPort : Integer.parseInt(portArgString));
    }
    try {
      scanner.connect();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to connect");
    }
  }
  
  @Test
  public void testStatus() throws IOException {
    ScanStatus status = scanner.getStatus();
    assertTrue(status.isInitialized());
    assertFalse(status.isError());
  }
  
  @Test
  public void testGetProduct() throws IOException {
    RackType product = scanner.getCurrentProduct();
    assertNotNull(product);
  }
  
  @Test
  public void testSetProduct() throws IOException {
    RackType product = new RackType(Manufacturer.MATRIX, 8, 12);
    assertTrue(scanner.setCurrentProduct(product));
    assertEquals(product, scanner.getCurrentProduct());
  }
  
  @Test
  public void testStatusReset() throws IOException, ScannerException {
    scanner.getScan(); // Sets data ready bit (and others)
    assertTrue(scanner.resetStatus());
  }
  
  @Test
  public void testNullScan() throws IOException, ScannerException {
    // If status indicates no data, getScan should return null
    scanner.resetStatus();
    Scan scan = scanner.getScan();
    assertNull(scan);
  }
  
  @Test
  public void testWaitForScan() throws IOException, ScannerException {
    // Unless someone scans a plate while this is running, it should return null
    scanner.resetStatus();
    assertNull(scanner.waitForScan(1));
  }
  
  @Test
  public void testPrepareAndWaitForScan() throws IOException, ScannerException {
  // Unless someone scans a plate while this is running, it should return null
    assertNull(scanner.prepareAndWaitForScan(1));
  }
  
  @AfterClass
  public static void cleanUp() throws Exception {
    scanner.close();
  }

}
