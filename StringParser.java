import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/*
 * �쐬��: 2004/01/17
 *
 * �t�@�C�����當����ǂݍ���ŁChashcode���o��
 */

/**
 * @author matsu
 *
 * �e�X�g�R�[�h
 */
public class StringParser {
	public static void main(String args[]) {
		try {
			BufferedReader br =
				new BufferedReader(
					new InputStreamReader(new FileInputStream("data.txt")));
					
			String msg = "";
			while((msg = br.readLine()) != null){
				for(int i=0; i<msg.length(); i++){
					int a = String.valueOf(msg.charAt(i)).hashCode();
					System.out.println(a);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
