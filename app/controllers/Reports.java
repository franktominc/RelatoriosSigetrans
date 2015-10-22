package controllers;

import com.avaje.ebean.*;
import models.report.ReportData;
import models.report.ReportFilter;
import models.report.ReportVictim;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.ReportModel;
import views.html.index;
import views.html.Relatorio;


import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ftominc on 19/03/15.
 */
public class Reports extends Controller{

    public static Result processRequest(){
        ReportFilter reportFilter = Form.form(ReportFilter.class).bindFromRequest().get();
        Result result=TODO;
        reportFilter.getDfinal().setTime(reportFilter.getDfinal().getTime()+(1000*60*60*24)-1000);
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
            case "gender":
                result = genderReport(reportFilter);
                break;
            case "age":
                result = ageReport(reportFilter);
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
    private static Result neighborhoodReportByDate(ReportFilter reportFilter){

        String sql = "select neighborhood,count(Id) as c " +
                "from car_accident " +
                "where date between '" +
                reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' GROUP BY neighborhood " +
                "ORDER BY count(Id) DESC";

        String[] data = {"neighborhood","c"};
        String dateFormatted = new SimpleDateFormat("dd/MM/yyyy").format(reportFilter.getInitial()) + " - " +
               new SimpleDateFormat("dd/MM/yyyy").format(reportFilter.getDfinal());
        List l = find(sql,data);
        return ok(Relatorio.render(l,"Acidentes por bairro",dateFormatted));
    }

    private static Result accidentTypeReport(ReportFilter reportFilter){

        String sql = "select type, count(Id) as c " +
                "from car_accident " +
                "where date between '" +
                reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' group by type " +
                "order by count(Id) DESC";

        String[] data = {"type","c"};

        List l=find(sql,data);

        return ok(Relatorio.render(l, "Acidentes por Tipo",""));
    }

    private static Result severityReport(ReportFilter reportFilter){

        String sql = "SELECT severity, count(car_accident_id) as c " +
                "from (SELECT * from car_accident " +
                "INNER JOIN victim on car_accident.id = victim.car_accident_id) as k " +
                "where k.date between '" +
                reportFilter.getInitial() +"' and '"+ reportFilter.getDfinal() +
                "' GROUP BY severity";

        String[] data = {"severity","c"};

        List l = find(sql, data);

        return ok(Relatorio.render(l, "Acidentes por Severidade",""));
    }

    private static Result streetReport(ReportFilter reportFilter){

        String sql = "select street, count(id) as c " +
                "from car_accident " +
                "where date between '"+ reportFilter.getInitial()+
                "' and '" + reportFilter.getDfinal() +
                "' GROUP BY street ORDER BY c DESC ";

        String[] data = {"street","c"};

        List l = find(sql, data);

        return ok(Relatorio.render(l, "Acidentes por rua",""));
    }

    private static Result vehicleTypeReport(ReportFilter reportFilter){

        String sql = "select vehicle2.type as t, count(id) as c "+
                "from (SELECT vehicle.type, date, vehicle.id from vehicle INNER JOIN " +
                "car_accident ON car_accident.id = vehicle.car_accident_id) as vehicle2 " +
                "where date between '" + reportFilter.getInitial() +
                "' and '" + reportFilter.getDfinal() +
                "' group by t " +
                "order by c DESC";

        String[] data = {"t","c"};

        List l = find(sql, data);

        return ok(Relatorio.render(l, "Acidentes por tipo do veiculo",""));
    }

    private static Result genderReport(ReportFilter reportFilter){

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

        return ok(Relatorio.render(l, "Acidentes por Sexo",""));
    }

    private static Result ageReport(ReportFilter reportFilter){

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
        return ok(Relatorio.render(l,"Acidentes por Idade",""));
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
    public static Result ReportModel(){
        String sql = "select neighborhood,count(Id) as c " +
                "from car_accident " +


                " GROUP BY neighborhood " +
                "ORDER BY count(Id) DESC";

        String[] data = {"neighborhood","c"};

        List l = find(sql,data);

        return ok(ReportModel.render(l));
    }


}
