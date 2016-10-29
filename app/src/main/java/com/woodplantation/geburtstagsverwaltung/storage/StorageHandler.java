package com.woodplantation.geburtstagsverwaltung.storage;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Sebu on 10.03.2016.
 * Contact: sebastian.oltmanns.developer@googlemail.com
 */
public class StorageHandler {

	private Context context;
	private static final String filePath = "data";

	public StorageHandler(Context context) {
		this.context = context;
	}

	public void saveData(ArrayList<DataSet> data) {
		try {
			long time = System.currentTimeMillis();
			FileOutputStream fos = context.openFileOutput(filePath, Context.MODE_PRIVATE);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ObjectOutputStream output = new ObjectOutputStream(bos);

			output.writeObject(data);

			output.close();
			bos.close();
			fos.close();
			Log.d("storagehandler", "savedata, size: " + data.size() + " time needed: " + (System.currentTimeMillis() - time));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<DataSet> loadData() {
		try {
			long time = System.currentTimeMillis();
			FileInputStream fis = context.openFileInput(filePath);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ObjectInputStream input = new ObjectInputStream(bis);

			ArrayList<DataSet> result = (ArrayList<DataSet>) input.readObject();

			input.close();
			bis.close();
			fis.close();
			Log.d("storagehandler", "loaddata, size: " + result.size() + " time needed: " + (System.currentTimeMillis() - time));
			return result;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<DataSet>();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}