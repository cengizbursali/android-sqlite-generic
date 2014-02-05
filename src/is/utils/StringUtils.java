package is.utils;

import android.annotation.SuppressLint;
import java.util.Locale;

@SuppressLint("DefaultLocale")
public class StringUtils {
	
	private static String[] badCharacters= new String[]{
		",",
		".",
		";",
		"\\",
		"(",
		")",
		"%",
		"&",
		"-"
	};
	
	public static Boolean isNullOrEmpty(String _text){
		if(_text == null || _text.trim().equals("")){
			return true;
		}
		return false;
	}
	
	public static String removeBadCharacters(String _text, String[] _chars){
		String tempText = "";
		for (String char_item : _chars) {
			if(_text.contains(char_item)){
				tempText = _text.replace(char_item, "");
			}
		}
		return tempText;
	}
	
	public static String removeBadCharacters(String _text){
		String tempText = "";
		for (String char_item : badCharacters) {
			if(_text.contains(char_item)){
				tempText = _text.replace(char_item, "");
			}
		}
		return tempText;
	}

	public static String toLowerCaseWithoutInitial(String _text){
		String tempText = _text.substring(0, 1);
		tempText = tempText.concat(_text.substring(1).toLowerCase(Locale.ENGLISH));
		return tempText;
	}

	public static String toLowerCaseWithoutEachInitial(String _text, String _splitBy){
		String[] texts = _text.split(_splitBy);

		String tempText = "";
		
		for (String item : texts) {
			tempText = tempText.concat(
					item.substring(0, 1).concat(
							item.substring(1).toLowerCase(Locale.ENGLISH)).concat(" "));
		}
		
		return tempText.trim();
	}

}
