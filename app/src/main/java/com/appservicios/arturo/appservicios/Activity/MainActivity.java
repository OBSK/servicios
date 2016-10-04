package com.appservicios.arturo.appservicios.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.appservicios.arturo.appservicios.R;
import com.appservicios.arturo.appservicios.Ref.Constant;
import com.appservicios.arturo.appservicios.registroActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout txtCorreo,txtContraseña;
    private EditText campoCorreo,campoContraseña;
    private CheckBox guardar;
    Button btnRegistrase,btnLogin;
    Firebase myConnectionsStatusRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Referencia a los textinputlayout
        txtCorreo = (TextInputLayout) findViewById(R.id.til_correo);
        txtContraseña = (TextInputLayout) findViewById(R.id.til_contraseña);
        //Referencia a los editText
        campoCorreo = (EditText) findViewById(R.id.campo_correo);
        campoContraseña = (EditText) findViewById(R.id.campo_contraseña);
        //Referencia al boton registrarse
        btnRegistrase=(Button)findViewById(R.id.btnRegistrarse);
        btnLogin=(Button)findViewById(R.id.btnIngresar);
        //Referencia al checkbox
        guardar=(CheckBox)findViewById(R.id.checkBox);
        Firebase.setAndroidContext(this);

        //Implementamos los TextWatcher
        campoCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Cuando detecta el cambio el checkbox pasa a true
                guardar.setChecked(true);
                txtCorreo.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        campoContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContraseña.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //Iniciar nueva activity de registro
        btnRegistrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registro = new Intent(getApplicationContext(), registroActivity.class);
                startActivity(registro);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view,"Verificando", Snackbar.LENGTH_SHORT)
                        .show();
                if (guardar.isChecked()==true){
                    guardarconfiguracion();
                }else{
                    limpiarconfiguracion();
                }
                validarDatos();
            }
        });
    }
    public void guardarconfiguracion(){
        SharedPreferences preferencias=getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("mail", campoCorreo.getText().toString());
        editor.putString("pass",campoContraseña.getText().toString());
        editor.commit();
    }

    public void cargarconfiguracion(){
        //Referencia al SharedPreferences
        SharedPreferences prefe=getSharedPreferences("datos",Context.MODE_PRIVATE);
        //Al iniciar toma como parametro el valor de mail, si no lo tiene se convierte en null
        campoCorreo.setText(prefe.getString("mail",""));
        campoContraseña.setText(prefe.getString("pass",""));
        if (campoCorreo.length()>1){
            guardar.setChecked(true);
        }else{
            guardar.setChecked(false);
        }
    }
    public void limpiarconfiguracion(){
        SharedPreferences prefe=getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putString("mail","");
        editor.putString("pass","");
        editor.commit();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    public void onStart()
    {
        super.onStart();
        cargarconfiguracion();
    }
    private boolean CorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            campoCorreo.setError(getText(R.string.txt_errorcorreo));
            return false;
        } else {
            campoCorreo.setError(null);
        }
        return true;
    }
    private boolean PasswordValido(String password){
        if(password.isEmpty()){
            campoContraseña.setError(getText(R.string.txt_errorpassword));
            return false;
        }else{
            campoContraseña.setError(null);
        }
        return true;
    }
    protected Boolean estadoConectado(){
        //Verifica que la conexion sea estable, si no lo es manda a configurar
        if (conectadoWifi()||conectadoMovil()){
            return true;
        }else{
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.error_wifi)
                    .setTitle(getString(R.string.login_error))
                    .setPositiveButton(R.string.verificar, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
            builder.setNegativeButton(R.string.Ahora_no,null);
            AlertDialog dialog=builder.create();
            dialog.show();
        }
        return false;
    }
    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;

    }

    protected Boolean conectadoMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    private void validarDatos() {
        String correo=campoCorreo.getText().toString();
        String password=campoContraseña.getText().toString();
        boolean a = CorreoValido(correo);
        boolean b = PasswordValido(password);
        boolean c= estadoConectado();
        if (a && b && c) {
            Firebase authenticateUser = new Firebase(Constant.FIREBASE_URL); // Get app main firebase url

            authenticateUser.authWithPassword(correo,password, authResultHandler);
            //Toast.makeText(MainActivity.this,"Hola q ase", Toast.LENGTH_SHORT).show();
        }
        }
    Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
        @Override
        public void onAuthenticated(AuthData authData) {
            // Authenticated successfully with payload authData
            // Go to main activity
            Intent intent=new Intent(MainActivity.this, startActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            // Authenticated failed, show Firebase error to user
            Toast.makeText(MainActivity.this, getText(R.string.error_login), Toast.LENGTH_SHORT).show();
        }
    };








}
