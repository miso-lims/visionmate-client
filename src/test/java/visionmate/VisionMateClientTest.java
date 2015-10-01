package visionmate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import visionmate.RackType.Manufacturer;

public class VisionMateClientTest {

  private static MockScannerServer server;
  private static VisionMateClient client;
  
  @BeforeClass
  public static void setup() throws UnknownHostException {
    server = new MockScannerServer();
    new Thread(server).start();
    
    client = new VisionMateClient("127.0.0.1", 8000);
    try {
      client.connect();
    } catch (IOException e) {
      throw new IllegalStateException("Failed to connect");
    }
  }
  
  @Test
  public void testGetStatus() throws SocketTimeoutException, IOException {
    ScanStatus status = client.getStatus();
    assertNotNull(status);
  }
  
  @Test
  public void testGetProduct() throws SocketTimeoutException, IOException {
    RackType product = client.getCurrentProduct();
    assertNotNull(product);
  }
  
  @Test
  public void testSetProduct() throws SocketTimeoutException, IOException {
    RackType product = new RackType(Manufacturer.ABGENE, 4, 6);
    assertTrue(client.setCurrentProduct(product));
    RackType resultingProduct = client.getCurrentProduct();
    assertEquals(product, resultingProduct);
  }
  
  @Test
  public void testResetStatus() throws SocketTimeoutException, IOException {
    assertTrue(client.resetStatus());
    ScanStatus status = client.getStatus();
    assertTrue(status.isInitialized());
    assertFalse(status.isScanning());
    assertFalse(status.isFinishedScan());
    assertFalse(status.isDataReady());
    assertFalse(status.isDataSent());
    assertFalse(status.isError());
  }
  
  @Test
  public void testGetScanNoData() throws SocketTimeoutException, IOException, ScannerException {
    server.clearData();
    assertNull(client.getScan());
  }
  
  @Test
  public void testGetScan() throws SocketTimeoutException, IOException, ScannerException {
    server.setCurrentProduct(new RackType("M0202"));
    server.emulateScan(new String[] {"0188137823","0188137800","0188137799","0188137776"});
    Scan scan = client.getScan();
    assertNotNull(scan);
    assertEquals(scan.getBarcode('A', 1), "0188137823");
    assertEquals(scan.getBarcode(2, 2), "0188137776");
    assertTrue(scan.isFull());
  }
  
  @Test
  public void testWaitAndNoScan() throws SocketTimeoutException, IOException, ScannerException {
    server.clearData();
    Scan scan = client.waitForScan(1);
    assertNull(scan);
  }
  
  @Test
  public void testWaitForScan() throws SocketTimeoutException, IOException, ScannerException {
    server.clearData();
    server.setCurrentProduct(new RackType("M0202"));
    
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        server.emulateScan(new String[] {"1111111111","0188137800","0188137799","0188137776"});
      }
    }).start();
    
    Scan scan = client.waitForScan(4000);
    assertNotNull(scan);
    assertEquals("1111111111", scan.getBarcode('A', 1));
  }
  
  @Test
  public void testPrepareAndWaitForNoScan() throws SocketTimeoutException, IOException, ScannerException {
    server.setCurrentProduct(new RackType("M0202"));
    server.emulateScan(new String[] {"1111111111","0188137800","0188137799","0188137776"});
    Scan scan = client.prepareAndWaitForScan(1);
    assertNull(scan);
  }
  
  @Test
  public void testPrepareAndWaitForScan() throws SocketTimeoutException, IOException, ScannerException {
    server.setCurrentProduct(new RackType("M0202"));
    server.emulateScan(new String[] {"1111111111","0188137800","0188137799","0188137776"});
    
    new Thread(new Runnable() {
      @Override
      public void run() {
        // Simulate user scanning a rack after a 2-second wait
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        server.emulateScan(new String[] {"2222222222","0188137800","0188137799","0188137776"});
      }
    }).start();
    
    Scan scan = client.prepareAndWaitForScan(4000);
    assertNotNull(scan);
    assertEquals("2222222222", scan.getBarcode('A', 1));
  }
  
  @AfterClass
  public static void cleanup() {
    client.close();
  }

}
