package org.nestframework.commons.utils;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import sun.security.rsa.RSAPublicKeyImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA encrypt
 * @author wanghai
 * @version 1.0.0
 */
public class RSA_Encrypt {
	/** assign to RSA */
	private static String ALGORITHM = "RSA";

	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";   

	/** assign key size */
	private static int KEYSIZE = 1024;
	/** assign publicKey filename */
	private static String PUBLIC_KEY_FILE = "PublicKey";
	/** assign privateKey filename */
	private static String PRIVATE_KEY_FILE = "PrivateKey";

	private static PublicKey publicKey;
	
	private static PrivateKey privateKey;
	/**
	 * generate keypair(publicKey and privateKey)
	 */
	private static void generateKeyPair() throws Exception {
		/** RSA need SecureRandom  */
		SecureRandom sr = new SecureRandom();
		/** create a KeyPairGenerator object */
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
		/** initialized this KeyPairGenerator object use SecureRandom  */
		kpg.initialize(KEYSIZE, sr);
		/** create keyPair */
		KeyPair kp = kpg.generateKeyPair();
		/** get public key */
		publicKey = kp.getPublic();
		/** get private key */
		privateKey = kp.getPrivate();
		/** save public key to file as stream */
		ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(
				PUBLIC_KEY_FILE));
		ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(
				PRIVATE_KEY_FILE));
		oos1.writeObject(publicKey);
		oos2.writeObject(privateKey);
		writePublicKey(PUBLIC_KEY_FILE+".key");
		/** clear cache */
		oos1.close();
		oos2.close();
	}

	/**
	 * 初始化私钥（默认位置在classes目录）
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void getPrivateKey() throws FileNotFoundException, IOException, ClassNotFoundException{
		if(privateKey==null){
			String f = RSA_Encrypt.class.getResource("/").getFile();
			getPrivateKey(f+PRIVATE_KEY_FILE);
		}
	}
	
	/**
	 * 初始化指定位置的私钥
	 * @param privateKeyPath
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
 	public static void getPrivateKey(String privateKeyPath) throws FileNotFoundException, IOException, ClassNotFoundException{
		/** 将文件中的私钥对象读出 */
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				privateKeyPath));
		privateKey = (PrivateKey) ois.readObject();
		ois.close();
	}
 	
 	/**
	 * 初始化公钥钥（默认位置在classes目录）
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void getPublicKey() throws FileNotFoundException, IOException, ClassNotFoundException{
		if(publicKey==null){
			String f = RSA_Encrypt.class.getResource("/").getFile();
			getPublicKey(f+PUBLIC_KEY_FILE);
		}
	}
	
	/**
	 * 读取指定位置的公钥
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 */
	public static void getPublicKey(String publicKeyPath) throws FileNotFoundException, IOException, ClassNotFoundException{
		/** 将文件中的公钥对象读出 */
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				publicKeyPath));
		publicKey = (PublicKey) ois.readObject();
		ois.close();
	}
	
	/**
	 * write publicKey to Txt file
	 * @param publicKeyPath
	 */
	public static void writePublicKey(String publicKeyPath){
		try {
			PrintWriter pw;
			pw = new PrintWriter( new FileWriter( publicKeyPath ) );
			pw.println("-------------PUBLIC_KEY-------------"); 
			
			BigInteger m=((RSAPublicKeyImpl)publicKey).getModulus();
			BigInteger e=((RSAPublicKeyImpl)publicKey).getPublicExponent();
			pw.println("bitlen="+m.bitLength()+";");
			String mStr=m.toString(16);
			if((mStr.length() % 2)==1)
				mStr="0"+mStr;
			pw.println("m="+mStr+";");
			String eStr = e.toString(16);
			if((eStr.length() % 2)==1)
				eStr="0"+eStr;
			pw.println("e="+eStr+";");
			pw.println("-------------PUBLIC_KEY-------------"); 
			
	        pw.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	/**
	 * 加密方法 source： 源数据
	 */
	public static String encrypt(String source) throws Exception {
		getPublicKey();
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] b = source.getBytes();
		/** 执行加密操作 */
		byte[] b1 = cipher.doFinal(b);
		return Base64.encodeBase64URLSafeString(b1);
	}

	/**
	 * 解密算法 cryptograph:密文
	 */
	public static String decrypt(String cryptograph) throws Exception {
		getPrivateKey();
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] b1 = Base64.decodeBase64(cryptograph);
		/** 执行解密操作 */
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	/**
	 * 
	 * 用私钥对信息生成数字签名
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * @return
	 * @throws Exception
	 */
	public static String sign(String data) {
		try{
			getPrivateKey();
			// 用私钥对信息生成数字签名
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
	
			signature.initSign(privateKey);
	
			signature.update(data.getBytes());
			byte[] b1 =  signature.sign();
			return Base64.encodeBase64URLSafeString(b1);
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**  
     * 校验数字签名  
     * @param data  
     *            加密数据  
     * @param sign  
     *            数字签名  
     * @return 校验成功返回true 失败返回false  
     * @throws Exception  
     */  
    public static boolean verify(String data, String sign){
    	try{
	        // 取公钥匙对象   
    		getPublicKey();
	        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);   
	
	        signature.initVerify(publicKey);
			byte[] b1 = Base64.decodeBase64(sign);
	        signature.update(data.getBytes());   
	
	        // 验证签名是否正常   
	        return signature.verify(b1);   
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
    }   

	public static void main(String[] args) throws Exception {
		//生成密钥对
		//generateKeyPair();
		getPrivateKey(PRIVATE_KEY_FILE);
		getPublicKey(PUBLIC_KEY_FILE);
		writePublicKey(PUBLIC_KEY_FILE+".key");
		String source = "296502429874592438576248524admin";// 要加密的字符串
		String cryptograph = encrypt(source);// 生成的密文
		System.out.println(cryptograph);
		//String cryptograph = "jJIDpSMhia6RMuhYYRDZdWWCsnTbJoMUO31JW/8a+48lcUszpD0n86NunpfyhPtWkq6k6Ehj3MgYia5sL/HmK73YY8rp/7g0IbAfeVZ+HfSpzQd+wpV03eqchVgItQH5jiGsfNmm78Wc+Sbyn+mUExlpZl8vCM0Q9yl6l3rSlTw=";
		String target = decrypt(cryptograph);// 解密密文
		System.out.println(target);
		
		//签名
		//String source="296502429874592438576248524admin";
		String s=sign(source);
		System.out.println(s);
		//验证
		System.out.println(verify(source, s));
	}
}
