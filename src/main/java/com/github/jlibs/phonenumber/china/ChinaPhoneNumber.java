package com.github.jlibs.phonenumber.china;

import com.github.jlibs.phonenumber.PhoneNumber;
import java.util.regex.Pattern;

/**
 * 中国大陆地区的电话号码
 */
public class ChinaPhoneNumber extends PhoneNumber {

  private static final int NUMBER_TYPE_UNKNOW    = 0; // 未知
  private static final int NUMBER_TYPE_LAND_LINE = 1; // 座机、固定电话
  private static final int NUMBER_TYPE_CELLULAR  = 2; // 手机
  private static final int NUMBER_TYPE_EMERGENCY = 3; // 紧急电话，报警电话
  private static final int NUMBER_TYPE_HOTLINE   = 4; // 热线号码，400号码
  private static final int NUMBER_TYPE_SERVICE   = 5; // 客服号码，如 10010、95599 等
  private static final int NUMBER_TYPE_OTHER     = 99; // 其它未知号码

  private static final Pattern CELLULAR_RULE = Pattern.compile("^1[3-9]\\d{9}$");
  private static final Pattern LANDLINE_RULE = Pattern.compile("^[2-9]\\d{8,10}$");

  /**
   * 固话区号
   */
  private final String area;

  /**
   * 不含区号的电话号码
   */
  private final String number;

  /**
   * 手机号码类型
   */
  private int type = NUMBER_TYPE_UNKNOW;

  /**
   * 创建中国大陆地区的电话号码实例
   * @param number 电话号码，如果是固话号码，必须包含区号
   */
  public ChinaPhoneNumber(String number) {
    super("86", "00");
    String[] parsed = parse(number);
    this.area   = parsed[0];
    this.number = parsed[1];
    super.setNumber(this.area.isEmpty() ? this.number : ("0" + this.area + this.number));
  }

  /**
   * 创建中国大陆地区的电话号码实例
   * @param area   区号，若无，则传递 null 或空字符串
   * @param number 号码（不含区号）
   */
  public ChinaPhoneNumber(String area, String number) {
    this((null == area ? "" : area) + number);
  }

  /**
   * 获取区号，只有固话号码才有区号，其它号码为空字符串
   * @return 返回区号，不包含前导0，如 10，20，755 等
   */
  public String getAreaCode() {
    return this.area;
  }

  /**
   * 获取本地号码，不包含国内或地区内部的区号。
   * @return
   */
  public String getLocalNumber() {
    return this.number;
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
   * 是否为 400/800 热线号码
   * @return
   */
  public boolean isHotLineNumber() {
    return NUMBER_TYPE_HOTLINE == this.type;
  }

  /**
   * 是否为 10/95/96 大型企业单位的服务号码
   * @return
   */
  public boolean isServiceNumber() {
    return NUMBER_TYPE_SERVICE == this.type;
  }

  /**
   * 是否为紧急报警电话号码
   * @return
   */
  public boolean isEmergencyNumber() {
    return NUMBER_TYPE_EMERGENCY == this.type;
  }

  private String[] parse(String number) {
    String n = trim(normalize(number));
    if (n.startsWith("86")) {
      n = trim(n.substring(2));
    }
    if (n.startsWith("10")) {
      if (n.length() < 7) {
        this.type = NUMBER_TYPE_SERVICE; // 专用服务号码
        return new String[]{"", n};
      }
      this.type = NUMBER_TYPE_LAND_LINE;
      return new String[]{"10", n.substring(2)}; // 北京固话号码
    }
    if (n.startsWith("11") || n.startsWith("12")) { // 报警号码或其它
      this.type = n.length() == 3 ? NUMBER_TYPE_EMERGENCY : NUMBER_TYPE_OTHER;
      return new String[]{"", n};
    }
    if (n.startsWith("400") || n.startsWith("800")) { // 400/800 热线号码或其它
      this.type = n.length() == 10 ? NUMBER_TYPE_HOTLINE : NUMBER_TYPE_OTHER;
      return new String[]{"", n};
    }
    if ((n.startsWith("95") || n.startsWith("96")) && n.length() < 7) {
      this.type = NUMBER_TYPE_SERVICE; // 专用服务号码
      return new String[]{"", n};
    }
    if (CELLULAR_RULE.matcher(n).matches()) { // 手机号码
      this.type = NUMBER_TYPE_CELLULAR;
      return new String[]{"", n};
    }
    if (LANDLINE_RULE.matcher(n).matches()) {
      this.type = NUMBER_TYPE_LAND_LINE; // 固话号码
      int an = n.startsWith("2") ? 2 : 3; // 区号长度
      return new String[]{n.substring(0, an), n.substring(an)};
    }
    this.type = NUMBER_TYPE_OTHER;
    return new String[]{"", n};
  }

}
