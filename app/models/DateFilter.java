package models;

import java.util.Date;

/**
 * Created by ftominc on 24/03/15.
 */
public class DateFilter {
    Date initial;
    Date dfinal;

    public Date getInitial() {
        return initial;
    }

    public void setInitial(Date initial) {
        this.initial = initial;
    }

    public Date getDfinal() {
        return dfinal;
    }

    public void setDfinal(Date dfinal) {
        this.dfinal = dfinal;
    }
}
