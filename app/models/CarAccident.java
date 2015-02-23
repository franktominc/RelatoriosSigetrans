package models;





import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

/**
 * Created by ftominc on 12/02/15.
 */
@Entity
public class CarAccident {
    @Id
    private Long id;
    private String neighborhood;
    private String street;
    private String crossroad;
    private Date date;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Victim> victims;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;
}
