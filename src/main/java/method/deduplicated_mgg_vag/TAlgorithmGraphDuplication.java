package method.deduplicated_mgg_vag;

import java.util.ArrayList;
import java.util.HashMap;

import algorithm.core.TAlgorithmGraph;

public class TAlgorithmGraphDuplication {
  private HashMap<Long, ArrayList<TAlgorithmGraph>> fDiscreteDuplicationHashMap = new HashMap<Long, ArrayList<TAlgorithmGraph>>();
  private HashMap<Long, TAlgorithmGraph> fDuplicationHashMap = new HashMap<Long, TAlgorithmGraph>();

  boolean existsDiscreteDuplication(TAlgorithmGraph algorithmGraph) {
    return fDiscreteDuplicationHashMap.containsKey(algorithmGraph.getDiscreteCode());
  }

  boolean existsDuplication(TAlgorithmGraph algorithmGraph) {
    return fDuplicationHashMap.containsKey(algorithmGraph.getCode());
  }

  void add(TAlgorithmGraph algorithmGraph) {
    Long discreteCode = algorithmGraph.getDiscreteCode();
    if (!fDiscreteDuplicationHashMap.containsKey(discreteCode)) {
      fDiscreteDuplicationHashMap.put(discreteCode, new ArrayList<TAlgorithmGraph>());
    }
    fDiscreteDuplicationHashMap.get(discreteCode).add(algorithmGraph);
    fDuplicationHashMap.put(algorithmGraph.getCode(), algorithmGraph);
  }

  void remove(TAlgorithmGraph algorithmGraph) {
    Long discreteCode = algorithmGraph.getDiscreteCode();
    fDiscreteDuplicationHashMap.get(discreteCode).remove(algorithmGraph);
    if (fDiscreteDuplicationHashMap.get(discreteCode).isEmpty()) {
      fDiscreteDuplicationHashMap.remove(discreteCode);
    }
    fDuplicationHashMap.remove(algorithmGraph.getCode());
  }

  int countDiscreteDuplication(TAlgorithmGraph algorithmGraph) {
    Long discreteCode = algorithmGraph.getDiscreteCode();
    if (!fDiscreteDuplicationHashMap.containsKey(discreteCode)) {
      return 0;
    }
    return fDiscreteDuplicationHashMap.get(discreteCode).size();
  }
}
