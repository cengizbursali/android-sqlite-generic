package is.wordy;

import is.data.DBClass;
import is.enums.WordLevels;
import is.enums.WordTypes;
import is.model.Category;
import is.model.Word;
import is.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddWord extends ListActivity {


	DBClass dbHelper = new DBClass(this);

	ArrayList<String> tr_words_list = new ArrayList<String>();

	ArrayList<String> types_list = new ArrayList<String>();
	ArrayList<String> categories_list = new ArrayList<String>();
	ArrayList<String> levels_list = new ArrayList<String>();

	Integer selected_level_id;
	Integer selected_type_id;
	Integer selected_category_id = 0;

	//Declaring an ArrayAdapter to set items to ListView
	ArrayAdapter<String> eng_words_adapter;
	ArrayAdapter<String> types_adapter;
	ArrayAdapter<String> categories_adapter;
	ArrayAdapter<String> levels_adapter;

	//Called when the activity is first created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Setting a custom layout for the list activity
		setContentView(R.layout.add_word);

		//Reference to the button of the layout main.xml
		Button btnAddToList = (Button) findViewById(R.id.btnAddToList);
		Button btnAddToDatabase = (Button) findViewById(R.id.btnAddToDatabase);

		//Spinner spnCategory = (Spinner) findViewById(R.id.spinnerCategory);
		Spinner spnLevel = (Spinner) findViewById(R.id.spinnerLevel);
		Spinner spnType = (Spinner) findViewById(R.id.spinnerType);

		List<Category> categories = dbHelper.getAllCategories();

		for (Category _category : categories) {
			categories_list.add(_category.getCategoryName());
		}


//		categories_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories_list);
//		spnCategory.setAdapter(categories_adapter);

		FillInLevels();
		levels_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels_list);
		spnLevel.setAdapter(levels_adapter);

		FillInTypes();
		types_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types_list);
		spnType.setAdapter(types_adapter);



		spnLevel.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				selected_level_id = position + 1;
			}

			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});

		spnType.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				selected_type_id = position + 1;
			}

			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});

		/*spnCategory.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				selected_category_id = position;
			}

			public void onNothingSelected(AdapterView<?> parentView) {

			}

		});*/


		ListView lv = getListView();

		//Defining the ArrayAdapter to set items to ListView
		eng_words_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tr_words_list);

		//Defining a click event listener for the button "Add"
		OnClickListener addToListListener = new OnClickListener() {

			public void onClick(View v) {
				EditText edit = (EditText) findViewById(R.id.txtTr);
				if(!StringUtils.isNullOrEmpty(edit.getText().toString())){
					tr_words_list.add(edit.getText().toString());
					edit.setText("");
					eng_words_adapter.notifyDataSetChanged();
				}
				else{
					ShowToast(getResources().getString(R.string.warnAddToList), Toast.LENGTH_SHORT);
				}
			}
		};

		
		OnClickListener addToDatabaseListener = new OnClickListener() {

			public void onClick(View v) {
				EditText eng = (EditText) findViewById(R.id.txtEng);
				
				if(StringUtils.isNullOrEmpty(eng.getText().toString()) ||
						tr_words_list.isEmpty()){
					ShowToast(getResources().getString(R.string.warnNullValue), Toast.LENGTH_SHORT);
					return;
				}
//
//				if(dbHelper.getWordByEng(eng.getText().toString().trim()).size() != 0){
//					ShowToast(getResources().getString(R.string.warnAddedValue), Toast.LENGTH_LONG);
//					return;
//				}


				if(dbHelper.getWordByEngAndType(eng.getText().toString().trim(), selected_type_id).size() != 0){
					ShowToast(getResources().getString(R.string.warnAddedValue), Toast.LENGTH_LONG);
					return;
				}
				
				
				String engValue = eng.getText().toString().trim();
				
				String trValue = "";
				for (String item : tr_words_list) {
					trValue = trValue.concat(item + ";");
				}
				trValue = trValue.substring(0, trValue.length() - 1);
				
				
				Word word = new Word(engValue, 
						trValue, 
						WordTypes.getEnumByValue(selected_type_id.toString()),
						WordLevels.getEnumByValue(selected_level_id.toString()),
						selected_category_id);
				
				if(dbHelper.addWord(word)){
					ShowToast(getResources().getString(R.string.infoSuccessfullyAdded), Toast.LENGTH_SHORT);
					eng.setText("");
					
					tr_words_list.clear();
					eng_words_adapter.notifyDataSetChanged();
				}
				
			}
		};


		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				AlertDialog.Builder adb=new AlertDialog.Builder(AddWord.this);
				adb.setTitle("Delete?");
				adb.setMessage("Are you sure you want to delete ' " + eng_words_adapter.getItem(position) + " '");
				final int positionToRemove = position;
				adb.setNegativeButton("Cancel", null);
				adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						tr_words_list.remove(positionToRemove);
						eng_words_adapter.notifyDataSetChanged();
					}});
				adb.show();
			}
		});

		//Setting the event listener for the add button
		btnAddToList.setOnClickListener(addToListListener);
		btnAddToDatabase.setOnClickListener(addToDatabaseListener);

		//Setting the adapter to the ListView
		setListAdapter(eng_words_adapter);
		
		for (Word name : dbHelper.getAllWords()) {
			Log.d("added", name.getTr());
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void FillInLevels(){
		levels_list.clear();
		for (Integer i = 1; i <= WordLevels.values().length; i++) {
			levels_list.add(StringUtils.toLowerCaseWithoutEachInitial(WordLevels.getEnumNameByValue(i.toString()), "_"));
		}
		selected_level_id = 1;
	}

	public void FillInTypes(){
		types_list.clear();
		for (Integer i = 1; i <= WordTypes.values().length; i++) {
			types_list.add(StringUtils.toLowerCaseWithoutEachInitial(WordTypes.getEnumNameByValue(i.toString()), "_"));
		}
		selected_type_id = 1;
	}

	public void ShowToast(String _warningText, int _length){
		Toast toast = Toast.makeText(getApplicationContext(), _warningText, _length);
		toast.show();
	}
	
}
