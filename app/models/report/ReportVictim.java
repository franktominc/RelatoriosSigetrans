package models.report;

import java.util.Date;

/**
 * Created by ftominc on 08/04/15.
 */
public class ReportVictim {

    private String name;
    private Integer age;
    private String hospital;
    private Date accidentDate;

    public ReportVictim(String name, Integer age, String hospital, Date accidentDate) {
        this.name = name;
        this.age = age;
        this.hospital = hospital;
        this.accidentDate = accidentDate;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getHospital() {
        return hospital;
    }

    public Date getAccidentDate() {
        return accidentDate;
    }
}
