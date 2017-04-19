
package space.bobcheng.myapplication.jsonClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CertainRecord {

    @SerializedName("first_seats")
    @Expose
    private String firstSeats;
    @SerializedName("second_seats")
    @Expose
    private String secondSeats;
    @SerializedName("wz")
    @Expose
    private String wz;
    @SerializedName("ticket_type")
    @Expose
    private String ticketType;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("lishi")
    @Expose
    private String lishi;
    @SerializedName("start_from")
    @Expose
    private String startFrom;
    @SerializedName("yz")
    @Expose
    private String yz;
    @SerializedName("rw")
    @Expose
    private String rw;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("train_id")
    @Expose
    private String trainId;
    @SerializedName("end_to")
    @Expose
    private String endTo;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("yw")
    @Expose
    private String yw;
    @SerializedName("ticket_left")
    @Expose
    private String ticketLeft;

    public String getFirstSeats() {
        return firstSeats;
    }

    public void setFirstSeats(String firstSeats) {
        this.firstSeats = firstSeats;
    }

    public String getSecondSeats() {
        return secondSeats;
    }

    public void setSecondSeats(String secondSeats) {
        this.secondSeats = secondSeats;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLishi() {
        return lishi;
    }

    public void setLishi(String lishi) {
        this.lishi = lishi;
    }

    public String getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(String startFrom) {
        this.startFrom = startFrom;
    }

    public String getYz() {
        return yz;
    }

    public void setYz(String yz) {
        this.yz = yz;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getEndTo() {
        return endTo;
    }

    public void setEndTo(String endTo) {
        this.endTo = endTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYw() {
        return yw;
    }

    public void setYw(String yw) {
        this.yw = yw;
    }

    public String getTicketLeft() {
        return ticketLeft;
    }

    public void setTicketLeft(String ticketLeft) {
        this.ticketLeft = ticketLeft;
    }

}
