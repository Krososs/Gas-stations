package pl.sk.station;

import lombok.Data;
import lombok.NonNull;
import org.json.JSONObject;
import javax.persistence.*;

@Entity
@Data
@Table(name="stations")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;

    private String address;
    @NonNull
    private Double lon;
    @NonNull
    private Double lat;
    @NonNull
    private Double pb95;
    @NonNull
    private Double pb98;
    @NonNull
    private Double diesel;

    @NonNull
    private Double gas;

    public JSONObject toJson(){
        JSONObject data = new JSONObject();
        data.put("id", this.id);
        data.put("name", this.name);
        data.put("lon", this.lon);
        data.put("lat", this.lat);
        return data;
    }

    public Station() {
    }
}
