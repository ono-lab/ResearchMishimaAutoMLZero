package experiments;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 実験の種類
 */
public enum TExperimentType {
  LINEAR_REGRESSION(),

  AFFINE_REGRESSION(),

  NOIZY_AFFINE_REGRESSION(),

  TWO_LAYERS_NN_REGRESSION(),

  LINEAR_CLASSIFICATION(),

  NOIZY_LINEAR_CLASSIFICATION(),

  AFFINE_CLASSIFICATION(),

  TWO_LAYERS_NN_CLASSIFICATION();

  public String getName() {
    return this.name().toLowerCase();
  }

  public TExperimentSpec getSpec(TExperimentInstance instance) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      File config = new File(instance.getExperimentFilePath());
      return objectMapper.readValue(config, TExperimentSpec.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}