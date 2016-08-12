package go.pokemon.com.pokemonivchecker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Inventories;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.util.PokeNames;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;

public class AuthActivity extends AppCompatActivity {

    private TextView textView;
    private EditText tokenText;
    private Button button;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        textView = (TextView) findViewById(R.id.textView);
        tokenText = (EditText) findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);

        final PokemonAdapter adapter = new PokemonAdapter(this,R.layout.pokemon, new ArrayList<String>(), new LinkedHashMap<String, List<Pokemon>>());
        listView.setAdapter(adapter);

        tokenText.setText("4/YV0o_kge3_tnuha6Zm8XWUM3p7KSzqHrXOs3OJe3IUI");

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String token = tokenText.getText().toString();
                Log.e("token", token);

                new PokemonFeedTask(adapter).execute(token);

            }
        });
    }


}
