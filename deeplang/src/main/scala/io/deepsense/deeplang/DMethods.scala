/**
 * Copyright (c) 2015, CodiLime Inc.
 */

package io.deepsense.deeplang

import scala.reflect.runtime.{universe => ru}

import io.deepsense.deeplang.inference.{InferenceWarnings, InferContext}


// TODO remove currying and make contexts parameters implicit

// code below is generated automatically
// scalastyle:off

abstract class DMethod0To1[
    P,
    TO_0 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (): TO_0

  def infer(context: InferContext)(parameters: P)(): (DKnowledge[TO_0], InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)()
    } else {
      inferTypes(context)(parameters)()
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(): (DKnowledge[TO_0], InferenceWarnings) = {(
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(): (DKnowledge[TO_0], InferenceWarnings) = {
    inferTypes(context)(parameters)()
  }

}

abstract class DMethod0To2[
    P,
    TO_0 <: DOperable : ru.TypeTag,
    TO_1 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (): (TO_0, TO_1)

  def infer(context: InferContext)(parameters: P)(): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)()
    } else {
      inferTypes(context)(parameters)()
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {((
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1])), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {
    inferTypes(context)(parameters)()
  }

}

abstract class DMethod0To3[
    P,
    TO_0 <: DOperable : ru.TypeTag,
    TO_1 <: DOperable : ru.TypeTag,
    TO_2 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(parameters: P)(): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)()
    } else {
      inferTypes(context)(parameters)()
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {((
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2])), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {
    inferTypes(context)(parameters)()
  }

}

abstract class DMethod1To0[
    P,
    TI_0 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0): Unit

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): (Unit, InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0)
    } else {
      inferTypes(context)(parameters)(k0)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): (Unit, InferenceWarnings) = {((), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): (Unit, InferenceWarnings) = {
    inferTypes(context)(parameters)(k0)
  }

}

abstract class DMethod1To1[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0): TO_0

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): (DKnowledge[TO_0], InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0)
    } else {
      inferTypes(context)(parameters)(k0)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): (DKnowledge[TO_0], InferenceWarnings) = {(
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): (DKnowledge[TO_0], InferenceWarnings) = {
    inferTypes(context)(parameters)(k0)
  }

}

abstract class DMethod1To2[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag,
    TO_1 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0): (TO_0, TO_1)

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0)
    } else {
      inferTypes(context)(parameters)(k0)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {((
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1])), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {
    inferTypes(context)(parameters)(k0)
  }

}

abstract class DMethod1To3[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag,
    TO_1 <: DOperable : ru.TypeTag,
    TO_2 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0)
    } else {
      inferTypes(context)(parameters)(k0)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {((
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2])), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {
    inferTypes(context)(parameters)(k0)
  }

}

abstract class DMethod2To0[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TI_1 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0, t1: TI_1): Unit

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): (Unit, InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0, k1)
    } else {
      inferTypes(context)(parameters)(k0, k1)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): (Unit, InferenceWarnings) = {((), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): (Unit, InferenceWarnings) = {
    inferTypes(context)(parameters)(k0, k1)
  }

}

abstract class DMethod2To1[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TI_1 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0, t1: TI_1): TO_0

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): (DKnowledge[TO_0], InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0, k1)
    } else {
      inferTypes(context)(parameters)(k0, k1)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): (DKnowledge[TO_0], InferenceWarnings) = {(
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): (DKnowledge[TO_0], InferenceWarnings) = {
    inferTypes(context)(parameters)(k0, k1)
  }

}

abstract class DMethod2To2[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TI_1 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag,
    TO_1 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0, t1: TI_1): (TO_0, TO_1)

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0, k1)
    } else {
      inferTypes(context)(parameters)(k0, k1)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {((
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1])), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {
    inferTypes(context)(parameters)(k0, k1)
  }

}

abstract class DMethod2To3[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TI_1 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag,
    TO_1 <: DOperable : ru.TypeTag,
    TO_2 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0, t1: TI_1): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0, k1)
    } else {
      inferTypes(context)(parameters)(k0, k1)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {((
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2])), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {
    inferTypes(context)(parameters)(k0, k1)
  }

}

abstract class DMethod3To0[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TI_1 <: DOperable : ru.TypeTag,
    TI_2 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0, t1: TI_1, t2: TI_2): Unit

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): (Unit, InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0, k1, k2)
    } else {
      inferTypes(context)(parameters)(k0, k1, k2)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): (Unit, InferenceWarnings) = {((), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): (Unit, InferenceWarnings) = {
    inferTypes(context)(parameters)(k0, k1, k2)
  }

}

abstract class DMethod3To1[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TI_1 <: DOperable : ru.TypeTag,
    TI_2 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0, t1: TI_1, t2: TI_2): TO_0

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): (DKnowledge[TO_0], InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0, k1, k2)
    } else {
      inferTypes(context)(parameters)(k0, k1, k2)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): (DKnowledge[TO_0], InferenceWarnings) = {(
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): (DKnowledge[TO_0], InferenceWarnings) = {
    inferTypes(context)(parameters)(k0, k1, k2)
  }

}

abstract class DMethod3To2[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TI_1 <: DOperable : ru.TypeTag,
    TI_2 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag,
    TO_1 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0, t1: TI_1, t2: TI_2): (TO_0, TO_1)

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0, k1, k2)
    } else {
      inferTypes(context)(parameters)(k0, k1, k2)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {((
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1])), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): ((DKnowledge[TO_0], DKnowledge[TO_1]), InferenceWarnings) = {
    inferTypes(context)(parameters)(k0, k1, k2)
  }

}

abstract class DMethod3To3[
    P,
    TI_0 <: DOperable : ru.TypeTag,
    TI_1 <: DOperable : ru.TypeTag,
    TI_2 <: DOperable : ru.TypeTag,
    TO_0 <: DOperable : ru.TypeTag,
    TO_1 <: DOperable : ru.TypeTag,
    TO_2 <: DOperable : ru.TypeTag] extends DMethod {
  def apply(context: ExecutionContext)(parameters: P)
           (t0: TI_0, t1: TI_1, t2: TI_2): (TO_0, TO_1, TO_2)

  def infer(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {
    if (context.fullInference) {
      inferFull(context)(parameters)(k0, k1, k2)
    } else {
      inferTypes(context)(parameters)(k0, k1, k2)
    }
  }

  protected def inferTypes(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {((
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_0]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_1]),
    DKnowledge(context.dOperableCatalog.concreteSubclassesInstances[TO_2])), InferenceWarnings.empty)
  }

  protected def inferFull(context: InferContext)(parameters: P)(
      k0: DKnowledge[TI_0],
      k1: DKnowledge[TI_1],
      k2: DKnowledge[TI_2]): ((DKnowledge[TO_0], DKnowledge[TO_1], DKnowledge[TO_2]), InferenceWarnings) = {
    inferTypes(context)(parameters)(k0, k1, k2)
  }

}

// scalastyle:on