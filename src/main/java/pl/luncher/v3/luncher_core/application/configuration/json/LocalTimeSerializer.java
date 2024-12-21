package pl.luncher.v3.luncher_core.application.configuration.json;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatterBuilder;

public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

  @Override
  public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeString(value.format(
        new DateTimeFormatterBuilder().appendValue(HOUR_OF_DAY, 2).appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2).toFormatter()));
  }
}
