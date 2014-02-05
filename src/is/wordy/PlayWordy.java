package is.wordy;

import is.data.DBClass;
import is.model.Word;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlayWordy extends Activity {


	DBClass dbHelper = new DBClass(this);
	Integer WORD_COUNT = 0;
	int word_index = 0;

	//Called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Setting a custom layout for the list activity
		setContentView(R.layout.play_word);
		WORD_COUNT = dbHelper.getWordsCount();



		//Reference to the button of the layout main.xml
		final TextView txtEng = (TextView) findViewById(R.id.lblEngWord);
		final TextView txtTr = (TextView) findViewById(R.id.lblTrWord);
		
		Button btnShowWord = (Button) findViewById(R.id.btnShowWord);
		Button btnNextWord = (Button) findViewById(R.id.btnNextWord);


		word_index = pickRandomNumber();
		Word word = dbHelper.getWordByID(word_index);

		if(word != null){
			txtEng.setText("Eng:    " + word.getEng() + "\n" +
					"Type:   " + word.getType().toString() + "\n" +
					"Level:  " + word.getLevel().toString());
			String[] values = word.getTr().split(";");
			String trValue = "Tr Values:\n";
			for (String item : values) {
				trValue = trValue.concat("*" + item + "\n");
			}
			txtTr.setText(trValue);
			txtTr.setVisibility(View.INVISIBLE);
		}
		else{
			ShowToast(getResources().getString(R.string.warnSelectError));
		}

		//Defining a click event listener for the button "Add"
		OnClickListener showWordListener = new OnClickListener() {

			public void onClick(View v) {
				txtTr.setVisibility(View.VISIBLE);
			}
		};


		OnClickListener nextWordListener = new OnClickListener() {

			public void onClick(View v) {
				word_index = pickRandomNumber();
				Word word = dbHelper.getWordByID(word_index);
				
				if(word != null){
					txtEng.setText("Eng:     " + word.getEng() + "\n" +
							"Type:    " + word.getType().toString() + "\n" +
							"Level:   " + word.getLevel().toString());
					String[] values = word.getTr().split(";");
					String trValue = "Tr Values:\n";
					for (String item : values) {
						trValue = trValue.concat("*" + item + "\n");
					}
					txtTr.setText(trValue);
					txtTr.setVisibility(View.INVISIBLE);
				}
				else{
					ShowToast(getResources().getString(R.string.warnSelectError));
				}
			}
		};



		//Setting the event listener for the add button
		btnShowWord.setOnClickListener(showWordListener);
		btnNextWord.setOnClickListener(nextWordListener);

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
	
	public Integer pickRandomNumber(){
		Integer tempNumber = 0;
		int count = 0;
		tempNumber = new Random().nextInt(WORD_COUNT + 1);
		if(tempNumber == 0 || tempNumber == word_index){
			do {
				if(count == 100){
					ShowToast("Too many tries! Error!");
					break;
				}
				tempNumber = new Random().nextInt(WORD_COUNT + 1);
				count++;
			} while (tempNumber == 0 || tempNumber == word_index);
		}
		return tempNumber;
	}

}
