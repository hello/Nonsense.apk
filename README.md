# nonsense-java

Simple tool for generating and hosting fake and/or cached Sense API data. A Java port of the original [nonsense project](https://github.com/hello/nonsense).

# Prerequisites

The `nonsense-java` project requires a copy of the JDK8 to run. Grab it from [oracle.com](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

# Installation

Download the latest jar from the [releases page](https://github.com/hello/nonsense-java/releases).

# Usage

Run the jar downloaded from the releases page through the java command, like so:

```
$ java -jar nonsense-release.jar
```

## Routes

The web server run by nonsense currently responds to the following Sense API calls:

- `POST /v1/oauth/token`: authenticates the user, accepts any input and returns a random token.
- `GET /v1/account`: returns fake user info consistent with the `POST /v1/oauth/token` route.
- `GET /v2/trends/:time-scale`: returns a trends object matching [the spec](https://github.com/hello/suripu-spec/blob/master/v2/trends.apib).
  - `:time-scale`: either `last_week`, `last_month`, or `last_3_months`.
- `GET /v1/timeline/:date`: returns an array of timelines.
  - `:date`: A date of the format `yyyy-MM-dd`
- `GET /v2/timeline/:date`: returns a single timeline object matching [the spec](https://github.com/hello/suripu-spec/blob/master/v2/trends.apib).
  - `:date`: A date of the format `yyyy-MM-dd`

### Route Query Parameters

The data routes included in nonsense support several non-spec query parameters that control
parameters of randomized data.

- `GET /v2/trends/:time-scale`
    - `ns_time_zone`: the time-zone the trends should be generated in, uses the format `+/-hh:mm`.
    - `ns_locale`: specifies the locale to use when generating textual data.
    - `ns_account_age`: specifies the age of the fake nonsense account, affects the `last_week` `:time-scale`.
- `GET /:version/timeline/:date`
    - `ns_time_zone`: the time-zone the timeline should be generated in, uses the format `+/-hh:mm`.
    - `ns_locale`: specifies the locale to use when generating textual data.

## Using data dumps

### Timeline

If you'd like to use a data dump of real timelines, use the `--timeline-cache` option:

```
$ java -jar nonsense-release.jar --timeline-cache ./assets/timeline-cache/timeline_1.txt
```

Requests to the timeline endpoints will return random timelines from the dump.

The `assets/timeline-caches` directory in this repository includes example timeline
responses in the expected format, one timeline JSON object per line.

# Development

Building the project requires maven, and a copy of IntelliJ.

## Importing

To import the nonsense-java project into IntelliJ, follow these steps:

- Select `Import Project` from Welcome to IntelliJ window
- Find and select the `nonsense-java` directory
- Select `Maven` in the `Import Project` window
- Leave the default project options
- Make sure the `is.hello:nonsense` artifact is selected on the `Maven projects to import` screen
- Select your copy of JDK 1.8 on the select project SDK screen
- Leave the project name and project file location fields on their default values
- Under the `Maven Projects` tab, select nonsense -> Lifecycle -> package

## Running

To set up the project to run nonsense, follow these steps:

- Edit Configurations for your project (in the down arrow menu next to run in a normal installation)
- Press the `+` button
- Select `Application`
- Call it `Run` and check `Single instance only`
- Enter `nonsense.Nonsense` for the main class
- Add any arguments you need (usually none)
- Save your changes
- Press the Run button

## Testing

To set up the project to run the nonsense tests, follow these steps:

- Edit Configurations for your project (in the down arrow menu next to run in a normal installation)
- Press the `+` button
- Select `JUnit`
- Call it `Tests`
- Select `All in package` under `Test kind`
- Enter `nonsense` for the package
- Select `Across module dependencies` for `Search for tests`
- Save your changes
- Press the Run button

---

Or, you can run the tests from the command line through maven like so:

```
$ mvn test
```

## Distribution

- Under the `Maven Projects` tab, select nonsense -> Plugins -> assembly -> assembly
- Copy the `target/nonsense-*-jar-with-dependencies.jar` file wherever you need it
