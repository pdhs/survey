/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import Data.ResponseFor;
import Data.ResponseSummery;
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
import org.primefaces.model.chart.BarChartModel;
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
    private int personOrderNo;
    String personName;
    //
    Person person;
    //
    DataModel<Person> persons;
    //
    PieChartModel pcmFullfilled;
    PieChartModel pcmPreviousVisits;
    BarChartModel ccmDuration;
    BarChartModel ccmReturnCount;
    BarChartModel ccmResponse;
    BarChartModel ccmPersonFullfill;
    BarChartModel pcmYearlyPreviousVisits;
    BarChartModel ccmYearlyFillfilled;

    private BarChartModel bcReception;
    private BarChartModel bcListening;
    private BarChartModel bcReply;
    private BarChartModel bcResponse;
    private BarChartModel bcEfficiency;
    private BarChartModel bcFacilities;
    private BarChartModel bcGeneral;

    private LineChartModel lcReception;
    private LineChartModel lcListening;
    private LineChartModel lcReply;
    private LineChartModel lcResponse;
    private LineChartModel lcEfficiency;
    private LineChartModel llcFacilities;
    private LineChartModel lcGeneral;

    private ResponseSummery reception;
    private ResponseSummery listening;
    private ResponseSummery response;
    private ResponseSummery reply;
    private ResponseSummery efficiency;
    private ResponseSummery facilities;
    private ResponseSummery general;

    LineChartModel ccmReocrds;

    private BarChartModel barChartModel;

    String designation;

    Date fromDate;
    Date toDate;

    public String toCreatePeriodAnalysisOfAllResponses() {
        resetAllModelsForPeriosAnalysis();
        return "/period_analysis";
    }

    public void resetAllModelsForPeriosAnalysis() {
        bcReception = new BarChartModel();
        bcListening = new BarChartModel();
        bcReply = new BarChartModel();
        bcResponse = new BarChartModel();
        bcEfficiency = new BarChartModel();
        bcFacilities = new BarChartModel();
        bcGeneral = new BarChartModel();
    }

    public ResponseSummery calculateResponseSummery(ResponseSummery rs) {
        System.out.println("rs.getResponseFor() = " + rs.getResponseFor());
        rs.totalResponses = rs.veryGoodCount + rs.goodCount + rs.averageCount + rs.poorCount + rs.veryPoorCount;
        System.out.println("rs.totalResponses = " + rs.totalResponses);
        rs.totalQuestionnears = rs.totalResponses + rs.notAnsweredCount;
        System.out.println("rs.totalQuestionnears = " + rs.totalQuestionnears);
        if (rs.totalResponses == 0) {
            return rs;
        }
        rs.veryGoodPercent = (rs.veryGoodCount / rs.totalResponses) * 100;
        System.out.println("rs.veryGoodCount = " + rs.veryGoodCount);
        System.out.println("rs.totalResponses = " + rs.totalResponses);
        System.out.println("rs.veryGoodPercent = " + rs.veryGoodPercent);
        rs.goodPercent = (rs.goodCount / rs.totalResponses) * 100;
        System.out.println("rs.goodPercent = " + rs.goodPercent);
        rs.averagePercent = (rs.averageCount / rs.totalResponses) * 100;
        System.out.println("rs.averagePercent = " + rs.averagePercent);
        rs.poorPercent = (rs.poorCount / rs.totalResponses) * 100;
        System.out.println("rs.poorPercent = " + rs.poorPercent);
        rs.veryPoorPercent = (rs.veryPoorCount / rs.totalResponses) * 100;
        System.out.println("rs.veryPoorPercent = " + rs.veryPoorPercent);
        return rs;
    }

    public BarChartModel createBarChartModel(BarChartModel model, ResponseSummery r) {
        model = new BarChartModel();
        model.setAnimate(true);
        model.getAxis(AxisType.Y).setMax(100);
        model.getAxis(AxisType.Y).setMin(0);

        ChartSeries responsesSeries = new ChartSeries();
        responsesSeries.set("ඉතා හොදයි", r.veryGoodPercent);
        responsesSeries.set("හොදයි", r.goodPercent);
        responsesSeries.set("සාමාන්‍යයි", r.averagePercent);
        responsesSeries.set("දුර්වලයි", r.poorCount);
        responsesSeries.set("ඉතා දුර්වලයි", r.veryPoorCount);

        model.addSeries(responsesSeries);

        return model;

    }

    public void createPeriodAnalysisOfAllResponses() {
        System.out.println("createPeriodAnalysisOfAllResponses");
        List<Questioner> questioners;
        String j = "select q from Questioner q where q.questionerDate between :fd and :td";
        Map m = new HashMap();
        m.put("fd", fromDate);
        m.put("td", toDate);
        questioners = getQueFacade().findBySQL(j, m);

        System.out.println("questioners.size() = " + questioners.size());

        reception = new ResponseSummery(ResponseFor.reception);
        listening = new ResponseSummery(ResponseFor.listening);
        response = new ResponseSummery(ResponseFor.response);
        reply = new ResponseSummery(ResponseFor.reply);
        efficiency = new ResponseSummery(ResponseFor.efficiency);
        facilities = new ResponseSummery(ResponseFor.facilities);
        general = new ResponseSummery(ResponseFor.general);

        List<Response> res = getResFacade().findBySQL("select r from Response r order by r.id");
        System.out.println("res.size = " + res.size());
        if (res.size() < 5) {
            JsfUtil.addErrorMessage("Response Error. Less than one.");
            return;
        }
        Response vg = res.get(0);
        Response g = res.get(1);
        Response a = res.get(2);
        Response p = res.get(3);
        Response vp = res.get(4);

        for (Questioner q : questioners) {
            System.out.println("q = " + q);
            if (q.getReception() == null) {
                reception.notAnsweredCount++;
            } else if (q.getReception().equals(vg)) {
                reception.veryGoodCount++;
            } else if (q.getReception().equals(g)) {
                reception.goodCount++;
            } else if (q.getReception().equals(a)) {
                reception.averageCount++;
            } else if (q.getReception().equals(p)) {
                reception.poorCount++;
            } else if (q.getReception().equals(vp)) {
                reception.veryPoorCount++;
            }

            if (q.getListening() == null) {
                listening.notAnsweredCount++;
            } else if (q.getListening().equals(vg)) {
                listening.veryGoodCount++;
            } else if (q.getListening().equals(g)) {
                listening.goodCount++;
            } else if (q.getListening().equals(a)) {
                listening.averageCount++;
            } else if (q.getListening().equals(p)) {
                listening.poorCount++;
            } else if (q.getListening().equals(vp)) {
                listening.veryPoorCount++;
            }

            if (q.getResponse() == null) {
                response.notAnsweredCount++;
            } else if (q.getResponse().equals(vg)) {
                response.veryGoodCount++;
            } else if (q.getResponse().equals(g)) {
                response.goodCount++;
            } else if (q.getResponse().equals(a)) {
                response.averageCount++;
            } else if (q.getResponse().equals(p)) {
                response.poorCount++;
            } else if (q.getResponse().equals(vp)) {
                response.veryPoorCount++;
            }

            if (q.getReply() == null) {
                reply.notAnsweredCount++;
            } else if (q.getReply().equals(vg)) {
                reply.veryGoodCount++;
            } else if (q.getReply().equals(g)) {
                reply.goodCount++;
            } else if (q.getReply().equals(a)) {
                reply.averageCount++;
            } else if (q.getReply().equals(p)) {
                reply.poorCount++;
            } else if (q.getReply().equals(vp)) {
                reply.veryPoorCount++;
            }

            if (q.getEfficiency() == null) {
                efficiency.notAnsweredCount++;
            } else if (q.getEfficiency().equals(vg)) {
                efficiency.veryGoodCount++;
            } else if (q.getEfficiency().equals(g)) {
                efficiency.goodCount++;
            } else if (q.getEfficiency().equals(a)) {
                efficiency.averageCount++;
            } else if (q.getEfficiency().equals(p)) {
                efficiency.poorCount++;
            } else if (q.getEfficiency().equals(vp)) {
                efficiency.veryPoorCount++;
            }

            if (q.getFacilities() == null) {
                facilities.notAnsweredCount++;
            } else if (q.getFacilities().equals(vg)) {
                facilities.veryGoodCount++;
            } else if (q.getFacilities().equals(g)) {
                facilities.goodCount++;
            } else if (q.getFacilities().equals(a)) {
                facilities.averageCount++;
            } else if (q.getFacilities().equals(p)) {
                facilities.poorCount++;
            } else if (q.getFacilities().equals(vp)) {
                facilities.veryPoorCount++;
            }

            if (q.getGeneral() == null) {
                general.notAnsweredCount++;
            } else if (q.getGeneral().equals(vg)) {
                general.veryGoodCount++;
            } else if (q.getGeneral().equals(g)) {
                general.goodCount++;
            } else if (q.getGeneral().equals(a)) {
                general.averageCount++;
            } else if (q.getGeneral().equals(p)) {
                general.poorCount++;
            } else if (q.getGeneral().equals(vp)) {
                general.veryPoorCount++;
            }

        }

        reception = calculateResponseSummery(reception);
        response = calculateResponseSummery(response);
        listening = calculateResponseSummery(listening);
        reply = calculateResponseSummery(reply);
        efficiency = calculateResponseSummery(efficiency);
        facilities = calculateResponseSummery(facilities);
        general = calculateResponseSummery(general);

        bcReception = createBarChartModel(bcReception, reception);
        bcResponse = createBarChartModel(bcResponse, response);
        bcListening = createBarChartModel(bcListening, listening);
        bcReply = createBarChartModel(bcReply, reply);
        bcEfficiency = createBarChartModel(bcEfficiency, efficiency);
        bcFacilities = createBarChartModel(bcFacilities, facilities);
        bcGeneral = createBarChartModel(bcGeneral, general);

    }

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

    public LineChartModel getCcmReocrds() {
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

    public BarChartModel getPcmYearlyPreviousVisits() {
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
        pcmYearlyPreviousVisits = new BarChartModel();
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

    public void setPcmYearlyPreviousVisits(BarChartModel pcmYearlyPreviousVisits) {
        this.pcmYearlyPreviousVisits = pcmYearlyPreviousVisits;
    }

    public BarChartModel getCcmYearlyFillfilled() {
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
        ccmYearlyFillfilled = new BarChartModel();
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

        List<Response> rs = getResFacade().findAll();

        Response veryGood = rs.get(0);
        Response good = rs.get(1);
        Response average = rs.get(2);
        Response poor = rs.get(3);
        Response worst = rs.get(4);

        List<Response> responses = new ArrayList<Response>();

        Long maxVal = 10l;
        responses.add(veryGood);
        responses.add(good);
        responses.add(average);
        responses.add(poor);
        responses.add(worst);
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
        yAxis.setMax(maxVal + maxVal * .2);
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

        List<Response> ers = getResFacade().findBySQL("SELECT r From Response r order by r.id");

        Response vg = ers.get(0);
        Response g = ers.get(1);
        Response a = ers.get(2);
        Response p = ers.get(3);
        Response vp = ers.get(4);

        List<Response> responses = new ArrayList<Response>();

        Long maxVal = 10l;
        responses.add(vg);
        responses.add(g);
        responses.add(a);
        responses.add(p);
        responses.add(vp);
        System.out.println("responses = " + responses);

        System.out.println("selectedResponseFors = " + selectedResponseFors);

//        ResponseFor[] rfs = (ResponseFor[]) selectedResponseFors.toArray();
        for (ResponseFor rf : selectedResponseFors) {

            for (Response r : responses) {
                ChartSeries chartSeries = new ChartSeries();
                String t = r.getName() + " - " + rf.toString();
                System.out.println("t = " + t);
                chartSeries.setLabel(t);

                for (int monthCount = 0; monthCount < monthsDifference; monthCount = monthCount + 3) {

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

                    int q = (startDate.get(Calendar.MONTH) / 3) + 1;
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
        yAxis.setMax(maxVal + maxVal * .2);
        ccmReocrds = model;
    }

    public BarChartModel getCcmYearlyRepeatVisits() {
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
        ccmYearlyFillfilled = new BarChartModel();
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

    public void setCcmYearlyFillfilled(BarChartModel ccmYearlyFillfilled) {
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

    public BarChartModel getCcmPersonFullfill() {
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
        temPerson.setOrderNo(personOrderNo);
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
            if (q.getNoOfVisits() == null || q.getNoOfVisits() < 2) {
                noCount++;
            } else {
                yesCount++;
            }
        }
        pcmPreviousVisits.set("Yes", yesCount);
        pcmPreviousVisits.set("No", noCount);

        return pcmPreviousVisits;
    }

    public BarChartModel getCcmResponse() {
        ccmResponse = new BarChartModel();
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

    public BarChartModel getCcmResponseHor() {
        ccmResponse = new BarChartModel();
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

    public BarChartModel getCcmReturnCount() {
        ccmReturnCount = new BarChartModel();
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

    public BarChartModel getCcmDuration() {
        ccmDuration = new BarChartModel();
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

    public int getPersonOrderNo() {
        return personOrderNo;
    }

    public void setPersonOrderNo(int personOrderNo) {
        this.personOrderNo = personOrderNo;
    }

    public BarChartModel getBcReception() {
        return bcReception;
    }

    public void setBcReception(BarChartModel bcReception) {
        this.bcReception = bcReception;
    }

    public BarChartModel getBcListening() {
        return bcListening;
    }

    public void setBcListening(BarChartModel bcListening) {
        this.bcListening = bcListening;
    }

    public BarChartModel getBcReply() {
        return bcReply;
    }

    public void setBcReply(BarChartModel bcReply) {
        this.bcReply = bcReply;
    }

    public BarChartModel getBcResponse() {
        return bcResponse;
    }

    public void setBcResponse(BarChartModel bcResponse) {
        this.bcResponse = bcResponse;
    }

    public BarChartModel getBcEfficiency() {
        return bcEfficiency;
    }

    public void setBcEfficiency(BarChartModel bcEfficiency) {
        this.bcEfficiency = bcEfficiency;
    }

    public BarChartModel getBcFacilities() {
        return bcFacilities;
    }

    public void setBcFacilities(BarChartModel bcFacilities) {
        this.bcFacilities = bcFacilities;
    }

    public BarChartModel getBcGeneral() {
        return bcGeneral;
    }

    public void setBcGeneral(BarChartModel bcGeneral) {
        this.bcGeneral = bcGeneral;
    }

    public BarChartModel getBarChartModel() {
        return barChartModel;
    }

    public void setBarChartModel(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }

    public LineChartModel getLineModel2() {
        return lineModel2;
    }

    public void setLineModel2(LineChartModel lineModel2) {
        this.lineModel2 = lineModel2;
    }

    public ResponseSummery getReception() {
        return reception;
    }

    public void setReception(ResponseSummery reception) {
        this.reception = reception;
    }

    public ResponseSummery getListening() {
        return listening;
    }

    public void setListening(ResponseSummery listening) {
        this.listening = listening;
    }

    public ResponseSummery getResponse() {
        return response;
    }

    public void setResponse(ResponseSummery response) {
        this.response = response;
    }

    public ResponseSummery getReply() {
        return reply;
    }

    public void setReply(ResponseSummery reply) {
        this.reply = reply;
    }

    public ResponseSummery getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(ResponseSummery efficiency) {
        this.efficiency = efficiency;
    }

    public ResponseSummery getFacilities() {
        return facilities;
    }

    public void setFacilities(ResponseSummery facilities) {
        this.facilities = facilities;
    }

    public ResponseSummery getGeneral() {
        return general;
    }

    public void setGeneral(ResponseSummery general) {
        this.general = general;
    }

    public LineChartModel getLcReception() {
        return lcReception;
    }

    public void setLcReception(LineChartModel lcReception) {
        this.lcReception = lcReception;
    }

    public LineChartModel getLcListening() {
        return lcListening;
    }

    public void setLcListening(LineChartModel lcListening) {
        this.lcListening = lcListening;
    }

    public LineChartModel getLcReply() {
        return lcReply;
    }

    public void setLcReply(LineChartModel lcReply) {
        this.lcReply = lcReply;
    }

    public LineChartModel getLcResponse() {
        return lcResponse;
    }

    public void setLcResponse(LineChartModel lcResponse) {
        this.lcResponse = lcResponse;
    }

    public LineChartModel getLcEfficiency() {
        return lcEfficiency;
    }

    public void setLcEfficiency(LineChartModel lcEfficiency) {
        this.lcEfficiency = lcEfficiency;
    }

    public LineChartModel getLlcFacilities() {
        return llcFacilities;
    }

    public void setLlcFacilities(LineChartModel llcFacilities) {
        this.llcFacilities = llcFacilities;
    }

    public LineChartModel getLcGeneral() {
        return lcGeneral;
    }

    public void setLcGeneral(LineChartModel lcGeneral) {
        this.lcGeneral = lcGeneral;
    }

    
    
}
