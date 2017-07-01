package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGenerator {
	public static void main(String[] args) {
		Schema schema = new Schema(1, "com.akinsete.ourgrocerylist.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
		schema.enableKeepSectionsByDefault();

		addTables(schema);

		try {
			new DaoGenerator().generateAll(schema,"./app/src/main/java");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addTables(final Schema schema) {
		addGroceryEntity(schema);
	}

	// This is use to describe the columns of your table
	private static Entity addGroceryEntity(final Schema schema) {
		Entity grocery = schema.addEntity("Grocery");
		grocery.addIdProperty().primaryKey().autoincrement();
		grocery.addStringProperty("name").notNull();
		grocery.addIntProperty("quantity").notNull();
		grocery.addStringProperty("status");
		return grocery;
	}
}
