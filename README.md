# nonsense-java

Simple tool for generating and hosting fake and/or cached Sense API data. A java port of the original [nonsense project](https://github.com/hello/nonsense).

# prerequisites

The `nonsense-java` project requires a copy of the JDK8 to run. Grab it from [oracle.com](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

# installation

Download the latest jar from the releases page.

# usage

Run the jar downloaded from the releases page through the java command, like so:

```
$ java -jar nonsense-release.jar
```

## routes

The web server run by nonsense currently responds to the following Sense API calls:

- `POST /v1/oauth/token`: authenticates the user, accepts any input and returns a random token.
- `GET /v1/account`: returns fake user info consistent with the `POST /v1/oauth/token` route.
- `GET /v2/trends/:time-scale`: returns a trends object matching [the spec](https://github.com/hello/suripu-spec/blob/master/v2/trends.apib).
- `GET /v1/timeline/:date`: returns an array of timelines.
- `GET /v2/timeline/:date`: returns a single timeline object matching [the spec](https://github.com/hello/suripu-spec/blob/master/v2/trends.apib).

### route options

The data routes included in nonsense support several non-spec query parameters that control
parameters of randomized data.

- `GET /v2/trends/:time-scale`
    - `ns_time_zone`: the time-zone the trends should be generated in, uses the format `+/-hh:mm`.
    - `ns_locale`: specifies the locale to use when generating textual data.
    - `ns_account_age`: specifies the age of the fake nonsense account, affects the `last-week` `:time-scale`.
- `GET /:version/timeline/:date`
    - `ns_time_zone`: the time-zone the timeline should be generated in, uses the format `+/-hh:mm`.
    - `ns_locale`: specifies the locale to use when generating textual data.

## using data dumps

If you'd like to use a data dump of real timelines, use `--timeline-cache`:

```
$ java -jar nonsense-release.jar --timeline-cache ./assets/timeline-cache/timeline_1.txt
```

Requests to the timeline endpoints will return random timelines from the dump.

The `assets/timeline-caches` directory in this repository includes example timeline
responses in the expected format, one timeline JSON object per line.

# development

Building the project requires maven, and a copy of IntelliJ.
