package bakingrecipes;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import bakingrecipes.Data.Example;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Alfa on 5/10/2018.
 */

interface BakingInterface {
    @NonNull
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Observable<ArrayList<Example>> getBaking();

}
