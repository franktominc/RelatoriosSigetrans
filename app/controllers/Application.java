package controllers;

import play.db.ebean.Transactional;
import play.data.Form;
import play.mvc.*;


import views.html.*;

import static views.html.hue.*;

public class Application extends Controller {

    public static Result index() {
        return ok(Test2.render());
    }


}
