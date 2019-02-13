package com.example.adeeliftikhar.mysqldatabase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adeeliftikhar.mysqldatabase.singletonpkg.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    EditText editTextName, editTextEmail, editTextPhoneNumber, editTextAddress, editTextPassword, editTextReEnterPassword;
    String incomingName, incomingEmail, incomingPhoneNumber, incomingAddress, incomingPassword, incomingReEnterPassword;
    Button buttonSignUp, buttonShowUserData;
    AlertDialog.Builder builder;
    //    Get IP Address of Computer to access the LocalHost...
    String regURL = "http://192.168.7.121/phpfiles/Register.php";
//    String regURL = "http://192.168.1.22/phpfiles/Register.php";
//    String regURL = "http://10.117.29.216/phpfiles/Register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        builder = new AlertDialog.Builder(RegistrationActivity.this);
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextAddress = findViewById(R.id.edit_text_address);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextReEnterPassword = findViewById(R.id.edit_text_re_enter_password);
        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonShowUserData = findViewById(R.id.button_show_user_data);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingName = editTextName.getText().toString();
                incomingEmail = editTextEmail.getText().toString();
                incomingPhoneNumber = editTextPhoneNumber.getText().toString();
                incomingAddress = editTextAddress.getText().toString();
                incomingPassword = editTextPassword.getText().toString();
                incomingReEnterPassword = editTextReEnterPassword.getText().toString();
                if (incomingName.isEmpty() || incomingEmail.isEmpty() || incomingPhoneNumber.isEmpty() || incomingAddress.isEmpty() || incomingPassword.isEmpty() || incomingReEnterPassword.isEmpty()) {
                    builder.setTitle("Something went Wrong!");
                    builder.setMessage("Fill All Fields...");
                    displayAlert("input_error");
                } else if (!(incomingPassword.equals(incomingReEnterPassword))) {
                    builder.setTitle("Something went Wrong!");
                    builder.setMessage("Passwords not matching...");
                    displayAlert("input_error");
                } else if (incomingPassword.equals(incomingReEnterPassword)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, regURL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrationActivity.this);
                                        builder1.setTitle("Sever Response");
                                        builder1.setMessage(message);
                                        displayAlert(code);

//                                        JSONArray jsonArray = new JSONArray(response);
//                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                        String code = jsonObject.getString("code");
//                                        String message = jsonObject.getString("message");
//                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegistrationActivity.this);
//                                        builder1.setTitle("Sever Response");
//                                        builder1.setMessage(message);
//                                        displayAlert(code);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Log.e("TAG", "Error" + error.getMessage());
                            Toast.makeText(RegistrationActivity.this, "Error Occurred" + error.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }) {
                        //                            This method pass the data to String Request...
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("register_name", incomingName);
                            params.put("register_email", incomingEmail);
                            params.put("register_phone_no", incomingPhoneNumber);
                            params.put("register_address", incomingAddress);
                            params.put("register_password", incomingPassword);
                            return params;
//                            return super.getParams();
                        }

//                            Toast.makeText(RegistrationActivity.this, "Data has been entered", Toast.LENGTH_SHORT).show();
                    };
                    MySingleton.getMySingletonClassObject(RegistrationActivity.this).addToRequestQueue(stringRequest);
                }
            }
        });
    }

    private void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (code) {
                    case "input_error":
                        editTextPassword.setText("");
                        editTextReEnterPassword.setText("");
                        break;
                    case "reg_success":
                        Toast.makeText(RegistrationActivity.this, "reg_success", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case "reg_failed":
                        Toast.makeText(RegistrationActivity.this, "reg_failed", Toast.LENGTH_SHORT).show();
                        editTextName.setText("");
                        editTextEmail.setText("");
                        editTextPhoneNumber.setText("");
                        editTextAddress.setText("");
                        editTextPassword.setText("");
                        editTextReEnterPassword.setText("");
                        break;
                    default:
                        Toast.makeText(RegistrationActivity.this, "Some Thing Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}