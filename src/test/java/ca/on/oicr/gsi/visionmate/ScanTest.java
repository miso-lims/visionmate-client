package ca.on.oicr.gsi.visionmate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ca.on.oicr.gsi.visionmate.RackType;
import ca.on.oicr.gsi.visionmate.Scan;
import ca.on.oicr.gsi.visionmate.ServerConfig;

public class ScanTest {
  
  @Rule
  public final ExpectedException exception = ExpectedException.none();
  
  @Test
  public void testScan() {
    RackType rack = new RackType("M0812");
    ServerConfig config = new ServerConfig();
    String data = "0188137823,0188137800,0188137799,0188137776,0188137775,0188137752,0188137751,0188137728,0188137822,0188137801," +
        "0188137798,0188137777,0188137774,0188137753,0188137750,0188137729,0188137821,0188137802,0188137797,0188137778,0188137773," +
        "0188137754,0188137749,0188137730,0188137820,0188137803,0188137796,0188137779,0188137772,0188137755,0188137748,0188137731," +
        "0188137819,0188137804,0188137795,0188137780,0188137771,0188137756,0188137747,0188137732,0188137818,0188137805,0188137794," +
        "0188137781,0188137770,0188137757,0188137746,0188137733,0188137817,0188137806,0188137793,0188137782,0188137769,0188137758," + 
        "0188137745,0188137734,0188137816,0188137807,0188137792,0188137783,0188137768,0188137759,0188137744,0188137735,0188137815," +
        "0188137808,0188137791,0188137784,0188137767,0188137760,0188137743,0188137736,0188137814,0188137809,0188137790,0188137785," +
        "0188137766,0188137761,0188137742,0188137737,0188137813,0188137810,0188137789,0188137786,0188137765,0188137762,0188137741," +
        "0188137738,No Tube,0188137788,0188137812,0188137787,0188137764,0188137763,0188137740,0188137739,";
    Scan scan = new Scan(rack, data, config);
    assertEquals("0188137823", scan.getBarcode('A', 1));
    assertEquals("0188137799", scan.getBarcode(3, 1));
    assertEquals("0188137763", scan.getBarcode('F', 12));
    assertFalse(scan.isEmpty());
    assertFalse(scan.isFull());
    assertTrue(scan.getNoReadCount() == 0);
  }
  
  @Test
  public void testFullScan() {
    RackType rack = new RackType("M0812");
    ServerConfig config = new ServerConfig();
    String data = "0188137823,0188137800,0188137799,0188137776,0188137775,0188137752,0188137751,0188137728,0188137822,0188137801," +
        "0188137798,0188137777,0188137774,0188137753,0188137750,0188137729,0188137821,0188137802,0188137797,0188137778,0188137773," +
        "0188137754,0188137749,0188137730,0188137820,0188137803,0188137796,0188137779,0188137772,0188137755,0188137748,0188137731," +
        "0188137819,0188137804,0188137795,0188137780,0188137771,0188137756,0188137747,0188137732,0188137818,0188137805,0188137794," +
        "0188137781,0188137770,0188137757,0188137746,0188137733,0188137817,0188137806,0188137793,0188137782,0188137769,0188137758," + 
        "0188137745,0188137734,0188137816,0188137807,0188137792,0188137783,0188137768,0188137759,0188137744,0188137735,0188137815," +
        "0188137808,0188137791,0188137784,0188137767,0188137760,0188137743,0188137736,0188137814,0188137809,0188137790,0188137785," +
        "0188137766,0188137761,0188137742,0188137737,0188137813,0188137810,0188137789,0188137786,0188137765,0188137762,0188137741," +
        "0188137738,0188137001,0188137788,0188137812,0188137787,0188137764,0188137763,0188137740,0188137739,";
    Scan scan = new Scan(rack, data, config);
    assertEquals("0188137823", scan.getBarcode('A', 1));
    assertEquals("0188137799", scan.getBarcode(3, 1));
    assertEquals("0188137763", scan.getBarcode('F', 12));
    assertFalse(scan.isEmpty());
    assertTrue(scan.isFull());
    assertTrue(scan.getNoReadCount() == 0);
  }
  
  @Test
  public void testEmptyScan() {
    RackType rack = new RackType("M0812");
    ServerConfig config = new ServerConfig();
    String data = "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube," +
        "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube," +
        "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube," +
        "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube," +
        "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube," +
        "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube," +
        "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube," +
        "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube," +
        "No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,No Tube,";
    Scan scan = new Scan(rack, data, config);
    assertTrue(scan.isEmpty());
    assertFalse(scan.isFull());
    assertTrue(scan.getNoReadCount() == 0);
  }
  
  @Test
  public void testFailedScan() {
    RackType rack = new RackType("M0812");
    ServerConfig config = new ServerConfig();
    String data = "0188137823,0188137800,0188137799,0188137776,0188137775,0188137752,0188137751,0188137728,0188137822,0188137801," +
        "0188137798,0188137777,0188137774,0188137753,0188137750,0188137729,0188137821,0188137802,0188137797,0188137778,0188137773," +
        "0188137754,0188137749,0188137730,0188137820,0188137803,0188137796,0188137779,0188137772,0188137755,0188137748,0188137731," +
        "0188137819,0188137804,No Read,No Read,No Read,0188137756,0188137747,0188137732,0188137818,0188137805,0188137794," +
        "0188137781,0188137770,0188137757,0188137746,0188137733,0188137817,0188137806,0188137793,0188137782,0188137769,0188137758," + 
        "0188137745,0188137734,0188137816,0188137807,0188137792,0188137783,0188137768,0188137759,0188137744,0188137735,0188137815," +
        "0188137808,0188137791,0188137784,0188137767,0188137760,0188137743,0188137736,0188137814,0188137809,0188137790,0188137785," +
        "0188137766,0188137761,0188137742,0188137737,0188137813,0188137810,0188137789,0188137786,0188137765,0188137762,0188137741," +
        "0188137738,0188137001,0188137788,0188137812,0188137787,0188137764,0188137763,0188137740,0188137739,";
    Scan scan = new Scan(rack, data, config);
    assertEquals("0188137823", scan.getBarcode('A', 1));
    assertEquals("0188137799", scan.getBarcode(3, 1));
    assertEquals("0188137763", scan.getBarcode('F', 12));
    assertFalse(scan.isEmpty());
    assertTrue(scan.isFull());
    assertTrue(scan.getNoReadCount() == 3);
  }
  
  @Test
  public void test1ToA() {
    assertEquals(Scan.getCharForNumber(1), 'A');
  }
  
  @Test
  public void testUpperATo1() {
    assertEquals(Scan.getNumberForChar('A'), 1);
  }
  
  @Test
  public void testLowerATo1() {
    assertEquals(Scan.getNumberForChar('a'), 1);
  }
  
  @Test
  public void test24toX() {
    assertEquals(Scan.getCharForNumber(24), 'X');
  }
  
  @Test
  public void testUpperXTo24() {
    assertEquals(Scan.getNumberForChar('X'), 24);
  }
  
  @Test
  public void testLowerXTo24() {
    assertEquals(Scan.getNumberForChar('x'), 24);
  }
  
  @Test
  public void testInvalidLowNum() {
    exception.expect(IllegalArgumentException.class);
    Scan.getCharForNumber(0);
  }
  
  @Test
  public void testInvalidHighNum() {
    exception.expect(IllegalArgumentException.class);
    Scan.getCharForNumber(25);
  }
  
}
