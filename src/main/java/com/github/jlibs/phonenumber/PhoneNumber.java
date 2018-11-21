package com.github.jlibs.phonenumber;

import java.util.regex.Pattern;

/**
 * 电话号码
 */
public abstract class PhoneNumber {

  /**
   * 删除所有非数字的字符
   */
  private static final Pattern NORMALIZE_RULE = Pattern.compile("\\D+");

  /**
   * 分隔冠码、区号与电话号码的空格
   */
  private static final String SPACE = " ";

  /**
   * 国际冠码：拨打国际电话时，表示要拨出所在国家的号码前缀。多数国家为 00，美国加拿大为 011，不同国家有差别。
   * 比如在中国拨打美国的电话：00-1-美国电话号码。其中 00 就是要拨出中国的国际冠码，只在拨出电话时有用。
   * 如果给外国人留中国的电话号码，不能写国际冠码，因为不同国家不一样，可用+表示，如：+86 13800138000。
   * 这个字段只在特定国家/地区显示要拨打的国际电话时使用，以指示如何拨打国际电话。
   */
  private final String internationalPrefixCode;

  /**
   * 国际区域代码：是国家或地区的区号。例如中国大陆为86，港澳台地区分别为 852/853/886，美国加拿大为1等。
   */
  private final String internationalAreaCode;

  /**
   * 国家或地区内的完整的电话号码
   */
  private String number;

  /**
   * 创建电话号码实例
   * @param internationalAreaCode   国际区域代码
   * @param internationalPrefixCode 国际冠码
   */
  protected PhoneNumber(String internationalAreaCode, String internationalPrefixCode) {
    this.internationalAreaCode   = trimPrefixZero(normalizeNumber(internationalAreaCode));
    this.internationalPrefixCode = normalizeNumber(internationalPrefixCode);
  }

  /**
   * 国家或地区内的完整的电话号码，不包含国际冠码，不包含国家/地区的国际代码
   * @return
   */
  public String getNumber() {
    return this.number;
  }

  /**
   * 国家或地区内的完整的电话号码，不包含国际冠码，不包含国家/地区的国际代码
   * @param number
   */
  protected void setNumber(String number) {
    this.number = normalizeNumber(number);
  }

  /**
   * 国际区域代码：国家/地区的国际代码。例如中国大陆为86，港澳台地区分别为 852/853/886，美国加拿大为1等。
   * @return
   */
  public String getInternationalAreaCode() {
    return this.internationalAreaCode;
  }

  /**
   * 展示给别人的标准格式的电话号码，例如：+86 13800138000
   * @return
   */
  public String showNumber() {
    if (getInternationalAreaCode().isEmpty()) {
      return getNumber();
    }
    return "+" + getInternationalAreaCode() + SPACE + trim(getNumber());
  }

  /**
   * 获取拨打号码，即拨打到号码 to 的拨号方法，例如本号码为中国的，拨打美国（to）的号码：00 1 1234567890
   * @param to 要拨打的电话号码
   * @return
   */
  public String dialNumber(PhoneNumber to) {
    if (getInternationalAreaCode().isEmpty() || to.getInternationalAreaCode().isEmpty() || getInternationalAreaCode().equals(to.getInternationalAreaCode())) {
      return to.getNumber();
    }
    return String.join(SPACE, getInternationalPrefixCode(), to.getInternationalAreaCode(), trim(to.getNumber()));
  }

  /**
   * 国际冠码：拨打国际电话时，表示要拨出所在国家的号码前缀。多数国家为 00，美国加拿大为 011，不同国家有差别。
   * 比如在中国拨打美国的电话：00-1-美国电话号码。其中 00 就是要拨出中国的国际冠码，只在拨出电话时有用。
   * 如果给外国人留中国的电话号码，不能写国际冠码，因为不同国家不一样，可用+表示，如：+86 13800138000。
   * 这个字段只在特定国家/地区显示要拨打的国际电话时使用，以指示如何拨打国际电话。
   * @return
   */
  protected String getInternationalPrefixCode() {
    return this.internationalPrefixCode;
  }

  /**
   * 删除号码的前导0
   * @param number
   * @return
   */
  protected static String trim(String number) {
    return trimPrefixZero(number);
  }

  /**
   * 号码标准化：删除所有非数字的字符
   * @param number
   * @return
   */
  protected static String normalize(String number) {
    return normalizeNumber(number);
  }

  /**
   * 删除号码的前导0
   * @param number
   * @return
   */
  private static String trimPrefixZero(String number) {
    int p = 0;
    int l = number.length();
    while ((p < l) && ('0' == number.charAt(p))) {
      p++;
    }
    return p > 0 ? number.substring(p) : number;
  }

  /**
   * 号码标准化：删除所有非数字的字符
   * @param number
   * @return
   */
  private static String normalizeNumber(String number) {
    if (null == number) {
      return "";
    }
    return NORMALIZE_RULE.matcher(number).replaceAll("");
  }

}
