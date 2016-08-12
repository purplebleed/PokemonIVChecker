package go.pokemon.com.pokemonivchecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pokegoapi.api.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by linleo on 8/12/2016.
 */
public class PokemonAdapter extends ArrayAdapter<String> {
    private final Context context;
    private Map<String, List<Pokemon>> pokemons;
    private List<String> names;

    public PokemonAdapter(Context context, int resourc, List<String> name, Map<String, List<Pokemon>> values) {
        super(context, -1, name);
        this.context = context;
        this.pokemons = values;
        this.names = name;
    }

    public void refreshPokemons( Map<String, List<Pokemon>> pokemons) {
        this.pokemons = pokemons;
        names.clear();
        names.addAll(pokemons.keySet());
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pokemon, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.pokemonName);
        TextView ivs = (TextView) rowView.findViewById(R.id.ivs);

        String name = names.get(position);

        nameView.setText(name);

        String iv = "";
        for(Pokemon pokemon: pokemons.get(name)){
            iv += " CP: "+pokemon.getCp() + " IV: " + pokemon.getIvRatio();
        }
        ivs.setText(iv);
        return rowView;
    }
}
