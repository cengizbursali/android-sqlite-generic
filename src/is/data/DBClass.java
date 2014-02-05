package is.data;

import is.enums.WordLevels;
import is.enums.WordTypes;
import is.model.Category;
import is.model.Word;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class DBClass extends SQLiteOpenHelper{

	// Database Version
	private static final int DATABASE_VERSION = 1;
	

	// Database Name
	private static final String DATABASE_NAME = "wordy";

	// Database Tables
	private static final String TABLE_WORDS = "words";	// 'words' Table
	private static final String TABLE_CATEGORIES = "categories";	// 'categories' Table
	//private static final String TABLE_SENTENCES = "sentences";	// 'sentences' Table

	//private boolean isSuccess = true;

	// Table 'words' Columns Names
	private static final String TABLE_WORDS_ID = "id";
	private static final String TABLE_WORDS_ENG = "eng";
	private static final String TABLE_WORDS_TR = "tr";
	private static final String TABLE_WORDS_LEVEL = "level";
	private static final String TABLE_WORDS_TYPE = "type";
	private static final String TABLE_WORDS_CATEGORY = "category";

	// Table 'categories' Columns Names
	private static final String TABLE_CATEGORIES_ID = "id";
	private static final String TABLE_CATEGORIES_NAME = "name";
	private static final String TABLE_CATEGORIES_DESCRIPTION = "description";




	// Constructor
	public DBClass(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		//Drop existing tables firstly
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

		//Create main table script - WORDS
		String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + "("
				+ TABLE_WORDS_ID + " INTEGER PRIMARY KEY," 
				+ TABLE_WORDS_ENG + " TEXT,"
				+ TABLE_WORDS_TR + " TEXT," 
				+ TABLE_WORDS_TYPE + " INTEGER," 
				+ TABLE_WORDS_LEVEL + " INTEGER," 
				+ TABLE_WORDS_CATEGORY + " INTEGER"
				+ ")";

		//Create table script - CATEGORIES
		String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
				+ TABLE_CATEGORIES_ID + " INTEGER PRIMARY KEY," 
				+ TABLE_CATEGORIES_NAME + " TEXT,"
				+ TABLE_CATEGORIES_DESCRIPTION + " TEXT"
				+ ")";

		//Create Tables
		db.execSQL(CREATE_WORDS_TABLE);
		db.execSQL(CREATE_CATEGORIES_TABLE);
		
		//TODO: XML Parse and import will be handled here!
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// Drop older tables if exist
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

		// Create tables again
		onCreate(db);
	}




	//INSERT OPERATIONS
	//-----START-----


	// Adding New Word
	public Boolean addWord(Word _word) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(TABLE_WORDS_ENG, _word.getEng());
			values.put(TABLE_WORDS_TR, _word.getTr());
			values.put(TABLE_WORDS_TYPE, _word.getType().getID());
			values.put(TABLE_WORDS_LEVEL, _word.getLevel().getID());
			values.put(TABLE_WORDS_CATEGORY, _word.getCategory());

			//Insert Row
			db.insert(TABLE_WORDS, null, values);
			db.close();
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Adding New Category
	public Boolean addCategory(Category _category) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(TABLE_CATEGORIES_NAME, _category.getCategoryName());
			values.put(TABLE_CATEGORIES_DESCRIPTION, _category.getCategoryDescription());

			//Insert Row
			db.insert(TABLE_CATEGORIES, null, values);
			db.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//-----END-----
	//INSERT OPERATIONS





	//SELECT OPERATIONS
	//-----START-----

	public List<Word> getAllWords() {

		List<Word> wordsList = new ArrayList<Word>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_WORDS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					Word word = new Word();

					word.setId(Integer.parseInt(cursor.getString(0)));
					word.setEng(cursor.getString(1));
					word.setTr(cursor.getString(2));
					word.setType(WordTypes.getEnumByValue(cursor.getString(3)));				
					word.setLevel(WordLevels.getEnumByValue(cursor.getString(4)));
					word.setCategory(Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordsByCount(Integer _count) {

		List<Word> wordsList = new ArrayList<Word>();

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
				TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, " LIMIT ?",
				new String[] { _count.toString() }, null, null, null);

		try {
			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public Word getWordByID(int _id) {
		Word contact = null;
		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_ID + "=?",
					new String[] { String.valueOf(_id) }, null, null, null);

			if (cursor != null){
				cursor.moveToFirst();

				contact = new Word(
						Integer.parseInt(cursor.getString(0)),
						cursor.getString(1),
						cursor.getString(2),
						WordTypes.getEnumByValue(cursor.getString(3)),
						WordLevels.getEnumByValue(cursor.getString(4)),
						Integer.parseInt(cursor.getString(5)));
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return contact;
	}

	public List<Word> getWordByEng(String _eng) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_ENG + " LIKE ?",
					new String[] { _eng }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByEngAndType(String _eng, Integer _type) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_ENG + " LIKE ? AND " + TABLE_WORDS_TYPE + "=?",
					new String[] { _eng, _type.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByEng(String _eng, Integer _count) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_ENG + " LIKE ? AND LIMIT ?",
					new String[] { _eng, _count.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByTr(String _tr) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_TR + " LIKE ?",
					new String[] { _tr }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByTr(String _tr, Integer _count) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_TR + " LIKE ? AND LIMIT ?",
					new String[] { _tr, _count.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByLevel(Integer _level) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_LEVEL + "=?",
					new String[] { _level.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByLevel(Integer _level, Integer _count) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_LEVEL + "=? LIMIT ?",
					new String[] { _level.toString(), _count.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByType(Integer _type) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_TYPE + "=?",
					new String[] { _type.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByType(Integer _type, Integer _count) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_TYPE + "=? AND LIMIT ?",
					new String[] { _type.toString(), _count.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByCategory(Integer _category) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_CATEGORY + "=?",
					new String[] { _category.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Word> getWordByCategory(Integer _category, Integer _count) {

		List<Word> wordsList = new ArrayList<Word>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_WORDS, new String[] { TABLE_WORDS_ID,
					TABLE_WORDS_ENG, TABLE_WORDS_TR, TABLE_WORDS_TYPE, TABLE_WORDS_LEVEL, TABLE_WORDS_CATEGORY }, TABLE_WORDS_CATEGORY + "=? AND LIMIT ?",
					new String[] { _category.toString(), _count.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Word word = new Word(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2),
							WordTypes.getEnumByValue(cursor.getString(3)),
							WordLevels.getEnumByValue(cursor.getString(4)),
							Integer.parseInt(cursor.getString(5)));

					wordsList.add(word);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return wordsList;
	}

	public List<Category> getAllCategories() {

		List<Category> categoriesList = new ArrayList<Category>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES;

		try {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Category category = new Category();

					category.setId(Integer.parseInt(cursor.getString(0)));
					category.setCategoryName(cursor.getString(1));
					category.setCategoryDescription(cursor.getString(2));

					categoriesList.add(category);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return categoriesList;
	}

	public List<Category> getCategoriesByCount(Integer _count) {

		List<Category> categoriesList = new ArrayList<Category>();

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { TABLE_CATEGORIES_ID,
					TABLE_CATEGORIES_NAME, TABLE_CATEGORIES_DESCRIPTION }, " LIMIT ?",
					new String[] { _count.toString() }, null, null, null);

			if (cursor.moveToFirst()) {
				do {
					Category category = new Category(
							Integer.parseInt(cursor.getString(0)),
							cursor.getString(1),
							cursor.getString(2));

					categoriesList.add(category);

				} while (cursor.moveToNext());
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return categoriesList;
	}

	public Category getCategoryByID(int _id){

		Category category = null;

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { TABLE_CATEGORIES_ID,
					TABLE_CATEGORIES_NAME, TABLE_CATEGORIES_DESCRIPTION }, TABLE_CATEGORIES_ID + "=?",
					new String[] { String.valueOf(_id) }, null, null, null);

			if (cursor != null){
				cursor.moveToFirst();
				category = new Category(
						Integer.parseInt(cursor.getString(0)),
						cursor.getString(1),
						cursor.getString(2));
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return category;
	}

	public Category getCategoryByName(String _name){

		Category category = null;

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { TABLE_CATEGORIES_ID,
					TABLE_CATEGORIES_NAME, TABLE_CATEGORIES_DESCRIPTION }, TABLE_CATEGORIES_NAME + "=?",
					new String[] { String.valueOf(_name) }, null, null, null);

			if (cursor != null){
				cursor.moveToFirst();
				category = new Category(
						Integer.parseInt(cursor.getString(0)),
						cursor.getString(1),
						cursor.getString(2));
			}
			db.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return category;
	}

	//-----END-----
	//SELECT OPERATIONS





	//UPDATE OPERATIONS
	//-----START-----


	public int updateWord(Word _word) {

		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(TABLE_WORDS_ID, _word.getId());
			values.put(TABLE_WORDS_ENG, _word.getEng());
			values.put(TABLE_WORDS_TR, _word.getTr());
			values.put(TABLE_WORDS_TYPE, _word.getType().getID());
			values.put(TABLE_WORDS_LEVEL, _word.getLevel().getID());
			values.put(TABLE_WORDS_CATEGORY, _word.getCategory());

			// updating row
			return db.update(TABLE_WORDS, values, TABLE_WORDS_ID + " = ?",
					new String[] { String.valueOf(_word.getId()) });
		} 
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int updateCategory(Category _category) {

		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(TABLE_CATEGORIES_ID, _category.getId());
			values.put(TABLE_CATEGORIES_NAME, _category.getCategoryName());
			values.put(TABLE_CATEGORIES_DESCRIPTION, _category.getCategoryDescription());

			// updating row
			return db.update(TABLE_CATEGORIES, values, TABLE_CATEGORIES_ID + " = ?",
					new String[] { String.valueOf(_category.getId()) });
		} 
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	//-----END-----
	//UPDATE OPERATIONS





	//DELETE OPERATIONS
	//-----START-----

	public Boolean deleteWord(Word _word) {

		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_WORDS, TABLE_WORDS_ID + " = ?",
					new String[] { String.valueOf(_word.getId()) });
			db.close();
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean deleteCategory(Category _category) {

		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_CATEGORIES, TABLE_CATEGORIES_ID + " = ?",
					new String[] { String.valueOf(_category.getId()) });
			db.close();
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	//-----END-----
	//DELETE OPERATIONS







	public void emptyWordsTable(){
		// Delete All Query
		String deleteQuery = "DELETE  * FROM " + TABLE_WORDS;

		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.rawQuery(deleteQuery, null);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
	}

	public Integer getWordsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_WORDS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	public Integer getCategoriesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CATEGORIES;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}


}
