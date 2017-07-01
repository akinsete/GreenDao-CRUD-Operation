package com.akinsete.ourgrocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.akinsete.ourgrocerylist.db.DaoSession;
import com.akinsete.ourgrocerylist.db.Grocery;
import com.akinsete.ourgrocerylist.db.GroceryDao;

public class ModifyGroceryList extends AppCompatActivity {

	Grocery grocery;
	DaoSession daoSession;

	EditText name,quantity;
	Button btn_save;

	boolean createNew = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_grocery_list);

		name = (EditText)findViewById(R.id.name);
		quantity = (EditText)findViewById(R.id.quantity);
		btn_save = (Button) findViewById(R.id.btn_save);

		daoSession = ((AppController) getApplication()).getDaoSession();

		handleIntent(getIntent());

		setClickEventListener();
	}


	private void handleIntent(Intent intent) {
		createNew = intent.getBooleanExtra("create",false);

		//// This means we are editing a grocery item
		if(!createNew){
			grocery = (Grocery)intent.getSerializableExtra("grocery");
			name.setText(grocery.getName());
			quantity.setText(grocery.getQuantity());
		}
	}


	private void setClickEventListener() {
		btn_save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(createNew){
					insetItem();
				}else{
					updateItem(grocery.getId());
				}
			}
		});
	}


	private void updateItem(long id){
		GroceryDao groceryDao = daoSession.getGroceryDao();
		Grocery grocery = new Grocery();
		grocery.setId(id);
		grocery.setName(name.getText().toString());
		grocery.setQuantity(Integer.parseInt(quantity.getText().toString()));
		groceryDao.saveInTx(grocery);
		Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
		finish();
	}

	private void insetItem(){
		GroceryDao groceryDao = daoSession.getGroceryDao();
		Grocery grocery = new Grocery();
		grocery.setName(name.getText().toString());
		grocery.setQuantity(Integer.parseInt(quantity.getText().toString()));
		groceryDao.insert(grocery);
		Toast.makeText(this, "Item inserted", Toast.LENGTH_SHORT).show();
		finish();
	}
}
