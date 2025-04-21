package co.edu.uniminuto.appcompatactivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

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

    private Button btnGuardar;
    private Button btnListUsers;
    private Button btnActualizar;
    private Button btnEliminar;
    private Button btnBuscar;
    private Button btnLimpiar;

    private int documento;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String pass;

    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        begin();

        btnGuardar.setOnClickListener(this::CreateUser);
        btnListUsers.setOnClickListener(this::listUsers);
        btnActualizar.setOnClickListener(this::updateUser);
        btnEliminar.setOnClickListener(this::deleteUser);
        btnBuscar.setOnClickListener(this::BuscarUser);
        btnLimpiar.setOnClickListener(v -> {
            etDocumento.setText("");
            etUsuario.setText("");
            etNombres.setText("");
            etApellidos.setText("");
            etContraseña.setText("");
        });
    }

    private void clearUserFields() {
        etDocumento.setText("");
        etUsuario.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etContraseña.setText("");
    }

    private void CreateUser(View view) {
        capData();
        listUsers(view);
        User user = new User(documento, nombres, apellidos, usuario, pass);
        UserRepository userRepository = new UserRepository(context, view);
        userRepository.insertUser(user);
        listUsers(view);
    }

    private void listUsers(View view) {
        UserRepository userRepository = new UserRepository(context, view);
        ArrayList<User> list = userRepository.getUserList();

        Log.d("MainActivity", "Users to display: " + list.size());

        ArrayAdapter<User> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);
        this.listUsers.setAdapter(adapter);

        listUsers.setOnItemClickListener((parent, view1, position, id) -> {
            selectedUser = list.get(position);
            populateUserFields(selectedUser);
        });
    }

    private void updateUser(View view) {
        if (selectedUser != null) {
            capData();
            selectedUser.setDocument(documento);
            selectedUser.setName(nombres);
            selectedUser.setLastName(apellidos);
            selectedUser.setUser(usuario);
            selectedUser.setPass(pass);

            UserRepository userRepository = new UserRepository(context, view);
            userRepository.updateUser(selectedUser);
            listUsers(view);

        } else {
            Toast.makeText(context, "No hay usuario seleccionado para actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUser(View view) {
        if (selectedUser != null) {
            UserRepository userRepository = new UserRepository(context, view);

            userRepository.deleteUser(selectedUser.getDocument());

            clearUserFields();

            listUsers(view);
        } else {
            Toast.makeText(context, "No hay usuario seleccionado para eliminar", Toast.LENGTH_SHORT).show();
        }
    }

    private void capData(){
        this.documento = Integer.parseInt(this.etDocumento.getText().toString());
        this.nombres = this.etNombres.getText().toString();
        this.apellidos = this.etApellidos.getText().toString();
        this.usuario = this.etUsuario.getText().toString();
        this.pass = this.etContraseña.getText().toString();
    }
    private void BuscarUser(View view) {
        String docStr = etDocumento.getText().toString();
        if (docStr.isEmpty()) {
            Toast.makeText(context, "Ingrese un número de documento", Toast.LENGTH_SHORT).show();
            return;
        }

        int doc = Integer.parseInt(docStr);
        UserRepository userRepository = new UserRepository(context, view);
        ArrayList<User> users = userRepository.getUserList();

        for (User user : users) {
            if (user.getDocument() == doc) {
                selectedUser = user;
                populateUserFields(user);
                Toast.makeText(context, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
    }

    private void populateUserFields(User user) {
        etDocumento.setText(String.valueOf(user.getDocument()));
        etUsuario.setText(user.getUser());
        etNombres.setText(user.getName());
        etApellidos.setText(user.getLastName());
        etContraseña.setText(user.getPass());
    }

    private void begin() {
        this.etNombres = findViewById(R.id.etNombres);
        this.etApellidos = findViewById(R.id.etApellidos);
        this.etDocumento = findViewById(R.id.etDocumento);
        this.etUsuario = findViewById(R.id.etUsuario);
        this.etContraseña = findViewById(R.id.etContraseña);
        this.listUsers = findViewById(R.id.lvLista);
        this.btnGuardar = findViewById(R.id.btnRegister);
        this.btnListUsers = findViewById(R.id.btnListar);
        this.btnActualizar = findViewById(R.id.btnActualizar);
        this.btnEliminar = findViewById(R.id.btnEliminar);
        this.btnLimpiar = findViewById(R.id.btnLimpiar);
        this.btnBuscar = findViewById(R.id.btnBuscar);
        this.context = this;
    }
}
