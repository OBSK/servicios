package com.appservicios.arturo.appservicios;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appservicios.arturo.appservicios.Activity.startActivity;
import com.appservicios.arturo.appservicios.Ref.AvatarID;
import com.appservicios.arturo.appservicios.Ref.Constant;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class registroActivity extends Activity {
    private TextInputLayout lblNombre,lblCelular,lblCorreo,lblPassword;
    private EditText txtNombre,txtCelular,txtCorreo,txtPassword;
    private static final String TAG=registroActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Hacemos referencia a los textInputLayout

    lblNombre=(TextInputLayout)findViewById(R.id.til_nombre);
    lblCelular=(TextInputLayout)findViewById(R.id.til_telefono);
    lblCorreo=(TextInputLayout)findViewById(R.id.til_correo);
    lblPassword=(TextInputLayout)findViewById(R.id.til_password);
        //Hacmos referencia a los editText
    txtNombre=(EditText)findViewById(R.id.txt_registronombre);
    txtCelular=(EditText)findViewById(R.id.txt_telefono);
    txtCorreo=(EditText)findViewById(R.id.txt_correo);
    txtPassword=(EditText)findViewById(R.id.txt_password);
        Firebase.setAndroidContext(this);
        txtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lblNombre.setCounterEnabled(true);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               NombreValido(String.valueOf(s));
                //Establece un mensaje de error que se mostrará a continuación nuestra EditText
                // . Si el errores null, se borrará el mensaje de error.
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lblCelular.setCounterEnabled(true);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CelularValido(String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CorreoValido(String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PasswordValido(String.valueOf(s));
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    //Referencia al boton de registro
        Button botonAceptar = (Button) findViewById(R.id.btn_enviarregistro);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();

            }
        });
    }
    //Referencia a la validacion del nombre
    private boolean NombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z-ñ ]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() >=30) {
            lblNombre.setError(getText(R.string.txt_errornombre));
            return false;
        } else {
            lblNombre.setError(null);
        }
        return true;
    }
    //Referencia a la validacion del celuar
    private boolean CelularValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches() || telefono.length()<9) {
            char[] arrayChar=telefono.toCharArray();
    //Implementamos un contador de los digitos ingresados
            for (int i=-1;i<arrayChar.length;i++){
                int contador=8-i;
                lblCelular.setError(getText(R.string.txt_errorcelular1)+" "+contador+" " +getText(R.string.txt_errorcelular2));
            }
            return false;
        } else {
            lblCelular.setError(null);
        }
        return true;
    }
    //Referencia a la validacion de correo
    private boolean CorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            lblCorreo.setError(getText(R.string.txt_errorcorreo));
            return false;
        } else {
            lblCorreo.setError(null);
        }
        return true;
    }
    //Referencia a la validacion de contraseña
    private boolean PasswordValido(String password){
        if(password.isEmpty()||password.length()>20){
            lblPassword.setError(getText(R.string.txt_errorpassword));
            return false;
        }else{
            lblPassword.setError(null);
        }
     return true;
    }
    //Instanciacion de las validaciones
    private void validarDatos() {
    String nombre=lblNombre.getEditText().getText().toString();
    String celular=lblCelular.getEditText().getText().toString();
    String correo=lblCorreo.getEditText().getText().toString();
    String password=lblPassword.getEditText().getText().toString();

        boolean a = NombreValido(nombre);
        boolean b = CelularValido(celular);
        boolean c = CorreoValido(correo);
        boolean d = PasswordValido(password);
    //Si la condicion es verdadera se procede a guardar el registro
        if (a && b && c && d) {
            final Firebase register = new Firebase(Constant.FIREBASE_URL);
            final String finalnombre=nombre;
            final String finalcorreo=correo;
            final String finalpassword=password;
            register.createUser(correo,password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> stringObjectMap) {
                    Toast.makeText(registroActivity.this, getText(R.string.txt_registro), Toast.LENGTH_SHORT).show();
                    register.authWithPassword(finalcorreo,finalpassword, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put(Constant.KEY_PROVIDER,authData.getProvider());
                            map.put(Constant.KEY_NAME,finalnombre);
                            map.put(Constant.KEY_USER_EMAIL,(String)authData.getProviderData().get(Constant.KEY_EMAIL));
                            map.put(Constant.KEY_AVATAR_ID,AvatarID.generateRandomAvatarForUser());
                            map.put(Constant.CHILD_CONNECTION, Constant.KEY_ONLINE);
                            long createTime=new Date().getTime();
                            map.put(Constant.KEY_TIMESTAMP,String.valueOf(createTime));
                            register.child(Constant.CHILD_USERS).child(Constant.CHILD_TIPO).child(authData.getUid()).setValue(map);
                            Intent intent = new Intent(registroActivity.this, startActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }


                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(registroActivity.this, "Error", Toast.LENGTH_SHORT).show();


                        }
                    });

                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    showErrorMessageToUser(firebaseError.getMessage());
                }
            });



           /* Ref.createUser(correo, password, new Firebase.ValueResultHandler<Map<String, Object>>() {

                @Override
                public void onSuccess(Map<String, Object> result) {
                    Toast.makeText(registroActivity.this, "Registrado con exito", Toast.LENGTH_SHORT).show();
                    registerMChatUser.authWithPassword(correo, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put(Constant.KEY_PROVIDER,authData.getProvider());
                            map.put(Constant.KEY_NAME,nombre);
                            map.put(Constant.KEY_USER_EMAIL,(String) authData.getProviderData().get(Constant.KEY_EMAIL));
                            map.put(Constant.CHILD_CONNECTION,Constant.KEY_ONLINE);
                            map.put(Constant.KEY_AVATAR_ID, AvatarID.generateRandomAvatarForUser());
                            long createTime = new Date().getTime();
                            map.put(Constant.KEY_TIMESTAMP, String.valueOf(createTime));
                            registerMChatUser.child(Constant.CHILD_USERS).child(Constant.CHILD_TIPO).child(authData.getUid()).setValue(map);
                            Intent intent = new Intent(registroActivity.this, startActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(registroActivity.this, "Oh no! ocurrio un error", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

            }
                @Override
                public void onError(FirebaseError firebaseError) {
                    showErrorMessageToUser(firebaseError.getMessage());
                }
            });
    }
}
    private void showErrorMessageToUser(String errorMessage) {
        AlertDialog.Builder builder=new AlertDialog.Builder(registroActivity.this);
        builder.setMessage(errorMessage)
                .setTitle(getString(R.string.login_error))
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog=builder.create();
        dialog.show();*/

    }

    }
    private void showErrorMessageToUser(String errorMessage){
        //Create an AlertDialog to show error message
        AlertDialog.Builder builder=new AlertDialog.Builder(registroActivity.this);
        builder.setMessage(errorMessage)
                .setTitle(getString(R.string.login_error))
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

}
