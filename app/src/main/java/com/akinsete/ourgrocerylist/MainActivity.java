package com.akinsete.ourgrocerylist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.akinsete.ourgrocerylist.db.DaoSession;
import com.akinsete.ourgrocerylist.db.Grocery;
import com.akinsete.ourgrocerylist.db.GroceryDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


	ListView listView;
	List<Grocery> groceries = new ArrayList<>();

	DaoSession daoSession;
	ArrayAdapter<Grocery> groceryArrayAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list_view);

		daoSession = ((AppController) getApplication()).getDaoSession();

		setupListView();
	}


	@Override
	protected void onResume() {
		super.onResume();

		fetchGroceryList();
	}


	private void setupListView() {
		groceryArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,groceries);
		listView.setAdapter(groceryArrayAdapter);

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				showOptions(position);

				return false;
			}
		});
	}


	private void showOptions(int position) {
		final Grocery selectedGroceryItem = groceries.get(position);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		String[] options = new String[2];

		options[0] = "Edit " + selectedGroceryItem.getName();
		options[1] = "Delete " + selectedGroceryItem.getName();

		alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0){
					proceedToUpdateItem(selectedGroceryItem);
				}else if(which == 1){
					deleteGroceryItem(selectedGroceryItem.getId());
				}

			}
		});
		alertDialogBuilder.create().show();
	}


	private void fetchGroceryList(){
		groceries.clear();
		//// Get the entity dao we need to work with.
		GroceryDao groceryDao = daoSession.getGroceryDao();

		//// Load all items
		groceries.addAll(groceryDao.loadAll());

		/// Notify our adapter of changes
		groceryArrayAdapter.notifyDataSetChanged();
	}


	private void deleteGroceryItem(long id){
		//// Get the entity dao we need to work with.
		GroceryDao groceryDao = daoSession.getGroceryDao();
		/// perform delete operation
		groceryDao.deleteByKey(id);

		fetchGroceryList();
	}


	private void proceedToUpdateItem(Grocery grocery){
		// Pass grocery id to the next screen
		Intent intent = new Intent(this,ModifyGroceryList.class);
		intent.putExtra("create",false);
		intent.putExtra("grocery", (Serializable) grocery);
		startActivity(intent);
	}

	public void addNewItem(View view) {
		// Go to add item activity
		Intent intent = new Intent(this,ModifyGroceryList.class);
		intent.putExtra("create",true);
		startActivity(intent);
	}
}
