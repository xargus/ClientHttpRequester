package center.xargus.ClientHttpRequester.utils;

public class TextUtils {

	public static boolean isEmpty(String text) {
		if (text != null && text.length() > 0) {
			return false;
		} else {
			return  true;
		}
	}
}
