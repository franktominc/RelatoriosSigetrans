package controllers;

import com.avaje.ebean.*;


import models.*;
import models.ReportFilter;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.index;
import views.html.test3;


import java.util.*;

/**
 * Created by ftominc on 19/03/15.
 */
public class Reports extends Controller{

    public static Result processRequest(){
        ReportFilter reportFilter = Form.form(ReportFilter.class).bindFromRequest().get();
        Result result=TODO;
        System.out.println(reportFilter.getReportType());
        switch (reportFilter.getReportType()){
            case "neighborhood":
                result = neighborhoodReportByDate(reportFilter);
                break;
            case "accidentType":
                result = accidentTypeReport(reportFilter);
                break;
            case "severity":
                result = severityReport(reportFilter);
                break;
            case "vehicleType":
                result = vehicleTypeReport(reportFilter);
                break;
            case "street":
                result = streetReport(reportFilter);
                break;
            case "genderReport":
                result = genderReport(reportFilter);
                break;
        }
        System.out.println("Renderizou");
        return result;
    }

    /**
     * Generate a report of the neighborhoods
     * with most Car Accidents given a initial and a
     * final dDate
     * @return Action Page with neighborhoods sorted
     * by Car Accidents
     */
    public static Result neighborhoodReportByDate(ReportFilter reportFilter){

        String sql = "select neighborhood,count(Id) as c " +
                "from car_accident " +
                "where date between '" +
                reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' GROUP BY neighborhood " +
                "ORDER BY count(Id) DESC";

        String[] data = {"neighborhood","c"};

        List l = find(sql,data);
        return ok(test3.render(l));
    }

    public static Result accidentTypeReport(ReportFilter reportFilter){

        String sql = "select type, count(Id) as c " +
                "from car_accident " +
                "where date between '" +
                reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' group by type " +
                "order by count(Id) DESC";

        String[] data = {"type","c"};

        List l=find(sql,data);

        return ok(test3.render(l));
    }

    public static Result severityReport(ReportFilter reportFilter){

        String sql = "SELECT severity, count(car_accident_id) as c " +
                "from (SELECT * from car_accident " +
                      "INNER JOIN victim on car_accident.id = victim.car_accident_id) as k " +
                "where k.date between '" +
                reportFilter.getInitial() +"' and '"+ reportFilter.getDfinal() +
                "' GROUP BY severity";

        String[] data = {"severity","c"};

        List l = find(sql, data);

        return ok(test3.render(l));
    }

    public static Result streetReport(ReportFilter reportFilter){

        String sql = "select street, count(id) as c " +
                "from car_accident " +
                "where date between '"+ reportFilter.getInitial()+
                "' and '" + reportFilter.getDfinal() +
                "' GROUP BY street ORDER BY c DESC ";

        String[] data = {"street","c"};

        List l = find(sql, data);

        return ok(test3.render(l));
    }

    public static Result vehicleTypeReport(ReportFilter reportFilter){

        String sql = "select vehicle2.type as t, count(id) as c "+
                "from (SELECT vehicle.type, date, vehicle.id from vehicle INNER JOIN " +
                       "car_accident ON car_accident.id = vehicle.car_accident_id) as vehicle2 " +
                "where date between '" + reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' group by t " +
                "order by c DESC";

        String[] data = {"t","c"};

        List l = find(sql, data);

        return ok(test3.render(l));
    }

    private static Result genderReport(ReportFilter reportFilter){

        String sql = "select count(id) as c, gender " +
                "from (SELECT victim.id, gender, date " +
                "      from victim INNER JOIN car_accident ON " +
                "     car_accident.id = victim.car_accident_id)" +
                " as victim2 " +
                "where date between '" +reportFilter.getInitial()+
                "' and '"+ reportFilter.getDfinal()+
                "' group by gender";

        String[] data = {"gender","c"};

        List l = find(sql, data);

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
