package com.github.jlibs.phonenumber.china;

import com.github.jlibs.phonenumber.PhoneNumber;

/**
 * 中国香港地区的电话号码
 */
public class HongKongChinaPhoneNumber extends PhoneNumber {

  private static final int NUMBER_TYPE_UNKNOW    = 0; // 未知
  private static final int NUMBER_TYPE_LAND_LINE = 1; // 座机、固定电话
  private static final int NUMBER_TYPE_CELLULAR  = 2; // 手机
  private static final int NUMBER_TYPE_EMERGENCY = 3; // 紧急电话，报警电话
  private static final int NUMBER_TYPE_OTHER     = 99; // 其它未知号码

  /**
   * 手机号码类型
   */
  private int type = NUMBER_TYPE_UNKNOW;

  /**
   * 创建中国香港地区的电话号码实例
   * @param number 电话号码
   */
  public HongKongChinaPhoneNumber(String number) {
    super("852", "001");
    super.setNumber(parse(number));
  }

  /**
   * 是否为固定电话/座机号码
   * @return
   */
  public boolean isLandLineNumber() {
    return NUMBER_TYPE_LAND_LINE == this.type;
  }

  /**
   * 是否为手机号码
   * @return
   */
  public boolean isCellularNumber() {
    return NUMBER_TYPE_CELLULAR == this.type;
  }

  /**
   * 是否为紧急报警电话号码
   * @return
   */
  public boolean isEmergencyNumber() {
    return NUMBER_TYPE_EMERGENCY == this.type;
  }

  private String parse(String number) {
    String n = trim(normalize(number));
    if (n.startsWith("852")) {
      n = trim(n.substring(3));
    }
    if (n.startsWith("2") || n.startsWith("3")) {
      this.type = NUMBER_TYPE_LAND_LINE;
      return n;
    }
    char s = n.charAt(0);
    if (('5' <= s) && (s <= '9') && (s != '7')) {
      this.type = NUMBER_TYPE_CELLULAR;
      return n;
    }
    this.type = NUMBER_TYPE_OTHER;
    return n;
  }

}
