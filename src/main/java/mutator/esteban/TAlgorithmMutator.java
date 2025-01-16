package mutator.esteban;

import java.util.ArrayList;
import utils.TRandomGenerator;
import algorithm.core.*;
import instruction.*;
import memory.TMemory;
import randomizer.*;


public class TAlgorithmMutator {
  private TAlgorithmMutation[] fActions;
  private double fMutateProb;
  private ArrayList<TAlgorithmComponentType> fMutableComponents = new ArrayList<TAlgorithmComponentType>();
  private int fSetupSizeMin;
  private int fSetupSizeMax;
  private int fPredictSizeMin;
  private int fPredictSizeMax;
  private int fLearnSizeMin;
  private int fLearnSizeMax;
  private TMemory fMemory;
  private TRandomGenerator fRand;
  private TAlgorithmOpsSet fAlgorithmOpsSet;

  public TAlgorithmMutator(TAlgorithmMutatorSpec spec, TMemory memory, TRandomGenerator rand) {
    this(spec.actions, spec.mutateProb, spec.minSetupSize, spec.maxSetupSize,
        spec.minPredictSize, spec.maxPredictSize, spec.minLearnSize, spec.maxLearnSize,
            new TAlgorithmOpsSet(spec.setupOps, spec.predictOps, spec.learnOps), memory, rand);
  }

  public TAlgorithmMutator(TAlgorithmMutation[] actions, double mutateProb, int setupSizeMin,
      int setupSizeMax, int predictSizeMin, int predictSizeMax, int learnSizeMin, int learnSizeMax,
      TAlgorithmOpsSet algorithmOpsSet, TMemory memory, TRandomGenerator rand) {
    fSetupSizeMin = setupSizeMin;
    fSetupSizeMax = setupSizeMax;
    fPredictSizeMin = predictSizeMin;
    fPredictSizeMax = predictSizeMax;
    fLearnSizeMin = learnSizeMin;
    fLearnSizeMax = learnSizeMax;
    fMemory = memory;
    fRand = rand;

    fMutateProb = mutateProb;
    fActions = actions;
    fAlgorithmOpsSet = algorithmOpsSet;
    for(TAlgorithmComponentType type : TAlgorithmComponentType.values()) {
      if (fAlgorithmOpsSet.hasOps(type))
        fMutableComponents.add(type);
    }
    if (fMutableComponents.isEmpty())
      throw new RuntimeException("Must mutate at least one component.");
  }

  int getComponentSizeMin(TAlgorithmComponentType type) {
    switch (type) {
      case SETUP:
        return fSetupSizeMin;
      case PREDICT:
        return fPredictSizeMin;
      case LEARN:
        return fLearnSizeMin;
      default:
        throw new RuntimeException("Should not reach here.");
    }
  }

  int getComponentSizeMax(TAlgorithmComponentType type) {
    switch (type) {
      case SETUP:
        return fSetupSizeMax;
      case PREDICT:
        return fPredictSizeMax;
      case LEARN:
        return fLearnSizeMax;
      default:
        throw new RuntimeException("Should not reach here.");
    }
  }

  TAlgorithmComponentType getRandomMutableComponentType() {
    int index = fRand.nextInt(0, fMutableComponents.size());
    return fMutableComponents.get(index);
  }

  int getRandomInstructionIndex(int componentSize) {
    return fRand.nextInt(0, componentSize);
  }

  public TAlgorithm mutate(TAlgorithm algorithm) {
    if (fMutateProb >= 1.0 || fRand.nextProbability() < fMutateProb) {
      mutateImpl(algorithm);
    }
    return algorithm;
  }

  public TAlgorithm mutate(int numOfMutations, TAlgorithm algorithm) {
    if (fMutateProb >= 1.0 || fRand.nextProbability() < fMutateProb) {
      for (int i = 0; i < numOfMutations; i++) {
        mutateImpl(algorithm);
      }
    }
    return algorithm;
  }

  private TAlgorithm mutateImpl(TAlgorithm algorithm) {
    int actionIndex = fRand.nextInt(0, fActions.length);
    TAlgorithmMutation action = fActions[actionIndex];
    switch (action) {
      case ALTER_PARAM:
        alterParam(algorithm);
        break;
      case RANDOMIZE_INSTRUCTION:
        randomizeInstruction(algorithm);
        break;
      case RANDOMIZE_COMPONENT:
        randomizeComponent(algorithm);
        break;
      case IDENTITY:
        break;
      case INSERT_INSTRUCTION:
        maybeInsertInstruction(algorithm);
        break;
      case REMOVE_INSTRUCTION:
        maybeRemoveInstruction(algorithm);
        break;
      case TRADE_INSTRUCTION:
        tradeInstruction(algorithm);
        break;
      case RANDOMIZE_ALGORITHM:
        randomizeAlgorithm(algorithm);
        break;
      // Do not add a default clause here. All actions should be supported.
    }
    return algorithm;
  }

  TAlgorithm alterParam(TAlgorithm algorithm) {
    TAlgorithmComponentType type = getRandomMutableComponentType();
    ArrayList<TInstruction> component = algorithm.getComponent(type);
    if (!component.isEmpty()) {
      int index = getRandomInstructionIndex(component.size());
      TInstruction instr = component.get(index);
      TInstructionRandomizer.alterParam(instr, type, fMemory, fRand);
    }
    return algorithm;
  }

  TAlgorithm randomizeInstruction(TAlgorithm algorithm) {
    TAlgorithmComponentType type = getRandomMutableComponentType();
    ArrayList<TInstruction> component = algorithm.getComponent(type);
    if (!component.isEmpty()) {
      int index = getRandomInstructionIndex(component.size());
      TOp op = fAlgorithmOpsSet.getRandomOp(type, fRand);
      TInstructionRandomizer.setOpAndRandomize(component.get(index), op, type, fMemory, fRand);
    }
    return algorithm;
  }

  TAlgorithm randomizeComponent(TAlgorithm algorithm) {
    TAlgorithmComponentType type = getRandomMutableComponentType();
    TAlgorithmRandomizer.execute(algorithm, type, fAlgorithmOpsSet, fMemory, fRand);
    return algorithm;
  }

  TAlgorithm maybeInsertInstruction(TAlgorithm algorithm) {
    TAlgorithmComponentType type = getRandomMutableComponentType();
    ArrayList<TInstruction> component = algorithm.getComponent(type);
    if (component.size() >= getComponentSizeMax(type))
      return algorithm;
    TOp op = fAlgorithmOpsSet.getRandomOp(type, fRand);
    insertInstruction(op, type, component);
    return algorithm;
  }

  TAlgorithm maybeRemoveInstruction(TAlgorithm algorithm) {
    TAlgorithmComponentType type = getRandomMutableComponentType();
    ArrayList<TInstruction> component = algorithm.getComponent(type);
    if (component.size() <= getComponentSizeMin(type))
      return algorithm;
    removeInstruction(component);
    return algorithm;
  }

  TAlgorithm tradeInstruction(TAlgorithm algorithm) {
    TAlgorithmComponentType type = getRandomMutableComponentType();
    ArrayList<TInstruction> component = algorithm.getComponent(type);
    TOp op = fAlgorithmOpsSet.getRandomOp(type, fRand);
    insertInstruction(op, type, component);
    removeInstruction(component);
    return algorithm;
  }

  TAlgorithm randomizeAlgorithm(TAlgorithm algorithm) {
    for(TAlgorithmComponentType type : fMutableComponents) {
      TAlgorithmRandomizer.execute(algorithm, type, fAlgorithmOpsSet, fMemory, fRand);
    }
    return algorithm;
  }

  private int insertInstruction(TOp op, TAlgorithmComponentType type, ArrayList<TInstruction> component) {
    int position = getRandomInstructionIndex(component.size() + 1);
    TInstruction instr = TInstructionRandomizer.makeInstructionAndRandomize(op, type, fMemory, fRand);
    component.add(position, instr);
    return position;
  }

  private int removeInstruction(ArrayList<TInstruction> component) {
    int position = getRandomInstructionIndex(component.size());
    component.remove(position);
    return position;
  }
}
