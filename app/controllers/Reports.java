package controllers;

import com.avaje.ebean.*;


import models.*;
import models.DateFilter;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.test;
import views.html.test3;


import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by ftominc on 19/03/15.
 */
public class Reports extends Controller{
    /**
     * Generate a report of the neighborhoods
     * with most Car Accidents given a initial and a
     * final dDate
     * @return Action Page with neighborhoods sorted
     * by Car Accidents
     */
    public static Result neighborhoodReportByDate(){
        DateFilter dateFilter = Form.form(DateFilter.class).bindFromRequest().get();

        String sql = "select neighborhood,count(Id) as c " +
                "from car_accident " +
                "where date between '" +
                dateFilter.getInitial() +
                "' and '" + dateFilter.getDfinal() +
                "' GROUP BY neighborhood " +
                "ORDER BY count(Id) DESC";

        String[] data = {"neighborhood","c"};


        List l = find(sql,data);
        return ok(test3.render(l));
    }
    public static Result accidentTypeReport(){
        DateFilter dateFilter = Form.form(DateFilter.class).bindFromRequest().get();
        List l;
        String sql = "select type, count(Id) as c " +
                "from car_accident " +
                "where date between '" +
                dateFilter.getInitial() +
                "' and '" + dateFilter.getDfinal() +
                "' group by type " +
                "order by count(Id) DESC";

        String[] data = {"type","c"};
        l=find(sql,data);
        System.out.println(l);
        return ok(test3.render(l));
    }
    public static Result severityReport(){
        DateFilter dateFilter = Form.form(DateFilter.class).bindFromRequest().get();
        String sql = "SELECT severity, count(car_accident_id) as c " +
                "from (SELECT * from car_accident " +
                      "INNER JOIN victim on car_accident.id = victim.car_accident_id) as k " +
                "where k.date between '" +
                dateFilter.getInitial() +"' and '"+ dateFilter.getDfinal() +
                "' GROUP BY severity";

        String[] data = {"severity","c"};
        List l = find(sql, data);
        System.out.println(l);
        return ok(test3.render(l));
    }
    public static Result streetReport(){
        DateFilter dateFilter = Form.form(DateFilter.class).bindFromRequest().get();
        String sql = "select street, count(id) as c " +
                "from car_accident " +
                "where date between '"+dateFilter.getInitial()+
                "' and '" + dateFilter.getDfinal() +
                "' GROUP BY street ORDER BY c DESC ";

        String[] data = {"street","c"};
        List l = find(sql, data);
        System.out.println(l);
        return ok(test3.render(l));
    }
    private static List find(String sql,String[] params){
        int S = 0;
        SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
        List<SqlRow> list = sqlQuery.findList();
        List<ReportData> l = new ArrayList<>();

        for(SqlRow s: list){
            l.add(new ReportData(s.getString(params[0]), s.getInteger(params[1])));
            S+=s.getInteger(params[1]);
        }
        l.add(new ReportData("Total", S));
        return l;
    }
}
