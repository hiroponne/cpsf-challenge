import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class StringToBinaryAES {

	String readString;
	String securityKey = "thisistestkey123";
	Key key;
	byte[] encodedData;
	byte[] byteData;
	String binary = "";

	public StringToBinaryAES() {

		/** 暗号化する文字列の読み込み */
		readString = inputString();
		
		/** 鍵の作成 */
		key = makeKey(securityKey);
		
		/** byte型にキャスト */
		byteData = readString.getBytes();
		
		/** 暗号化 */
		encodedData = encode(byteData, key);
		
		/** String型の2進数にキャスト */
		for (int i = 0; i < encodedData.length; i++) {
			Byte b = new Byte(encodedData[i]);
			/** 8bitに調整する為に頭に0をつける */
			for (int j = 0; j < 8 - Integer.toBinaryString(b.intValue())
					.length(); j++) {
				binary += "0";
			}
			binary += Integer.toBinaryString(b.intValue());
			System.out.println(b.intValue());
		}

		System.out.println(byteData.length);
		System.out.println(encodedData.length);

		/** 出力 */
		System.out.println(binary);
		
//		BinaryToStringAES bts = new BinaryToStringAES(binary);
	}

	public String inputString() {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(System.in));
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Key makeKey(String string) {
		return new SecretKeySpec(string.getBytes(), 0, string.length(), "AES");
	}

	public byte[] encode(byte[] buf, Key key) {
		try {
			/** AES暗号で暗号・復号化のインスタンスを取得 */
			Cipher cipher = Cipher.getInstance("AES");
			/** 暗号化モード */
			cipher.init(Cipher.ENCRYPT_MODE, key);
			/** 暗号化した値をreturn */
			return cipher.doFinal(buf);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] decode(byte[] buf, Key key) {
		try {
			/** AES暗号で暗号・復号化のインスタンスを取得 */
			Cipher cipher = Cipher.getInstance("AES");
			/** 復号化モード */
			cipher.init(Cipher.DECRYPT_MODE, key);
			/** 復号化した値をreturn */
			return cipher.doFinal(buf);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		StringToBinaryAES bts = new StringToBinaryAES();
	}

}
