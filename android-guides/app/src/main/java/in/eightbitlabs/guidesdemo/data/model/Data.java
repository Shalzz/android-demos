package in.eightbitlabs.guidesdemo.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class Data implements DataModel, Parcelable {

    public static final Factory<Data> FACTORY = new Factory<>(AutoValue_Data::new);

    public static final RowMapper<Data> SELECT_ALL_MAPPER = FACTORY.select_allMapper();
    public static final RowMapper<Data> SELECT_BY_CART_MAPPER = FACTORY.select_by_cartMapper();

    public static Data create(Data data, boolean in_cart) {
        return new AutoValue_Data(
                data._id(),
                data.name(),
                data.startDate(),
                data.endDate(),
                data.objType(),
                data.url(),
                data.icon(),
                data.loginRequired(),
                in_cart ? 1 : 0);
    }

    public static TypeAdapter<Data> typeAdapter(Gson gson) {
        return new AutoValue_Data.GsonTypeAdapter(gson);
    }
}

