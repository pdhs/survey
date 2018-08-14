/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author buddhika_ari
 */
public class ResponseSummery {

    ResponseFor responseFor;
    public double veryGoodCount;
    public double goodCount;
    public double averageCount;
    public double poorCount;
    public double veryPoorCount;
    public double notAnsweredCount;
    public double totalQuestionnears;
    public double totalResponses;

    public double veryGoodPercent;
    public double goodPercent;
    public double averagePercent;
    public double poorPercent;
    public double veryPoorPercent;
    public double notAnsweredPercent;

    public String title;
    public String xAxis;
    public String yAxis;

    public ResponseSummery() {
    }

    public ResponseSummery(ResponseFor responseFor) {
        this.responseFor = responseFor;
    }

    public ResponseFor getResponseFor() {
        return responseFor;
    }

    public void setResponseFor(ResponseFor responseFor) {
        this.responseFor = responseFor;
    }

    public double getVeryGoodCount() {
        return veryGoodCount;
    }

    public void setVeryGoodCount(double veryGoodCount) {
        this.veryGoodCount = veryGoodCount;
    }

    public double getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(double goodCount) {
        this.goodCount = goodCount;
    }

    public double getAverageCount() {
        return averageCount;
    }

    public void setAverageCount(double averageCount) {
        this.averageCount = averageCount;
    }

    public double getPoorCount() {
        return poorCount;
    }

    public void setPoorCount(double poorCount) {
        this.poorCount = poorCount;
    }

    public double getVeryPoorCount() {
        return veryPoorCount;
    }

    public void setVeryPoorCount(double veryPoorCount) {
        this.veryPoorCount = veryPoorCount;
    }

    public double getNotAnsweredCount() {
        return notAnsweredCount;
    }

    public void setNotAnsweredCount(double notAnsweredCount) {
        this.notAnsweredCount = notAnsweredCount;
    }

    public double getTotalQuestionnears() {
        return totalQuestionnears;
    }

    public void setTotalQuestionnears(double totalQuestionnears) {
        this.totalQuestionnears = totalQuestionnears;
    }

    public double getTotalResponses() {
        return totalResponses;
    }

    public void setTotalResponses(double totalResponses) {
        this.totalResponses = totalResponses;
    }

    public double getVeryGoodPercent() {
        return veryGoodPercent;
    }

    public void setVeryGoodPercent(double veryGoodPercent) {
        this.veryGoodPercent = veryGoodPercent;
    }

    public double getGoodPercent() {
        return goodPercent;
    }

    public void setGoodPercent(double goodPercent) {
        this.goodPercent = goodPercent;
    }

    public double getAveragePercent() {
        return averagePercent;
    }

    public void setAveragePercent(double averagePercent) {
        this.averagePercent = averagePercent;
    }

    public double getPoorPercent() {
        return poorPercent;
    }

    public void setPoorPercent(double poorPercent) {
        this.poorPercent = poorPercent;
    }

    public double getVeryPoorPercent() {
        return veryPoorPercent;
    }

    public void setVeryPoorPercent(double veryPoorPercent) {
        this.veryPoorPercent = veryPoorPercent;
    }

    public double getNotAnsweredPercent() {
        return notAnsweredPercent;
    }

    public void setNotAnsweredPercent(double notAnsweredPercent) {
        this.notAnsweredPercent = notAnsweredPercent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    

}
