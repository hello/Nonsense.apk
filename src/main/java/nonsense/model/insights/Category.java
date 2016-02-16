package nonsense.model.insights;

public enum Category {
    GENERIC("Insight"),
    SLEEP_HYGIENE("Sleep Hygiene"),
    LIGHT("Light"),
    SOUND("Sound"),
    TEMPERATURE("Temperature"),
    HUMIDITY("Humidity"),
    AIR_QUALITY("Air Quality"),
    SLEEP_DURATION("Sleep Duration"),
    TIME_TO_SLEEP("Time to Sleep"),
    SLEEP_TIME("Sleep Time"),
    WAKE_TIME("Wake Time"),
    WORKOUT("Work Out"),
    CAFFEINE("Caffeine"),
    ALCOHOL("Alcohol"),
    DIET("Diet"),
    DAYTIME_SLEEPINESS("Daytime Sleepiness"),
    DAYTIME_ACTIVITIES("Daytime Activities"),
    SLEEP_SCORE("Sleep Score"),
    SLEEP_QUALITY("Sleep Quality"),
    WAKE_VARIANCE("Wake Variance"),
    BED_LIGHT_DURATION("Bed Light Duration"),
    BED_LIGHT_INTENSITY_RATIO("Bed Light Intensity"),
    PARTNER_MOTION("Partner Motion");

    public final String title;

    Category(String title) {
        this.title = title;
    }
}
