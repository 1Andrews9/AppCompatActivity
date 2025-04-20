package co.edu.uniminuto.appcompatactivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import co.edu.uniminuto.appcompatactivity.entities.User;
import co.edu.uniminuto.appcompatactivity.repository.UserRepository;

public class MainActivity extends AppCompatActivity {
    private Context context;

    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContraseña;

    private ListView listUsers;

    private SQLiteDatabase sqLiteDatabase;

    private Button btnGuardar;
    private Button btnListUsers;

    private int documento;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        begin();
        btnGuardar.setOnClickListener(this::CreateUser);
        btnListUsers.setOnClickListener(this::listUsers);

    }
    private void CreateUser(View view) {
        capData();
        User user = new User(documento, nombres, apellidos, usuario, pass);
        UserRepository userRepository = new UserRepository(context, view);
        userRepository.insertUser(user);
        //invocacion del metodo para listar los usuarios
    }
    private void listUsers(View view) {
        UserRepository userRepository = new UserRepository(context, view);
        ArrayList<User> list = userRepository.getUserList();
        ArrayAdapter<User> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
        this.listUsers.setAdapter(adapter);

    }
    private void capData(){
        //Validaciones --- Regex
        this.documento=Integer.parseInt(this.etDocumento.getText().toString());
        System.out.println(this.documento);
        this.nombres=this.etNombres.getText().toString();
        this.apellidos=this.etApellidos.getText().toString();
        this.usuario=this.etUsuario.getText().toString();
        this.pass=this.etContraseña.getText().toString();
    }
    private void saveUser(){
        capData();
    }


    private void begin (){
            this.etNombres=findViewById(R.id.etNombres);
            this.etApellidos=findViewById(R.id.etApellidos);
            this.etDocumento=findViewById(R.id.etDocumento);
            this.etUsuario=findViewById(R.id.etUsuario);
            this.etContraseña=findViewById(R.id.etContraseña);
            this.listUsers=findViewById(R.id.lvLista);
            this.btnGuardar=findViewById(R.id.btnRegister);
            this.btnListUsers=findViewById(R.id.btnListar);
            this.context=this;
    }


}
