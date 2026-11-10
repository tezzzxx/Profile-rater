package Model;

@Entity
public class rating {
    @Id
    private long Id;
    private long raterId;
    private long ratedId;
    private int picRating;
    private int biorating;
    private int overAllRating;
    private String profileComment;
    private LocalDateTime;
}
