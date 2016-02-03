package nonsense.model.insights;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.OptionalLong;

import nonsense.model.Formats;

public class Insight {
    @JsonProperty("account_id")
    public final OptionalLong accountId;

    @JsonProperty("title")
    public final String title;

    @JsonProperty("message")
    public final String message;

    @JsonProperty("category")
    public final Category category;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE_TIME)
    public final ZonedDateTime timestamp;

    @JsonProperty("info_preview")
    public final Optional<String> infoPreview;

    @JsonProperty("image")
    public final Optional<MultiDensityImage> image;

    public Insight(@JsonProperty("account_id") OptionalLong accountId,
                   @JsonProperty("title") String title,
                   @JsonProperty("message") String message,
                   @JsonProperty("category") Category category,
                   @JsonProperty("timestamp") ZonedDateTime timestamp,
                   @JsonProperty("info_preview") Optional<String> infoPreview,
                   @JsonProperty("image") Optional<MultiDensityImage> image) {
        this.accountId = accountId;
        this.title = title;
        this.message = message;
        this.category = category;
        this.timestamp = timestamp;
        this.infoPreview = infoPreview;
        this.image = image;
    }

    @JsonProperty("category_name")
    public String getCategoryName() {
        return category.toString()
                       .replace("_", " ")
                       .toLowerCase();
    }
}
