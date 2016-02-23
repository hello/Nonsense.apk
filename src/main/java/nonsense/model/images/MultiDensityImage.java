package nonsense.model.images;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class MultiDensityImage {
    @JsonProperty("phone_1x")
    public final Optional<String> phoneDensityNormal;

    @JsonProperty("phone_2x")
    public final Optional<String> phoneDensityHigh;

    @JsonProperty("phone_3x")
    public final Optional<String> phoneDensityExtraHigh;

    public static MultiDensityImage newEmpty() {
        return new MultiDensityImage(Optional.empty(),
                                     Optional.empty(),
                                     Optional.empty());
    }

    public MultiDensityImage(@JsonProperty("phone_1x") final Optional<String> phoneDensityNormal,
                             @JsonProperty("phone_2x") final Optional<String> phoneDensityHigh,
                             @JsonProperty("phone_3x") final Optional<String> phoneDensityExtraHigh) {
        this.phoneDensityNormal = phoneDensityNormal;
        this.phoneDensityHigh = phoneDensityHigh;
        this.phoneDensityExtraHigh = phoneDensityExtraHigh;
    }

    @Override
    public String toString() {
        return "MultiDensityImage{" +
                "phoneDensityNormal=" + phoneDensityNormal +
                ", phoneDensityHigh=" + phoneDensityHigh +
                ", phoneDensityExtraHigh=" + phoneDensityExtraHigh +
                '}';
    }
}
