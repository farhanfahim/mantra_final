package com.tekrevol.mantra.helperclasses.validator;

import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;

/**
 * Created by khanhamza on 08-Mar-17.
 */

public class CnicValidation extends Validator {

    private EditText edtConfirmPassword;

    public CnicValidation() {
        super("CNIC number should contain 13 digits");
    }


    @Override
    public boolean isValid(EditText et) {
        return et.getText().toString().length() == 15;
    }


}
