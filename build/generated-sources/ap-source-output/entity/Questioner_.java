package entity;

import entity.Person;
import entity.Response;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.0.v20110604-r9504", date="2014-09-03T11:45:17")
@StaticMetamodel(Questioner.class)
public class Questioner_ { 

    public static volatile SingularAttribute<Questioner, Response> listening;
    public static volatile SingularAttribute<Questioner, Date> questionerDate;
    public static volatile SingularAttribute<Questioner, Boolean> requirementSatisfied;
    public static volatile SingularAttribute<Questioner, Response> efficiency;
    public static volatile SingularAttribute<Questioner, String> address;
    public static volatile SingularAttribute<Questioner, Date> questionerTime;
    public static volatile SingularAttribute<Questioner, String> telephone;
    public static volatile SingularAttribute<Questioner, String> requirement;
    public static volatile SingularAttribute<Questioner, Response> courtesy;
    public static volatile SingularAttribute<Questioner, Double> TimeTakenMin;
    public static volatile SingularAttribute<Questioner, String> officerPost;
    public static volatile SingularAttribute<Questioner, Integer> noOfVisits;
    public static volatile SingularAttribute<Questioner, Response> general;
    public static volatile SingularAttribute<Questioner, Response> response;
    public static volatile SingularAttribute<Questioner, Person> person;
    public static volatile SingularAttribute<Questioner, String> officerName;
    public static volatile SingularAttribute<Questioner, String> name;
    public static volatile SingularAttribute<Questioner, String> suggestions;
    public static volatile SingularAttribute<Questioner, Long> id;
    public static volatile SingularAttribute<Questioner, Double> TimeTakenHrs;
    public static volatile SingularAttribute<Questioner, Response> reception;
    public static volatile SingularAttribute<Questioner, Response> reply;
    public static volatile SingularAttribute<Questioner, Response> facilities;
    public static volatile SingularAttribute<Questioner, Boolean> previousVisits;

}