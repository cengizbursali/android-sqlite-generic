package is.wordy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import is.data.DBClass;
import is.enums.WordLevels;
import is.enums.WordTypes;
import is.model.Word;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {


	DBClass dbHelper = new DBClass(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		for (Word item : dbHelper.getAllWords() ) {
//			Log.d("word", String.valueOf(item.getId()) + " " + item.getEng());
//		}

		Button btnAddWord = (Button)findViewById(R.id.btnAddWord);
		btnAddWord.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, AddWord.class));

			}
		});

		Button btnPlayWord = (Button)findViewById(R.id.btnPlay);
		btnPlayWord.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, PlayWordy.class));

			}
		});

		/*
		Button btnDene = (Button)findViewById(R.id.btnDeneme);
		btnDene.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, Deneme.class));

			}
		});
		 */

		Button btnExport = (Button)findViewById(R.id.btnExport);
		btnExport.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
				adb.setTitle("Export Words?");
				adb.setMessage("Are you sure you want export words to a file?");
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						exportEverything();
					}});
				adb.show();

			}
		});
		
		Button btnImport = (Button)findViewById(R.id.btnImport);
		btnImport.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
				adb.setTitle("Import Words?");
				adb.setMessage("Please, make sure there is a file named 'wordsList.txt' in your external memory folder?");
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						importEverything();
					}});
				adb.show();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void exportEverything(){
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
					ShowToast("File Written Successfully!", Toast.LENGTH_LONG);
					return;
				}
			} 
			catch (IOException e) {
				ShowToast("File Write FAILED!!", Toast.LENGTH_LONG);
				return;
			}
		}
		ShowToast("There are no words in the database!!", Toast.LENGTH_LONG);
	}

	public void importEverything(){
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "wordsList.txt");
		String line = "";

		try {
			if (root.canRead()){
				FileReader fileReader = new FileReader(file);
				BufferedReader in = new BufferedReader(fileReader);
				
				dbHelper.emptyWordsTable();
				
				while((line = in.readLine()) != null)
				{
					String[] values = line.split("%");
					System.out.println(values[0]);
					dbHelper.addWord(new Word(Integer.valueOf(values[0]) + 250,
							values[1],
							values[2],
							WordTypes.getEnumByValue(values[3]),
							WordLevels.getEnumByValue(values[4]),
							Integer.valueOf(values[5])));

				}
				in.close();
				ShowToast("File Imported Successfully!", Toast.LENGTH_LONG);
			}
		} 
		catch (IOException e) {
			ShowToast("File Read FAILED!! Please, make sure file name is 'wordsList.txt!!' Still wrong? Then, file content format is improper!!", Toast.LENGTH_LONG);
		}
	}

	public void ShowToast(String _warningText, int length){
		Toast toast = Toast.makeText(getApplicationContext(), _warningText, length);
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
