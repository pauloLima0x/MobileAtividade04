package br.com.treinoFireBase;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    private RecyclerView mensagensRecycleView;
    private ChatAdapter adapter;
    private List<Mensagem> mensagens;
    private EditText mensagemEditText;
    private FirebaseUser fireUser;
    private CollectionReference mMsgsReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mensagensRecycleView =
                findViewById(R.id.mensagemRecycleView);
        mensagens = new ArrayList<>();
        adapter = new ChatAdapter(mensagens, this);
        mensagensRecycleView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        mensagensRecycleView.setLayoutManager(linearLayoutManager);
        mensagemEditText = findViewById(R.id.mensagemEditText);


    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView dataNomeTextView;
        TextView mensagemTextView;
        ChatViewHolder (View v) {
            super (v);
            this.dataNomeTextView =
                    v.findViewById(R.id.dataNomeTextView);
            this.mensagemTextView = findViewById(R.id.mensagemEditText);
        }
    }

    class ChatAdapter extends RecyclerView.Adapter
            <ChatViewHolder> {
        private List<Mensagem> mensagens;
        private Context context;
        ChatAdapter (List<Mensagem> mensagens, Context context) {
            this.mensagens = mensagens;
            this.context = context;
        }
        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                 parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.list_item, parent, false);
            return new ChatViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder,
                                     int position) {
            Mensagem m = mensagens.get(position);

            holder.dataNomeTextView.setText(context.getString(R.string.data_nome,
                   DateHelér.format(m.getData()), m.getUsuario() ));
                holder.mensagemTextView.setText(m.getTexto());
                mensagemEditText.setText("");


        }
        public int getItemCount() {
            return mensagens.size();
        }

    }




    private void setupFirebase() {
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        mMsgsReference =
                FirebaseFirestore.getInstance().collection("mensagens");
        getRemoteMsgs();
    }


    protected void onStart() {
        super.onStart();
        setupFirebase();
    }

    private void getRemoteMsgs () {
        mMsgsReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                      mensagens.clear();
                      for(DocumentSnapshot doc :
                      queryDocumentSnapshots.getDocuments()) {
                          Mensagem incomingMsg = doc.toObject(Mensagem.class);
                          mensagens.add(incomingMsg);
                      }
                      Collections.sort(mensagens);
                      adapter.notifyDataSetChanged();
                    }
                } );
    }

    public void enviarMensagem(View view) {
        String mensagem = mensagemEditText.getText().toString();
        Mensagem m = new Mensagem(fireUser.getEmail(), new Date(),
                mensagem);
        esconderTeclado(view);
        mMsgsReference.add(m);
    }

    private void esconderTeclado (View v) {
        InputMethodManager ims =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        ims.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }




    }






class Mensagem implements  Comparable<Mensagem> {
    private String usuario;
    private Date data;
    private String texto;
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(Date data) {
        this.texto = texto;
    }
    public Mensagem(String usuario, Date data, String texto) {
        this.usuario = usuario;
        this.data = data;
        this.texto = texto;
    }
    public Mensagem () {}
    @Override
    public int compareTo(Mensagem mensagem) {
        return this.data.compareTo(mensagem.data);
    }



}


class DateHelér {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    public static String format(Date date) {
        return sdf.format(date);
    }
}



