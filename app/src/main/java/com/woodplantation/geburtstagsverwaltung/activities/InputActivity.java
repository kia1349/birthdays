package com.woodplantation.geburtstagsverwaltung.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.woodplantation.geburtstagsverwaltung.R;

import java.util.Calendar;

/**
 * Created by Sebu on 10.03.2016.
 * Contact: sebastian.oltmanns.developer@googlemail.com
 */
public abstract class InputActivity extends AppCompatActivity {


	protected EditText firstNameEdit;
	protected EditText lastNameEdit;
	protected EditText birthdayDayEdit, birthdayMonthEdit, birthdayYearEdit;
	protected EditText othersEdit;

	protected Calendar birthday;

	private class FocusNextViewTextWatcher implements TextWatcher {
		int count;
		View view;
		FocusNextViewTextWatcher(int count, View view) {
			this.count = count;
			this.view = view;
		}
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
		@Override
		public void afterTextChanged(Editable editable) {
			if (editable.length() == count) view.requestFocus();
		}
	}

	protected void onCreate(Bundle savedInstanceState, int contentView) {
		super.onCreate(savedInstanceState);
		setContentView(contentView);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		firstNameEdit = findViewById(R.id.text_input_firstname);
		lastNameEdit = findViewById(R.id.text_input_lastname);
		birthdayDayEdit = findViewById(R.id.text_input_birthday_day);
		birthdayMonthEdit = findViewById(R.id.text_input_birthday_month);
		birthdayYearEdit = findViewById(R.id.text_input_birthday_year);
		othersEdit = findViewById(R.id.text_input_others);

        birthdayDayEdit.addTextChangedListener(new FocusNextViewTextWatcher(2, birthdayMonthEdit));
		birthdayMonthEdit.addTextChangedListener(new FocusNextViewTextWatcher(2, birthdayYearEdit));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			setResult(RESULT_CANCELED);
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void changeBirthday(View view) {
		DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				birthday.set(year, monthOfYear, dayOfMonth);
				birthdayDayEdit.setText(String.valueOf(dayOfMonth));
				birthdayMonthEdit.setText(String.valueOf(monthOfYear+1));
				birthdayYearEdit.setText(String.valueOf(year));
			}
		};

		DatePickerDialog datePickerDialog = new DatePickerDialog(this, callback,
				birthday.get(Calendar.YEAR),
				birthday.get(Calendar.MONTH),
				birthday.get(Calendar.DAY_OF_MONTH));

		datePickerDialog.show();
	}

	protected boolean checkInput() {
        String birthdayDay = birthdayDayEdit.getText().toString();
        String birthdayMonth = birthdayMonthEdit.getText().toString();
        String birthdayYear = birthdayYearEdit.getText().toString();
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();

        boolean flag = true;
        int messageResource = 0;

        //check for valid birthday: first, check if all fields are set
		if (TextUtils.isEmpty(birthdayDay)
				|| TextUtils.isEmpty(birthdayMonth)
				|| TextUtils.isEmpty(birthdayYear)) {
            messageResource = R.string.wrong_input_birthday;
            flag = false;
        } else {
            //all fields are set. check if the actual values are fine, e.g. 41.15.2088 is not valid
            int birthdayD = Integer.parseInt(birthdayDay);
            int birthdayM = Integer.parseInt(birthdayMonth);
            int birthdayY = Integer.parseInt(birthdayYear);
            Calendar inputBirthday = Calendar.getInstance();
            inputBirthday.set(birthdayY, birthdayM-1, birthdayD);
            if (inputBirthday.after(Calendar.getInstance())
                    || inputBirthday.get(Calendar.YEAR) != birthdayY
                    || inputBirthday.get(Calendar.MONTH) != birthdayM-1
                    || inputBirthday.get(Calendar.DAY_OF_MONTH) != birthdayD) {
                messageResource = R.string.wrong_input_birthday;
                flag = false;
            } else if (TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName)) {
                messageResource = R.string.wrong_input_no_name;
                flag = false;
            }
        }

        if (!flag) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(messageResource);
            builder.setTitle(R.string.wrong_input_title);
            builder.setPositiveButton(R.string.ok, null);
            builder.show();
            return false;
        }

        return true;
    }

	/**
	 * only call this function when checkInput() was called before AND returned true !
	 */
	protected void setBirthdayFromEditTexts() {
		String birthdayDay = birthdayDayEdit.getText().toString();
		String birthdayMonth = birthdayMonthEdit.getText().toString();
		String birthdayYear = birthdayYearEdit.getText().toString();
		int birthdayD = Integer.parseInt(birthdayDay);
		int birthdayM = Integer.parseInt(birthdayMonth);
		int birthdayY = Integer.parseInt(birthdayYear);

		birthday.set(birthdayY, birthdayM-1, birthdayD);

	}
}
