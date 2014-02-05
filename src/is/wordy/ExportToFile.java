package is.wordy;

import is.data.DBClass;
import is.model.Word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class ExportToFile extends Activity {


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
				if (root.canWrite()){
					FileWriter filewriter = new FileWriter(file);
					BufferedWriter out = new BufferedWriter(filewriter);
					for (int i=0; i<content.size(); i++)
					{
						out.write(content.get(i) + "\n");
					}
					out.close();
					ShowToast("File Written Successfully!");
				}
			} 
			catch (IOException e) {
				ShowToast("File Write FAILED!!");
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
