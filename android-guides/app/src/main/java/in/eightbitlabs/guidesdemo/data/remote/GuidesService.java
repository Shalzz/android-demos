package in.eightbitlabs.guidesdemo.data.remote;

import in.eightbitlabs.guidesdemo.data.model.Guides;
import retrofit2.http.GET;
import rx.Observable;

public interface GuidesService {

    String ENDPOINT = "https://guidebook.com/service/v2/";

    @GET("upcomingGuides")
    Observable<Guides> getUpcomingGuides();

}
