package space.bobcheng.myapplication.jsonClass;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("ticket_type")
    @Expose
    private String ticketType;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("start_from")
    @Expose
    private String startFrom;
    @SerializedName("end_to")
    @Expose
    private String endTo;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ticket_left")
    @Expose
    private Boolean ticketLeft;

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(String startFrom) {
        this.startFrom = startFrom;
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

    public Boolean getTicketLeft() {
        return ticketLeft;
    }

    public void setTicketLeft(Boolean ticketLeft) {
        this.ticketLeft = ticketLeft;
    }

}