package nonsense.model.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class Account {
    public static final String FAKE_ID = "not-a-real-account";

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("tz")
    private int timeZoneOffset;

    @JsonProperty("name")
    private String name;

    @JsonProperty("gender")
    private Gender gender;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("weight")
    private Integer weight;

    @JsonProperty("dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @JsonProperty("created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime created;

    @JsonProperty("last_modified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private ZonedDateTime lastModified;


    public static Account createFake() {
        final Account account = new Account();
        account.id = FAKE_ID;
        account.email = "fake@sayhello.com";
        account.timeZoneOffset = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        account.name = "Notareal Boy";
        account.created = ZonedDateTime.of(2015, 2, 14, 0, 30, 0, 0, ZoneId.of("UTC")); // First day of real data
        account.gender = Gender.MALE;
        account.height = 177;
        account.weight = 68039;
        account.birthDate = LocalDate.of(1990, 4, 1);
        account.lastModified = ZonedDateTime.now();
        return account;
    }


    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", timeZoneOffset=" + timeZoneOffset +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", birthDate=" + birthDate +
                ", created=" + created +
                ", lastModified=" + lastModified +
                '}';
    }
}
