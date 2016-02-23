package nonsense.model.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import nonsense.model.Formats;
import nonsense.util.Enums;

public class Account {
    public static final String FAKE_ID = "not-a-real-account";

    @JsonProperty("id")
    public final String id;

    @JsonProperty("email")
    public final String email;

    @JsonProperty("tz")
    public final int timeZoneOffset;

    @JsonProperty("name")
    public final String name;

    @JsonProperty("gender")
    public final Gender gender;

    @JsonProperty("height")
    public final Integer height;

    @JsonProperty("weight")
    public final Integer weight;

    @JsonProperty("dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    public final LocalDate birthDate;

    @JsonProperty("created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE_TIME)
    public final DateTime created;

    @JsonProperty("last_modified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE_TIME)
    public final DateTime lastModified;


    public Account(@JsonProperty("id") String id,
                   @JsonProperty("email") String email,
                   @JsonProperty("tz") int timeZoneOffset,
                   @JsonProperty("name") String name,
                   @JsonProperty("gender") Gender gender,
                   @JsonProperty("height") Integer height,
                   @JsonProperty("weight") Integer weight,
                   @JsonProperty("dob")LocalDate birthDate,
                   @JsonProperty("created") DateTime created,
                   @JsonProperty("last_modified") DateTime lastModified) {
        this.id = id;
        this.email = email;
        this.timeZoneOffset = timeZoneOffset;
        this.name = name;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
        this.created = created;
        this.lastModified = lastModified;
    }
    

    public static Account createFake() {
        return new Account(FAKE_ID,
                           "fake@sayhello.com",
                           DateTimeZone.getDefault().getOffset(DateTimeUtils.currentTimeMillis()),
                           "Sam Samson",
                           Enums.random(Gender.values()),
                           177,
                           68039,
                           new LocalDate(1990, 4, 1),
                           new DateTime(2015, 2, 14, 0, 30, 0, 0, DateTimeZone.UTC), // First day of real data
                           DateTime.now());
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
