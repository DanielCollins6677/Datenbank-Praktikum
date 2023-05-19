import java.time.LocalDate;
import java.util.Date;

public class Review {
    private String ProdID;

    private short rating;

    private int helpful;

    private LocalDate date;

    private String user;

    private String summary;

    private String content;

    public Review() {
    }

    public Review(String prodID, short rating, int helpful, LocalDate date, String user, String summary, String content) {
        ProdID = prodID;
        this.rating = rating;
        this.helpful = helpful;
        this.date = date;
        this.user = user;
        this.summary = summary;
        this.content = content;
    }

    public String getProdID() {
        return ProdID;
    }

    public void setProdID(String prodID) {
        ProdID = prodID;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public int getHelpful() {
        return helpful;
    }

    public void setHelpful(int helpful) {
        this.helpful = helpful;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "ProdID='" + ProdID + '\'' +
                ", rating=" + rating +
                ", helpful=" + helpful +
                ", date=" + date +
                ", user='" + user + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
