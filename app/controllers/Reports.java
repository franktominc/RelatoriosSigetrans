package controllers;

import com.avaje.ebean.*;


import models.report.ReportData;
import models.report.ReportFilter;
import models.report.ReportVictim;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import views.html.Test2;
import views.html.index;
import views.html.report;
import views.html.test3;

import it.innove.play.pdf.PdfGenerator;


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
                result = ok(neighborhoodReportByDate(reportFilter));
                break;
            case "accidentType":
                result = ok(accidentTypeReport(reportFilter));
                break;
            case "severity":
                result = ok(severityReport(reportFilter));
                break;
            case "vehicleType":
                result = ok(vehicleTypeReport(reportFilter));
                break;
            case "street":
                result = ok(streetReport(reportFilter));
                break;
            case "gender":
                result = ok(genderReport(reportFilter));
                break;
            case "age":
                result = ok(ageReport(reportFilter));
                break;
            case "deceased":
                result = victimStateReport(reportFilter,"'Óbito'");
                break;
            case "badHurted":
                result = victimStateReport(reportFilter,
                        "'Ferimentos considerados graves com risco à vida'");
                break;
        }
        return result;
    }

    /**
     * Generate a report of the neighborhoods
     * with most Car Accidents given a initial and a
     * final dDate
     * @return Action Page with neighborhoods sorted
     * by Car Accidents
     */
    private static Html neighborhoodReportByDate(ReportFilter reportFilter){

        String sql = "select neighborhood,count(Id) as c " +
                "from car_accident " +
                "where date between '" +
                reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' GROUP BY neighborhood " +
                "ORDER BY count(Id) DESC";

        String[] data = {"neighborhood","c"};

        List l = find(sql,data);
        return test3.render(l, reportFilter);
    }

    private static Html accidentTypeReport(ReportFilter reportFilter){

        String sql = "select type, count(Id) as c " +
                "from car_accident " +
                "where date between '" +
                reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' group by type " +
                "order by count(Id) DESC";

        String[] data = {"type","c"};

        List l=find(sql,data);

        return (test3.render(l,reportFilter));
    }

    private static Html severityReport(ReportFilter reportFilter){

        String sql = "SELECT severity, count(car_accident_id) as c " +
                "from (SELECT * from car_accident " +
                      "INNER JOIN victim on car_accident.id = victim.car_accident_id) as k " +
                "where k.date between '" +
                reportFilter.getInitial() +"' and '"+ reportFilter.getDfinal() +
                "' GROUP BY severity";

        String[] data = {"severity","c"};

        List l = find(sql, data);

        return (test3.render(l,reportFilter));
    }

    private static Html streetReport(ReportFilter reportFilter){

        String sql = "select street, count(id) as c " +
                "from car_accident " +
                "where date between '"+ reportFilter.getInitial()+
                "' and '" + reportFilter.getDfinal() +
                "' GROUP BY street ORDER BY c DESC ";

        String[] data = {"street","c"};

        List l = find(sql, data);

        return (test3.render(l,reportFilter));
    }

    private static Html vehicleTypeReport(ReportFilter reportFilter){

        String sql = "select vehicle2.type as t, count(id) as c "+
                "from (SELECT vehicle.type, date, vehicle.id from vehicle INNER JOIN " +
                       "car_accident ON car_accident.id = vehicle.car_accident_id) as vehicle2 " +
                "where date between '" + reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' group by t " +
                "order by c DESC";

        String[] data = {"t","c"};

        List l = find(sql, data);

        return (test3.render(l,reportFilter));
    }

    private static Html genderReport(ReportFilter reportFilter){

        String sql = "select count(id) as c, gender " +
                "from (SELECT victim.id, gender, date " +
                "      from victim INNER JOIN car_accident ON " +
                "     car_accident.id = victim.car_accident_id)" +
                " as victim2 " +
                "where date between '" +reportFilter.getInitial()+
                "' and '"+ reportFilter.getDfinal()+
                "' group by gender " +
                "order by c DESC";

        String[] data = {"gender","c"};

        List l = find(sql, data);

        return (test3.render(l,reportFilter));
    }

    private static Html ageReport(ReportFilter reportFilter){

        List<ReportData> l = new ArrayList<ReportData>();

        for (int i = 0; i <= 85; i+=5){

            String sql = "select count(id) as c from " +
                    "(select age, victim.id ,date from victim " +
                    "INNER JOIN car_accident ON car_accident.id = victim.car_accident_id) as victims " +
                    "where age between " + i + " and " + (i+5) +
                    " and date between '" + reportFilter.getInitial() + "' and '" +
                    reportFilter.getDfinal()+"'";
            SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
            List<SqlRow> list = sqlQuery.findList();
            if(!list.isEmpty())
                l.add(new ReportData(i + "-" + (i+5), list.get(0).getInteger("c")));
        }
        return (test3.render(l,reportFilter));
    }

    private static Result victimStateReport(ReportFilter reportFilter, String state){

        String sql = "select victim.name as name, age, hospital, date " +
                "from victim " +
                "INNER JOIN car_accident ON car_accident.id = victim.car_accident_id " +
                "where victim.severity like " + state + " and date between '" +
                reportFilter.getInitial() +"' and '" + reportFilter.getDfinal()+
                "' ORDER BY date desc";

        SqlQuery sqlQuery = Ebean.createSqlQuery(sql);

        List<SqlRow> list = sqlQuery.findList();

        List<ReportVictim> l = new ArrayList<>();

        for (SqlRow s : list){
            l.add(new ReportVictim(s.getString("name"),
                                    s.getInteger("age"),
                                    s.getString("hospital"),
                                    s.getDate("date")));
        }

        System.out.println(l);
        return ok(index.render(""));
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

    public static Result PDF(){

        ReportFilter reportFilter = Form.form(ReportFilter.class).bindFromRequest().get();
        Html result = Test2.render();
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
                case "gender":
                    result = genderReport(reportFilter);
                    break;
                case "age":
                    result = ageReport(reportFilter);
                    break;
            }

        return PdfGenerator.ok(result , "http://localhost:9000");
    }
}
