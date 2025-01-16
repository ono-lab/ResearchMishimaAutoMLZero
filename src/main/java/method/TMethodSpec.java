package method;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TMethodSpec {
  public TMethodSpec fromJSONFile(String filepath) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      File config = new File(filepath);
      TMethodSpec spec = objectMapper.readValue(config, getClass());
      return spec;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public abstract TMethod toMethod();
}
