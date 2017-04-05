package in.eightbitlabs.todo.data.remote;

import in.eightbitlabs.todo.data.model.Tasks;
import retrofit2.http.GET;
import rx.Observable;

public interface TodoService {

    String ENDPOINT = "https://dl.dropboxusercontent.com/u/6890301/";

    @GET("tasks.json")
    Observable<Tasks> getTasks();
}
