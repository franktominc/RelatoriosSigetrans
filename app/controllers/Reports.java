package controllers;

import com.avaje.ebean.*;


import models.ReportData;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.test;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ftominc on 19/03/15.
 */
public class Reports extends Controller{

    public static Result neighborhoodReport(){

        String sql = "select neighborhood,count(Id) as c " +
                "from car_accident " +
                "GROUP BY neighborhood " +
                "ORDER BY count(Id) DESC " +
                "limit 10";

        SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
        List<SqlRow> list = sqlQuery.findList().subList(0,10);
        List<ReportData> l = new ArrayList<ReportData>(){
            @Override
            public String toString(){
                String result = "[['Bairro', 'Quantidade'],";
                for (int i = 0; i < this.size(); i++){
                    result += this.get(i) + " ";
                    if(i< this.size() -1){
                        result += ",";
                    }
                }
                result += "]";
                return result;
            }
        };

        for(SqlRow s: list){
            l.add(new ReportData(s.getString("neighborhood"), s.getInteger("c")));
        }
        return ok(test.render(l));
    }
}
