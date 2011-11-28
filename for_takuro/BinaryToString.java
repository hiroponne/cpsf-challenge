public class BinaryToString {

	static String binary = "010010000110010101101100011011000110111100101100010101110110111101110010011011000110010000100001";
	String binaryPer8Bits;
	byte[] byteData;
	
	public BinaryToString(){
		this(binary);
	}

	public BinaryToString(String binary) {

		byteData = new byte[binary.length() / 8];

		for (int i = 0; i < binary.length() / 8; i++) {
			/** 文字列を8文字ずつに分ける */
			binaryPer8Bits = binary.substring(i * 8, (i + 1) * 8);
			/** ２進数の文字列を10進数の整数化 */
			Integer integer = new Integer(Integer.parseInt(binaryPer8Bits, 2));
			/** byte型変数にキャスト */
			byteData[i] = integer.byteValue();
		}
		/** String型にキャストし出力 */
		System.out.println(new String(byteData));
	}

	public static void main(String[] args) {
		BinaryToString bts = new BinaryToString();
	}

}
