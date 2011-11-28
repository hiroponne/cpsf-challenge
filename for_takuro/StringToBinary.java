import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class StringToBinary {

	String readString;
	byte[] byteData;
	String binary = "";

	public StringToBinary() {

		/** 文字列の読み込み */
		readString = inputString();

		/** byte型にキャスト */
		byteData = readString.getBytes();
		
		/** String型の2進数にキャスト */
		for (int i = 0; i < byteData.length; i++) {
			Byte b = new Byte(byteData[i]);
			/** 8bitに調整する為に頭に0をつける */
			for (int j = 0; j < 8 - Integer.toBinaryString(b.intValue())
					.length(); j++) {
				binary += "0";
			}
			/** String型で2進数に変換 */
			binary += Integer.toBinaryString(b.intValue());
		}
		/** 出力 */
		System.out.println(binary);
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

	public static void main(String[] args) {
		StringToBinary bts = new StringToBinary();
	}

}
