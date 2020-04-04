package br.com.treinoFireBase;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NovoUsuarioActivity extends AppCompatActivity {

    private EditText loginNovoUsuarioEditText;
    private EditText senhaNovoUsuarioEditText;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);
        loginNovoUsuarioEditText = findViewById(R.id.loginNovoUsuario);
        senhaNovoUsuarioEditText = findViewById(R.id.senhaNovoUsuario);
        mAuth = FirebaseAuth.getInstance();

    }

    public void criarNovoUsuario(View view) {
        String login = loginNovoUsuarioEditText.getText().toString();
        String senha = senhaNovoUsuarioEditText.getText().toString();
        mAuth.createUserWithEmailAndPassword(login, senha).addOnSuccessListener(
                (result) -> {
                    Toast.makeText(this, result.getUser().toString() ,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(error -> error.printStackTrace());
    }

}
