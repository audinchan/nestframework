package org.nestframework.commons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Title:        字符串加密
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      北控软件有限公司
 * @author       项目开发小组
 * @version      2.0
 */
public class Encryption
{
  // input
  //   b:   输入串
  //   alg: 算法,可取的值有:SRA, SRA-1, MD5.
  // return:
  //   若算法存在,则返回输入串b的信息摘要;
  //   否则,返回原来的输入串.
  public static String computeDigest(String str, String alg)
  {
    MessageDigest currentAlgorithm = null;

    try
    {
      currentAlgorithm = MessageDigest.getInstance(alg);
    }
    catch(NoSuchAlgorithmException e)
    {
      return str;
    }

    currentAlgorithm.reset();
    currentAlgorithm.update(str.getBytes());
    byte[] hash = currentAlgorithm.digest();
    String d = "";
    int usbyte = 0;  // unsigned byte
    for (int i = 0; i < hash.length; i++)
    {
      usbyte = hash[i] & 0xFF ;   // byte-wise AND converts signed byte to unsigned.
      if(usbyte<16)
        d += "0" + Integer.toHexString(usbyte);   // pad on left if single hex digit.
      else
        d +=       Integer.toHexString(usbyte);
    }// for

    return d.toUpperCase();
  }

  public static String computeDigest(String str)
  {
    return computeDigest(str, "MD5");
  }

  public static String createRegisterCode(String str)
  {
    return computeDigest(str);
  }

  public static String encrypt( String str )
  {
    String digestStr = computeDigest(str);

    return computeDigest(digestStr);
  }

  public static String encode( String str )
  {
    byte[] bytesStr = str.getBytes( ) ;
    for (int i = 0; i < bytesStr.length; i++)
      bytesStr[i] ^= 0x1A;

    return new String( bytesStr ) ;
  }

  public static String decode( String str )
  {
    byte[] bytesStr = str.getBytes( ) ;
    for (int i = 0; i < bytesStr.length; i++)
      bytesStr[i] ^= 0x1A;

    return new String( bytesStr ) ;
  }
}