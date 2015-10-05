package controllers;

import play.db.ebean.Transactional;
import play.data.Form;
import play.mvc.*;


import views.html.*;

import static views.html.ReportModel.*;

public class Application extends Controller {

    public static Result index() {
        return ok(GerarRelatorio.render());
    }


}
