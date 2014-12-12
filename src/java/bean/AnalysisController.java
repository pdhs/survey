/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import Data.ResponseFor;
import Data.YearCount;
import entity.Person;
import entity.Questioner;
import entity.Response;
import faces.util.JsfUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.ReadableInstant;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author buddhika
 */
@ManagedBean
@SessionScoped
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
    CartesianChartModel ccmPersonFullfill;
    CartesianChartModel pcmYearlyPreviousVisits;
    CartesianChartModel ccmYearlyFillfilled;

    LineChartModel ccmReocrds;

    BarChartModel barChartModel;

    String designation;

    Date fromDate;
    Date toDate;

    public Date getFromDate() {
        if (fromDate == null) {
            Calendar fd = Calendar.getInstance();
            fd.set(Calendar.MONTH, 0);
            fd.set(Calendar.DATE, 1);
            fromDate = fd.getTime();
        }
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        if (toDate == null) {
            toDate = new Date();
        }
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public CartesianChartModel getCcmReocrds() {
        if (ccmReocrds == null) {
            ccmReocrds = new LineChartModel();
        }
        return ccmReocrds;
    }

    public void setCcmReocrds(LineChartModel ccmReocrds) {
        this.ccmReocrds = ccmReocrds;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    private void addToYearCount(List<YearCount> lst, int year, int count, int pos, int neg) {
        boolean found = false;
        for (YearCount yc : lst) {
            if (yc.getYear() == year) {
                System.out.println("Year " + year + " already there.");
                System.out.println("Pre count " + yc.getCount());
                yc.setCount(yc.getCount() + count);
                yc.setNeg(yc.getNeg() + neg);
                yc.setPos(yc.getPos() + pos);
                System.out.println("Post count " + yc.getCount());
                found = true;
            }
        }
        if (found == false) {
            System.out.println("New Year create  " + year);
            YearCount yc = new YearCount();
            yc.setYear(year);
            yc.setCount(count);
            yc.setNeg(neg);
            yc.setPos(pos);
            lst.add(yc);
        }
    }

    public CartesianChartModel getPcmYearlyPreviousVisits() {
        List<Questioner> questioners;
        questioners = getQueFacade().findBySQL("select q from Questioner q order by q.questionerDate");
        List<YearCount> lst = new ArrayList<YearCount>();
        for (Questioner q : questioners) {
            if (q.getQuestionerDate() != null) {
                if (q.getPreviousVisits()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(q.getQuestionerDate());
                    addToYearCount(lst, c.get(Calendar.YEAR), 1, 1, 0);
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(q.getQuestionerDate());
                    addToYearCount(lst, c.get(Calendar.YEAR), 1, 0, 1);
                }
            }
        }
        pcmYearlyPreviousVisits = new CartesianChartModel();
        ChartSeries csp = new ChartSeries("Yes");
        ChartSeries csn = new ChartSeries("No");
        for (YearCount yc : lst) {
            Integer pp = yc.getPos() * 100 / yc.getCount();
            Integer np = yc.getNeg() * 100 / yc.getCount();
            System.out.println("count is " + yc.getCount());
            System.out.println("p is " + yc.getPos());
            System.out.println("n is " + yc.getNeg());
            System.out.println("pp is " + pp);
            System.out.println("np is " + np);
            csp.set(yc.getYear() + "", pp);
            csn.set(yc.getYear() + "", np);
        }
        pcmYearlyPreviousVisits.addSeries(csp);
        pcmYearlyPreviousVisits.addSeries(csn);
        return pcmYearlyPreviousVisits;
    }

    public void setPcmYearlyPreviousVisits(CartesianChartModel pcmYearlyPreviousVisits) {
        this.pcmYearlyPreviousVisits = pcmYearlyPreviousVisits;
    }

    public CartesianChartModel getCcmYearlyFillfilled() {
        List<Questioner> questioners;
        questioners = getQueFacade().findBySQL("select q from Questioner q order by q.questionerDate");
        List<YearCount> lst = new ArrayList<YearCount>();
        for (Questioner q : questioners) {
            if (q.getQuestionerDate() != null) {
                if (q.getRequirementSatisfied()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(q.getQuestionerDate());
                    addToYearCount(lst, c.get(Calendar.YEAR), 1, 1, 0);
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(q.getQuestionerDate());
                    addToYearCount(lst, c.get(Calendar.YEAR), 1, 0, 1);
                }
            }
        }
        ccmYearlyFillfilled = new CartesianChartModel();
        ChartSeries csp = new ChartSeries("Yes");
        ChartSeries csn = new ChartSeries("No");
        for (YearCount yc : lst) {
            Integer pp = yc.getPos() * 100 / yc.getCount();
            Integer np = yc.getNeg() * 100 / yc.getCount();
            System.out.println("count is " + yc.getCount());
            System.out.println("p is " + yc.getPos());
            System.out.println("n is " + yc.getNeg());
            System.out.println("pp is " + pp);
            System.out.println("np is " + np);
            csp.set(yc.getYear() + "", pp);
            csn.set(yc.getYear() + "", np);
        }
        ccmYearlyFillfilled.addSeries(csp);
        ccmYearlyFillfilled.addSeries(csn);
        return ccmYearlyFillfilled;
    }

    private LineChartModel lineModel2;

    private void createLineModels() {

    }

    public ResponseFor[] getResponsesFor() {
        return ResponseFor.values();
    }

    ResponseFor[] selectedResponseFors;

    public ResponseFor[] getSelectedResponseFors() {
        return selectedResponseFors;
    }

    public void setSelectedResponseFors(ResponseFor[] selectedResponseFors) {
        this.selectedResponseFors = selectedResponseFors;
    }

    public void fillMonthlyAnalysis() {
        LineChartModel model = new LineChartModel();

        Calendar fc = Calendar.getInstance();
        fc.setTime(fromDate);
        Calendar tc = Calendar.getInstance();
        tc.setTime(toDate);

        ReadableInstant fr = new DateTime(fc.get(Calendar.YEAR), fc.get(Calendar.MONTH) + 1, 1, 0, 0);
        ReadableInstant tr = new DateTime(tc.get(Calendar.YEAR), tc.get(Calendar.MONTH) + 1, 1, 0, 0);

        int monthsDifference = Months.monthsBetween(fr, tr).getMonths();

        System.out.println("monthsDifference = " + monthsDifference);

        Calendar beginingCalander = Calendar.getInstance();
        beginingCalander.setTime(fromDate);
        beginingCalander.set(Calendar.DATE, 1);
        beginingCalander.set(Calendar.HOUR, 0);
        beginingCalander.set(Calendar.MINUTE, 0);

        Date beginningDate = beginingCalander.getTime();
        System.out.println("beginningDate = " + beginningDate);

        String jpql;
        Map m;

        Response excellent = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Very Good'").get(0);
        Response veryGood = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Good'").get(0);
        Response good = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Normal'").get(0);
        Response poor = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Weak'").get(0);

        List<Response> responses = new ArrayList<Response>();

        Long maxVal = 10l;
        responses.add(excellent);
        responses.add(veryGood);
        responses.add(good);
        responses.add(poor);
        System.out.println("responses = " + responses);

        System.out.println("selectedResponseFors = " + selectedResponseFors);

//        ResponseFor[] rfs = (ResponseFor[]) selectedResponseFors.toArray();
        for (ResponseFor rf : selectedResponseFors) {

            for (Response r : responses) {
                ChartSeries chartSeries = new ChartSeries();
                String t = r.getName() + " - " + rf.toString();
                System.out.println("t = " + t);
                chartSeries.setLabel(t);

                for (int monthCount = 0; monthCount < monthsDifference; monthCount++) {

                    Calendar startDate = Calendar.getInstance();
                    startDate.setTime(beginningDate);
                    startDate.add(Calendar.MONTH, monthCount);
                    Calendar endDate = Calendar.getInstance();
                    endDate.setTime(startDate.getTime());
                    endDate.add(Calendar.MONTH, 1);
                    endDate.add(Calendar.DATE, -1);

                    m = new HashMap();
                    m.put("fd", startDate.getTime());
                    m.put("td", endDate.getTime());
                    m.put("r", r);

                    jpql = "Select count(q) from Questioner q where q.questionerDate between :fd and :td and q." + rf + "=:r";

                    System.out.println("m = " + m);
                    System.out.println("jpql = " + jpql);

                    String period = new SimpleDateFormat("yyyy MMM").format(startDate.getTime());
                    Long l = queFacade.findLongBySQL(jpql, m);
                    if (l > maxVal) {
                        maxVal = l;
                    }
                    chartSeries.set(period, l);
                    System.out.println("period = " + period);

                }

                model.addSeries(chartSeries);

            }
        }
        model.setTitle("Customer Satisfaction");
        model.setLegendPosition("e");
        model.setShowPointLabels(true);
        model.getAxes().put(AxisType.X, new CategoryAxis("Periods"));
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Responses");
        yAxis.setMin(0);
        yAxis.setMax(maxVal+ maxVal*.2);
        ccmReocrds = model;
    }

    public void fillQuartelyAnalysis() {
        LineChartModel model = new LineChartModel();

        Calendar fc = Calendar.getInstance();
        fc.setTime(fromDate);
        Calendar tc = Calendar.getInstance();
        tc.setTime(toDate);

        ReadableInstant fr = new DateTime(fc.get(Calendar.YEAR), fc.get(Calendar.MONTH) + 1, 1, 0, 0);
        ReadableInstant tr = new DateTime(tc.get(Calendar.YEAR), tc.get(Calendar.MONTH) + 1, 1, 0, 0);

        int monthsDifference = Months.monthsBetween(fr, tr).getMonths();

        System.out.println("monthsDifference = " + monthsDifference);

        Calendar beginingCalander = Calendar.getInstance();
        beginingCalander.setTime(fromDate);
        beginingCalander.set(Calendar.DATE, 1);
        beginingCalander.set(Calendar.HOUR, 0);
        beginingCalander.set(Calendar.MINUTE, 0);

        Date beginningDate = beginingCalander.getTime();
        System.out.println("beginningDate = " + beginningDate);

        String jpql;
        Map m;

        Response excellent = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Very Good'").get(0);
        Response veryGood = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Good'").get(0);
        Response good = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Normal'").get(0);
        Response poor = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Weak'").get(0);

        List<Response> responses = new ArrayList<Response>();

        Long maxVal = 10l;
        responses.add(excellent);
        responses.add(veryGood);
        responses.add(good);
        responses.add(poor);
        System.out.println("responses = " + responses);

        System.out.println("selectedResponseFors = " + selectedResponseFors);

//        ResponseFor[] rfs = (ResponseFor[]) selectedResponseFors.toArray();
        for (ResponseFor rf : selectedResponseFors) {

            for (Response r : responses) {
                ChartSeries chartSeries = new ChartSeries();
                String t = r.getName() + " - " + rf.toString();
                System.out.println("t = " + t);
                chartSeries.setLabel(t);

                for (int monthCount = 0; monthCount < monthsDifference; monthCount=monthCount+3) {

                    Calendar startDate = Calendar.getInstance();
                    startDate.setTime(beginningDate);
                    startDate.add(Calendar.MONTH, monthCount);
                    Calendar endDate = Calendar.getInstance();
                    endDate.setTime(startDate.getTime());
                    endDate.add(Calendar.MONTH, 3);
                    endDate.add(Calendar.DATE, -1);

                    m = new HashMap();
                    m.put("fd", startDate.getTime());
                    m.put("td", endDate.getTime());
                    m.put("r", r);

                    jpql = "Select count(q) from Questioner q where q.questionerDate between :fd and :td and q." + rf + "=:r";

                    System.out.println("m = " + m);
                    System.out.println("jpql = " + jpql);

                    int q = (startDate.get(Calendar.MONTH)/3)+1;
                    String period = new SimpleDateFormat("yyyy").format(startDate.getTime()) + " - " + q;
                    
                    
                    Long l = queFacade.findLongBySQL(jpql, m);
                    if (l > maxVal) {
                        maxVal = l;
                    }
                    chartSeries.set(period, l);
                    System.out.println("period = " + period);

                }

                model.addSeries(chartSeries);

            }
        }
        model.setTitle("Customer Satisfaction");
        model.setLegendPosition("e");
        model.setShowPointLabels(true);
        model.getAxes().put(AxisType.X, new CategoryAxis("Periods"));
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Responses");
        yAxis.setMin(0);
        yAxis.setMax(maxVal + maxVal*.2 );
        ccmReocrds = model;
    }

    public CartesianChartModel getCcmYearlyRepeatVisits() {
        List<Questioner> questioners;
        questioners = getQueFacade().findBySQL("select q from Questioner q order by q.questionerDate");
        List<YearCount> lst = new ArrayList<YearCount>();
        for (Questioner q : questioners) {
            if (q.getQuestionerDate() != null) {
                if (q.getPreviousVisits()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(q.getQuestionerDate());
                    addToYearCount(lst, c.get(Calendar.YEAR), 1, 1, 0);
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(q.getQuestionerDate());
                    addToYearCount(lst, c.get(Calendar.YEAR), 1, 0, 1);
                }
            }
        }
        ccmYearlyFillfilled = new CartesianChartModel();
        ChartSeries csp = new ChartSeries("Yes");
        ChartSeries csn = new ChartSeries("No");
        for (YearCount yc : lst) {
            Integer pp = yc.getPos() * 100 / yc.getCount();
            Integer np = yc.getNeg() * 100 / yc.getCount();
            System.out.println("count is " + yc.getCount());
            System.out.println("p is " + yc.getPos());
            System.out.println("n is " + yc.getNeg());
            System.out.println("pp is " + pp);
            System.out.println("np is " + np);
            csp.set(yc.getYear() + "", pp);
            csn.set(yc.getYear() + "", np);
        }
        ccmYearlyFillfilled.addSeries(csp);
        ccmYearlyFillfilled.addSeries(csn);
        return ccmYearlyFillfilled;
    }

    public void setCcmYearlyFillfilled(CartesianChartModel ccmYearlyFillfilled) {
        this.ccmYearlyFillfilled = ccmYearlyFillfilled;
    }

    /**
     * Creates a new instance of AnalysisController
     */
    public AnalysisController() {
    }

    public PersonFacade getPerFacade() {
        return perFacade;
    }

    public CartesianChartModel getCcmPersonFullfill() {
        return ccmPersonFullfill;
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
        if (personName == null || personName.trim().equals("")) {
            JsfUtil.addErrorMessage("Name ?");
            return;
        }
        Person temPerson = new Person();
        temPerson.setName(personName);
        temPerson.setDesignation(designation);
        getPerFacade().create(temPerson);
        JsfUtil.addSuccessMessage("Saved");
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

        Response excellent = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Very Good'").get(0);
        Response veryGood = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Good'").get(0);
        Response good = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Normal'").get(0);
        Response poor = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Weak'").get(0);

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

    public CartesianChartModel getCcmResponseHor() {
        ccmResponse = new CartesianChartModel();
        ChartSeries csExcellent = new ChartSeries("Excellent");
        ChartSeries csVeryGood = new ChartSeries("Very Good");
        ChartSeries csGood = new ChartSeries("Good");
        ChartSeries csWeak = new ChartSeries("Weak");

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

        Response excellent = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Very Good'").get(0);
        Response veryGood = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Good'").get(0);
        Response good = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Normal'").get(0);
        Response poor = getResFacade().findBySQL("SELECT r From Response r WHERE r.name = 'Weak'").get(0);

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

        csExcellent.set("Reception", a1);
        csExcellent.set("Courtesy", b1);
        csExcellent.set("Listening", c1);
        csExcellent.set("Reply", d1);
        csExcellent.set("Response", e1);
        csExcellent.set("Efficiency", f1);
        csExcellent.set("Facilities", g1);
        csExcellent.set("Overall", h1);

        csVeryGood.set("Reception", a2);
        csVeryGood.set("Courtesy", b2);
        csVeryGood.set("Listening", c2);
        csVeryGood.set("Reply", d2);
        csVeryGood.set("Response", e2);
        csVeryGood.set("Efficiency", f2);
        csVeryGood.set("Facilities", g2);
        csVeryGood.set("Overall", h2);

        csGood.set("Reception", a3);
        csGood.set("Courtesy", b3);
        csGood.set("Listening", c3);
        csGood.set("Reply", d3);
        csGood.set("Response", e3);
        csGood.set("Efficiency", f3);
        csGood.set("Facilities", g3);
        csGood.set("Overall", h3);

        csWeak.set("Reception", a4);
        csWeak.set("Courtesy", b4);
        csWeak.set("Listening", c4);
        csWeak.set("Reply", d4);
        csWeak.set("Response", e4);
        csWeak.set("Efficiency", f4);
        csWeak.set("Facilities", g4);
        csWeak.set("Overall", h4);

        ccmResponse.addSeries(csExcellent);
        ccmResponse.addSeries(csVeryGood);
        ccmResponse.addSeries(csGood);
        ccmResponse.addSeries(csWeak);
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
