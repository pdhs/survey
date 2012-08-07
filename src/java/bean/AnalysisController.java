/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import entity.Person;
import entity.Questioner;
import entity.Response;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author buddhika
 */
@ManagedBean
@RequestScoped
public class AnalysisController {

    @EJB
    QuestionerFacade queFacade;
    @EJB
    ResponseFacade resFacade;
    @EJB
    PersonFacade perFacade;
    //
    String personName;
    //
    Person person;
    //
    DataModel<Person> persons;
    //
    PieChartModel pcmFullfilled;
    PieChartModel pcmPreviousVisits;
    CartesianChartModel ccmDuration;
    CartesianChartModel ccmReturnCount;
    CartesianChartModel ccmResponse;

    /**
     * Creates a new instance of AnalysisController
     */
    public AnalysisController() {
    }

    public PersonFacade getPerFacade() {
        return perFacade;
    }

    public DataModel<Person> getPersons() {
        return new ListDataModel<Person>(getPerFacade().findAll());
    }

    public void setPersons(DataModel<Person> persons) {
        this.persons = persons;
    }

    public void setPerFacade(PersonFacade perFacade) {
        this.perFacade = perFacade;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void addPerson() {
        Person temPerson = new Person();
        temPerson.setName(personName);
        getPerFacade().create(temPerson);
        personName = "";
        person = temPerson;
    }

    public void addAllPersons() {
        System.out.print("1");
        List<Questioner> questioners = getQueFacade().findAll();
        System.out.print("2");
        for (Questioner q : questioners) {
            Person temPer = null;
            System.out.print("3");
            if (q.getOfficerName() != null) {
                System.out.print("4");
                List<Person> temPersons = getPerFacade().findBySQL("SELECT p From Person p WHERE p.name = '" + q.getOfficerName() + "'");
                System.out.print("5");
                if (temPersons.isEmpty() == true) {
                    System.out.print("6");

                    System.out.print("7");
                    temPer = new Person();
                    temPer.setName(q.getOfficerName());
                    getPerFacade().create(temPer);

                    q.setPerson(temPer);
                } else {
                    temPer = temPersons.get(0);
                    q.setPerson(temPer);
                }
            } else {
                q.setPerson(null);
            }
            getQueFacade().edit(q);
        }
    }

    public PieChartModel getPcmPreviousVisits() {
        pcmPreviousVisits = new PieChartModel();
        long yesCount = 0;
        long noCount = 0;
        List<Questioner> questioners = getQueFacade().findAll();
        for (Questioner q : questioners) {
            if (q.getPreviousVisits()) {
                yesCount++;
            } else {
                noCount++;
            }
        }
        pcmPreviousVisits.set("Yes", yesCount);
        pcmPreviousVisits.set("No", noCount);
        return pcmPreviousVisits;
    }

    public CartesianChartModel getCcmResponse() {
        ccmResponse = new CartesianChartModel();
        ChartSeries reception = new ChartSeries("Reception");
        ChartSeries courtesy = new ChartSeries("Courtesy");
        ChartSeries listening = new ChartSeries("Listening");
        ChartSeries reply = new ChartSeries("Reply");
        ChartSeries response = new ChartSeries("Response");
        ChartSeries efficacy = new ChartSeries("Efficacy");
        ChartSeries facilities = new ChartSeries("Facilities");
        ChartSeries overall = new ChartSeries("Overall");



        List<Questioner> questioners = new ArrayList<Questioner>();
        questioners = getQueFacade().findAll();

        long a1 = 0;
        long a2 = 0;
        long a3 = 0;
        long a4 = 0;
        long c1 = 0;
        long c2 = 0;
        long c3 = 0;
        long c4 = 0;
        long b1 = 0;
        long b2 = 0;
        long b3 = 0;
        long b4 = 0;
        long d1 = 0;
        long d2 = 0;
        long d3 = 0;
        long d4 = 0;
        long e1 = 0;
        long e2 = 0;
        long e3 = 0;
        long e4 = 0;
        long g1 = 0;
        long g2 = 0;
        long g3 = 0;
        long g4 = 0;
        long f1 = 0;
        long f2 = 0;
        long f3 = 0;
        long f4 = 0;
        long h1 = 0;
        long h2 = 0;
        long h3 = 0;
        long h4 = 0;

        Response excellent = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'විශිශ්ඨයි'").get(0);
        Response veryGood = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'ඉතා හොදයි'").get(0);
        Response good = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'හොදයි'").get(0);
        Response poor = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'දුර්වලයි'").get(0);

        for (Questioner q : questioners) {

            if (q.getReception() == null) {
            } else if (q.getReception().equals(excellent)) {
                a1++;
            } else if (q.getReception().equals(veryGood)) {
                a2++;
            } else if (q.getReception().equals(good)) {
                a3++;
            } else if (q.getReception().equals(poor)) {
                a4++;
            }

            if (q.getCourtesy() == null) {
            } else if (q.getCourtesy().equals(excellent)) {
                b1++;
            } else if (q.getCourtesy().equals(veryGood)) {
                b2++;
            } else if (q.getCourtesy().equals(good)) {
                b3++;
            } else if (q.getCourtesy().equals(poor)) {
                b4++;
            }

            if (q.getListening() == null) {
            } else if (q.getListening().equals(excellent)) {
                c1++;
            } else if (q.getListening().equals(veryGood)) {
                c2++;
            } else if (q.getListening().equals(good)) {
                c3++;
            } else if (q.getListening().equals(poor)) {
                c4++;
            }

            if (q.getReply() == null) {
            } else if (q.getReply().equals(excellent)) {
                d1++;
            } else if (q.getReply().equals(veryGood)) {
                d2++;
            } else if (q.getReply().equals(good)) {
                d3++;
            } else if (q.getReply().equals(poor)) {
                d4++;
            }

            if (q.getResponse() == null) {
            } else if (q.getResponse().equals(excellent)) {
                e1++;
            } else if (q.getResponse().equals(veryGood)) {
                e2++;
            } else if (q.getResponse().equals(good)) {
                e3++;
            } else if (q.getResponse().equals(poor)) {
                e4++;
            }

            if (q.getEfficiency() == null) {
            } else if (q.getEfficiency().equals(excellent)) {
                f1++;
            } else if (q.getEfficiency().equals(veryGood)) {
                f2++;
            } else if (q.getEfficiency().equals(good)) {
                f3++;
            } else if (q.getEfficiency().equals(poor)) {
                f4++;
            }

            if (q.getFacilities() == null) {
            } else if (q.getFacilities().equals(excellent)) {
                g1++;
            } else if (q.getFacilities().equals(veryGood)) {
                g2++;
            } else if (q.getFacilities().equals(good)) {
                g3++;
            } else if (q.getFacilities().equals(poor)) {
                g4++;
            }

            if (q.getGeneral() == null) {
            } else if (q.getGeneral().equals(excellent)) {
                h1++;
            } else if (q.getGeneral().equals(veryGood)) {
                h2++;
            } else if (q.getGeneral().equals(good)) {
                h3++;
            } else if (q.getGeneral().equals(poor)) {
                h4++;
            }



        }





        reception.set("Excellent", a1);
        reception.set("Very Good", a2);
        reception.set("Good", a3);
        reception.set("Poor", a4);
        courtesy.set("Excellent", b1);
        courtesy.set("Very Good", b2);
        courtesy.set("Good", b3);
        courtesy.set("Poor", b4);
        listening.set("Excellent", c1);
        listening.set("Very Good", c2);
        listening.set("Good", c3);
        listening.set("Poor", c4);
        reply.set("Excellent", d1);
        reply.set("Very Good", d2);
        reply.set("Good", d3);
        reply.set("Poor", d4);
        response.set("Excellent", e1);
        response.set("Very Good", e2);
        response.set("Good", e3);
        response.set("Poor", e4);
        efficacy.set("Excellent", f1);
        efficacy.set("Very Good", f2);
        efficacy.set("Good", f3);
        efficacy.set("Poor", f4);
        facilities.set("Excellent", g1);
        facilities.set("Very Good", g2);
        facilities.set("Good", g3);
        facilities.set("Poor", g4);
        overall.set("Excellent", h1);
        overall.set("Very Good", h2);
        overall.set("Good", h3);
        overall.set("Poor", h4);

        ccmResponse.addSeries(reception);
        ccmResponse.addSeries(courtesy);
        ccmResponse.addSeries(listening);
        ccmResponse.addSeries(reply);
        ccmResponse.addSeries(response);
        ccmResponse.addSeries(efficacy);
        ccmResponse.addSeries(facilities);
        ccmResponse.addSeries(overall);
        return ccmResponse;
    }

    public CartesianChartModel getCcmReturnCount() {
        ccmReturnCount = new CartesianChartModel();
        ChartSeries visitCount = new ChartSeries();
        visitCount.setLabel("No. of Visits");

        double visits = 0l;

        List<Questioner> questioners = new ArrayList<Questioner>();
        questioners = getQueFacade().findAll();

        long no = 0;
        long a1 = 0;
        long a2 = 0;
        long a3 = 0;
        long a4 = 0;
        long a5 = 0;
        long m5 = 0;

        for (Questioner q : questioners) {
            if (q.getNoOfVisits() != null) {
                visits = q.getNoOfVisits();
            } else {
                visits = 0.0;
            }
            if (q.getPreviousVisits() == false) {
                no++;
            } else if (visits == 0) {
                no++;
            } else if (visits == 1) {
                a1++;
            } else if (visits == 2) {
                a2++;
            } else if (visits == 3) {
                a3++;
            } else if (visits == 4) {
                a4++;
            } else if (visits == 5) {
                a5++;
            } else {
                m5++;
            }
        }

        visitCount.set("No Visits", no);
        visitCount.set("One visit", a1);
        visitCount.set("Two visits", a2);
        visitCount.set("Three Visits", a3);
        visitCount.set("Four Visits", a4);
        visitCount.set("Five Visits", a5);
        visitCount.set("More than 5 visits", m5);


        ccmReturnCount.addSeries(visitCount);



        return ccmReturnCount;
    }

    public CartesianChartModel getCcmDuration() {
        ccmDuration = new CartesianChartModel();
        ChartSeries duration = new ChartSeries();
        duration.setLabel("Duraton");

        double minutes = 0l;

        List<Questioner> questioners = new ArrayList<Questioner>();
        questioners = getQueFacade().findAll();

        long a15 = 0;
        long a30 = 0;
        long a45 = 0;
        long a60 = 0;
        long a90 = 0;
        long a120 = 0;
        long a180 = 0;
        long a240 = 0;
        long a300 = 0;
        long a = 0;

        for (Questioner q : questioners) {

            if (q.getTimeTakenMin() != null && q.getTimeTakenHrs() != null) {
                minutes = q.getTimeTakenMin() + q.getTimeTakenHrs() * 60;
            } else if (q.getTimeTakenMin() != null) {
                minutes = q.getTimeTakenMin();
            } else if (q.getTimeTakenHrs() != null) {
                minutes = q.getTimeTakenHrs() * 60;
            } else {
                minutes = 0;
            }



            if (minutes <= 0) {
            } else if (minutes <= 15) {
                a15++;
            } else if (minutes <= 30) {
                a30++;
            } else if (minutes <= 45) {
                a45++;
            } else if (minutes <= 60) {
                a60++;
            } else if (minutes <= 90) {
                a90++;
            } else if (minutes <= 120) {
                a120++;
            } else if (minutes <= 180) {
                a180++;
            } else if (minutes <= 240) {
                a240++;
            } else if (minutes <= 300) {
                a300++;
            } else {
                a++;
            }
        }

        duration.set("Less then 15 min", a15);
        duration.set("Less then 30 min", a30);
        duration.set("Less then 45 min", a45);
        duration.set("Less then one hour", a60);
        duration.set("Less then 2 hours", a120);
        duration.set("Less then 3 hours", a180);
        duration.set("Less then 4 hours", a240);
        duration.set("Less then 5 hours", a300);
        duration.set("More then 5 hours", a);


        ccmDuration.addSeries(duration);

        return ccmDuration;
    }

    public PieChartModel getPcmFullfilled() {
        pcmFullfilled = new PieChartModel();
        long yesCount = 0;
        long noCount = 0;

        List<Questioner> questioners = new ArrayList<Questioner>();
        questioners = getQueFacade().findAll();
        for (Questioner q : questioners) {
            if (q.getRequirementSatisfied()) {
                yesCount++;
            } else {
                noCount++;
            }
        }
        pcmFullfilled.set("Yes", yesCount);
        pcmFullfilled.set("No", noCount);
        return pcmFullfilled;
    }

    public QuestionerFacade getQueFacade() {
        return queFacade;
    }

    public void setQueFacade(QuestionerFacade queFacade) {
        this.queFacade = queFacade;
    }

    public ResponseFacade getResFacade() {
        return resFacade;
    }

    public void setResFacade(ResponseFacade resFacade) {
        this.resFacade = resFacade;
    }
}
