package in.eightbitlabs.todo.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Tasks {

    @SerializedName("data")
    private List<Data> data = new ArrayList<Data>();

    /**
     *
     * @return
     * The data
     */
    public List<Data> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Data> data) {
        this.data = data;
    }

}