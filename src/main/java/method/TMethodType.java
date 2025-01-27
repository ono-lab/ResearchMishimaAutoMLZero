package method;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import experiments.TExperimentInstance;
import method.deduplicated_mgg_vag.TDeduplicatedMGGAutoMLZeroVAGSpec;
import method.mgg_vag.TMGGAutoMLZeroVAGSpec;
import method.re.TREAutoMLZeroSpec;
import method.re_vag.TREAutoMLZeroVAGSpec;

public enum TMethodType {
  RE_AUTO_ML_ZERO(),

  RE_AUTO_ML_ZERO_VAG(),

  MGG_AUTO_ML_ZERO_VAG(),

  DEDUPLICATED_MGG_AUTO_ML_ZERO_VAG();

  public String getName() {
    return this.name().toLowerCase();
  }

  public TMethodSpec getSpec(TExperimentInstance instance) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      File config = new File(instance.getMethodFilePath());
      switch (this) {
        case RE_AUTO_ML_ZERO:
          return objectMapper.readValue(config, TREAutoMLZeroSpec.class);
        case RE_AUTO_ML_ZERO_VAG:
          return objectMapper.readValue(config, TREAutoMLZeroVAGSpec.class);
        case MGG_AUTO_ML_ZERO_VAG:
          return objectMapper.readValue(config, TMGGAutoMLZeroVAGSpec.class);
        case DEDUPLICATED_MGG_AUTO_ML_ZERO_VAG:
          return objectMapper.readValue(config, TDeduplicatedMGGAutoMLZeroVAGSpec.class);
        default:
          throw new IllegalArgumentException("Method not supported: " + this);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
