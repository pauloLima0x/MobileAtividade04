package br.com.treinoFireBase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText loginEditText;
    private EditText senhaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginEditText = findViewById(R.id.loginEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        loginEditText.setText("teste@gmail.com");
        senhaEditText.setText("123456");
        mAuth = FirebaseAuth.getInstance();
    }

   public void irParaCadastro(View view) {
        startActivity (new Intent(this, NovoUsuarioActivity.class));
   }
   public void fazerLogin (View view) {
        String login = loginEditText.getEditableText().toString();
        String senha = senhaEditText.getEditableText().toString();
        mAuth.signInWithEmailAndPassword(login, senha)
                .addOnSuccessListener(
                        (result) -> {
                            startActivity(new Intent (this, ChatActivity.class));
                        }
                ).addOnFailureListener( (exception) -> {
                    exception.printStackTrace();
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        } );
   }

   

}
