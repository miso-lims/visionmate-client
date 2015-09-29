package visionmate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import visionmate.RackType.Manufacturer;

public class RackTypeTest {

  @Test
  public void testM0812Rack() {
    RackType rack = new RackType("M0812");
    assertEquals(rack.getManufacturer(), Manufacturer.MATRIX);
    assertTrue(rack.getRows() == 8);
    assertTrue(rack.getColumns() == 12);
  }
  
  @Test
  public void testStringRepresentation() {
    RackType rack = new RackType(Manufacturer.MATRIX, 8, 12);
    assertEquals("M0812", rack.getStringRepresentation());
  }

}
