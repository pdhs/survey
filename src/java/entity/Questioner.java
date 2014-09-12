/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author buddhika
 */
@Entity
public class Questioner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date questionerDate;
    @Temporal(javax.persistence.TemporalType.TIME)
    Date questionerTime;
    @Lob
    String requirement;
    Boolean requirementSatisfied;
    Double TimeTakenHrs;
    Double TimeTakenMin;
    Boolean previousVisits;
    Integer noOfVisits;
    String officerName;
    String officerPost;
    @ManyToOne
    Response reception;
    @ManyToOne
    Response courtesy;
    @ManyToOne
    Response listening;
    @ManyToOne
    Response reply;
    @ManyToOne
    Response response;
    @ManyToOne
    Response efficiency;
    @ManyToOne
    Response facilities;
    @ManyToOne
    Response general;
    @Lob
    String suggestions;
    String name;
    @Lob
    String address;
    String telephone;

    @ManyToOne
    Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTimeTakenHrs() {
        return TimeTakenHrs;
    }

    public void setTimeTakenHrs(Double TimeTakenHrs) {
        this.TimeTakenHrs = TimeTakenHrs;
    }

    public Double getTimeTakenMin() {
        return TimeTakenMin;
    }

    public void setTimeTakenMin(Double TimeTakenMin) {
        this.TimeTakenMin = TimeTakenMin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Response getCourtesy() {
        return courtesy;
    }

    public void setCourtesy(Response courtesy) {
        this.courtesy = courtesy;
    }

    public Response getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Response efficiency) {
        this.efficiency = efficiency;
    }

    public Response getFacilities() {
        return facilities;
    }

    public void setFacilities(Response facilities) {
        this.facilities = facilities;
    }

    public Response getGeneral() {
        return general;
    }

    public void setGeneral(Response general) {
        this.general = general;
    }

    public Response getListening() {
        return listening;
    }

    public void setListening(Response listening) {
        this.listening = listening;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNoOfVisits() {
        return noOfVisits;
    }

    public void setNoOfVisits(Integer noOfVisits) {
        this.noOfVisits = noOfVisits;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getOfficerPost() {
        return officerPost;
    }

    public void setOfficerPost(String officerPost) {
        this.officerPost = officerPost;
    }

    public Boolean getPreviousVisits() {
        return previousVisits;
    }

    public void setPreviousVisits(Boolean previousVisits) {
        this.previousVisits = previousVisits;
    }

    public Date getQuestionerDate() {
        return questionerDate;
    }

    public void setQuestionerDate(Date questionerDate) {
        this.questionerDate = questionerDate;
    }

    public Date getQuestionerTime() {
        return questionerTime;
    }

    public void setQuestionerTime(Date questionerTime) {
        this.questionerTime = questionerTime;
    }

    public Response getReception() {
        return reception;
    }

    public void setReception(Response reception) {
        this.reception = reception;
    }

    public Response getReply() {
        return reply;
    }

    public void setReply(Response reply) {
        this.reply = reply;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public Boolean getRequirementSatisfied() {
        return requirementSatisfied;
    }

    public void setRequirementSatisfied(Boolean requirementSatisfied) {
        this.requirementSatisfied = requirementSatisfied;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Questioner)) {
            return false;
        }
        Questioner other = (Questioner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Questioner[ id=" + id + " ]";
    }

}
