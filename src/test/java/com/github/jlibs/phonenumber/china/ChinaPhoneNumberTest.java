package com.github.jlibs.phonenumber.china;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *
 */
public class ChinaPhoneNumberTest {

  public ChinaPhoneNumberTest() {
  }

  @Test
  public void testPhoneNumber() {
    ChinaPhoneNumber n = new ChinaPhoneNumber("+86 (0755) 86105638");
    assertEquals("86", n.getInternationalAreaCode());
    assertEquals("755", n.getAreaCode());
    assertEquals("075586105638", n.getNumber());
    assertEquals("86105638", n.getLocalNumber());
    assertEquals("+86 75586105638", n.showNumber());
    assertEquals("001 86 75586105638", (new HongKongChinaPhoneNumber("23154678")).dialNumber(n));
    assertEquals("00 852 23154678", n.dialNumber(new HongKongChinaPhoneNumber("23154678")));
    assertTrue(n.isLandLineNumber());
    assertFalse(n.isCellularNumber());
  }

  @Test
  public void testSomeMethod() {
    Object[][] cases = {
      // INPUT, NUMBER, AREA, LOCAL, F, M, E, H, S
      new Object[]{"110", "110", "", "110", false, false, true, false, false},
      new Object[]{"+86-13800138000", "13800138000", "", "13800138000", false, true, false, false, false},
      new Object[]{"0755 86105638", "075586105638", "755", "86105638", true, false, false, false, false},
    };
    for(Object[] item : cases) {
      ChinaPhoneNumber n = new ChinaPhoneNumber((String)item[0]);
      assertEquals((String)item[1], n.getNumber());
      assertEquals((String)item[2], n.getAreaCode());
      assertEquals((String)item[3], n.getLocalNumber());
      assertEquals((boolean)item[4], n.isLandLineNumber());
      assertEquals((boolean)item[5], n.isCellularNumber());
      assertEquals((boolean)item[6], n.isEmergencyNumber());
      assertEquals((boolean)item[7], n.isHotLineNumber());
      assertEquals((boolean)item[8], n.isServiceNumber());
    }
  }

}
