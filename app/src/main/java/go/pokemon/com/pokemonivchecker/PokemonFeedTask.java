package go.pokemon.com.pokemonivchecker;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by linleo on 8/12/2016.
 */

class PokemonFeedTask extends AsyncTask<String, Void, PokemonGo> {

    private Exception exception;
    private PokemonAdapter adapter;

    private Locale localeTW = new Locale("zh", "TW");

    public PokemonFeedTask(PokemonAdapter pokemonAdapter){
        this.adapter = pokemonAdapter;
    }

    protected PokemonGo doInBackground(String[] token) {

        PokemonGo go = null;
        try {
            OkHttpClient httpClient = new OkHttpClient();
            try{
                GoogleUserCredentialProvider provider = new GoogleUserCredentialProvider(httpClient);
                provider.login(token[0]);
                String refreshToken = provider.getRefreshToken();
                go = new PokemonGo(provider, httpClient);

            }catch (Exception e){
                Log.e("error", e.toString());
            }
        } catch (Exception e) {
            this.exception = e;
        }

        return go;
    }

    protected void onPostExecute(PokemonGo go) {
        // TODO: check this.exception
        // TODO: do something with the feed

        try {
            Map<String, List<Pokemon>> pokemon = listIvs(go,localeTW);
            adapter.refreshPokemons(pokemon);
        }catch (Exception e){
            Log.e("error", e.toString());
        }
    }

    private Map<String, List<Pokemon>> listIvs(PokemonGo go, Locale localeTW) throws Exception{
        Inventories inventories = go.getInventories();

        List<Pokemon> pokemonList = inventories.getPokebank().getPokemons();

        Map<Integer, List<Pokemon>> pokemonMap = new HashMap<>();
        Map<String, List<Pokemon>> pokemonName = new LinkedHashMap<>();

        if(CollectionUtils.isNotEmpty(pokemonList)){
            for(Pokemon pokemon: pokemonList){

                int id = pokemon.getPokemonId().getNumber();

                if(pokemonMap.containsKey(id)){
                    pokemonMap.get(id).add(pokemon);
                }else{
                    List<Pokemon> pokemonLists = new ArrayList<Pokemon>();
                    pokemonLists.add(pokemon);
                    pokemonMap.put(id, pokemonLists);
                }
            }
        }

        PokemonComparator pokemonComparator = new PokemonComparator();

        Object[] idList = pokemonMap.keySet().toArray();

        Arrays.sort(idList);

        for(Object idObj: idList){

            Integer id = (Integer)idObj;
            String output = PokeNames.getDisplayName(id,localeTW );
            if(id == 29){
                output = "nidoranf";
            }else if(id == 32){
                output = "nidoranm";
            }

            List<Pokemon> pokemonSort = pokemonMap.get(id);
            Collections.sort(pokemonSort, pokemonComparator);
            pokemonName.put(output, pokemonSort);
        }

        return pokemonName;
    }

    private class PokemonComparator implements Comparator<Pokemon> {

        public int compare(Pokemon p1, Pokemon p2) {

            int result = 0;

            result = (int)(p2.getIvRatio()*100 - p1.getIvRatio()*100);
            if(result == 0){
                result = p2.getCp() - p1.getCp();
            }

            return result;

        }

    }
}