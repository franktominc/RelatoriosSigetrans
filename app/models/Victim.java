package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by ftominc on 23/02/15.
 */

@Entity
public class Victim extends Model {

    @Id
    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String hospital;
    private String severity;
    private Long cod_vit;

    public Long getCod_vit() {
        return cod_vit;
    }

    public void setCod_vit(Long cod_vit) {
        this.cod_vit = cod_vit;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }
}
