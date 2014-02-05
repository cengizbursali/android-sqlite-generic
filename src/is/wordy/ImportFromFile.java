package is.wordy;

import is.data.DBClass;
import is.enums.WordLevels;
import is.enums.WordTypes;
import is.model.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class ImportFromFile extends Activity {


	DBClass dbHelper = new DBClass(this);

	//Called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Setting a custom layout for the list activity
		setContentView(R.layout.activity_main);

		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "wordsList.txt");

		List<String> content = prepareExportContent();

		if (content.size() > 0)
		{
			try {
				if (root.canRead()){
					FileReader fileReader = new FileReader(file);
					BufferedReader in = new BufferedReader(fileReader);
					for (int i=0; i<content.size(); i++)
					{
						String line = in.readLine();
						String[] values = line.split("&");
						dbHelper.addWord(new Word(Integer.valueOf(values[0]) + 250,
								values[1],
								values[2],
								WordTypes.getEnumByValue(values[3]),
								WordLevels.getEnumByValue(values[4]),
								Integer.valueOf(values[5])));

					}
					in.close();
					ShowToast("File Written Successfully!");
				}
			} 
			catch (IOException e) {
				ShowToast("File Read FAILED!! Please, make sure file name is 'wordsList.txt!!' Still wrong? Then, file content format is improper!!");
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void ShowToast(String _warningText){
		Toast toast = Toast.makeText(getApplicationContext(), _warningText, Toast.LENGTH_LONG);
		toast.show();
	}

	public List<String> prepareExportContent(){
		List<String> fileContent = new ArrayList<String>();
		String value = "";
		List<Word> wordsList = dbHelper.getAllWords();
		for (Word word : wordsList) {
			value = word.getId() + "%" +
					word.getEng() + "%" +
					word.getTr() + "%" + 
					word.getType().getID() + "%" + 
					word.getLevel().getID() + "%" + 
					word.getCategory();
			fileContent.add(value);
		}
		return fileContent;
	}

}
