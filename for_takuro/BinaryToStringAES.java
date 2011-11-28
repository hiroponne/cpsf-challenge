import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class BinaryToStringAES {

	static String binary = "0001010111100111010111110110001100111010011010101011010101100011101100101110101110011001110001000101000111101001100111001010001111110100100010001100100111100000000111011100010100001011110000000100011100010100011111100010111111110000101110001101001010111001";
	String binaryPer8Bits;
	String securityKey = "thisistestkey123";
	byte[] byteData;

	public BinaryToStringAES() {
		this(binary);
	}

	public BinaryToStringAES(String binary) {
		/** 鍵を作成 */
		Key key = new SecretKeySpec(securityKey.getBytes(), 0,
				securityKey.length(), "AES");
		byteData = new byte[binary.length() / 8];

		for (int i = 0; i < binary.length() / 8; i++) {
			/** 文字列を8文字ずつに分ける */
			binaryPer8Bits = binary.substring(i * 8, (i + 1) * 8);
			/** ２進数の文字列を10進数の整数化 */
			Integer integer = new Integer(Integer.parseInt(binaryPer8Bits, 2));
			/** byte型変数にキャスト */
			byteData[i] = integer.byteValue();
		}

		// System.out.println(byteData.length);
		// System.out.println(byteData.toString());

		/** 復号化した文字列を出力 */
		System.out.println(new String(decode(byteData, key)));

	}

	/** デバッグの為の暗号化メソッド */
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
		BinaryToStringAES bts = new BinaryToStringAES();
	}

}
