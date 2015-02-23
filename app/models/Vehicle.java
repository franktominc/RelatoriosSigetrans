package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by ftominc on 23/02/15.
 */
@Entity
public class Vehicle extends Model{
    @Id
    private Long id;
    private String type;
}
