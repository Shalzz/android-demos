package in.eightbitlabs.todo.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.RowMapper;

/**
 * @author shalzz
 */

@AutoValue
public abstract class Data implements DataModel, Parcelable {

    public static final Factory<Data> FACTORY = new Factory<>(AutoValue_Data::new);

    public static final RowMapper<Data> MAPPER = FACTORY.select_by_stateMapper();

    public static Data create(long id, String name, int state) {
        return new AutoValue_Data(id, name, state);
    }

    public static TypeAdapter<Data> typeAdapter(Gson gson) {
        return new AutoValue_Data.GsonTypeAdapter(gson);
    }
}
