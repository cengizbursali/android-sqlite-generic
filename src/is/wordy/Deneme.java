package is.wordy;


import is.data.DBClass;
import is.model.Word;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Deneme extends Activity {


	DBClass dbHelper = new DBClass(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		DBClass dbHelper =  new DBClass(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.deneme);

		for (Word item : dbHelper.getAllWords() ) {
			Log.d("word", String.valueOf(item.getId()) + " " + item.getEng());
			TextView dene = (TextView)findViewById(R.id.txtDeneme);
			dene.append(item.getEng()+"\n");
		}
		
		for (int i = 0; i < 1000; i++) {
			TextView dene = (TextView)findViewById(R.id.txtDeneme);
			dene.append("Deneme\n");
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void ShowToast(String _warningText, int length){
		Toast toast = Toast.makeText(getApplicationContext(), _warningText, length);
		toast.show();
	}

	
}
