/**
 * Copyright 2015, deepsense.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.deepsense.deeplang.parameters

import scala.collection.immutable.ListMap
import scala.util.matching.Regex

import spray.json.DefaultJsonProtocol._
import spray.json._

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.parameters.exceptions.VariableNotDefinedException

case class BooleanParameter(
    description: String,
    default: Option[Boolean],
    var _value: Option[Boolean] = None)
  extends Parameter
  with CanHaveDefault {

  type HeldValue = Boolean

  val parameterType = ParameterType.Boolean

  private[parameters] def replicate: Parameter = copy()

  override protected def defaultValueToJson(defaultValue: Boolean) = defaultValue.toJson

  override protected def definedValueToJson(definedValue: Boolean): JsValue = definedValue.toJson

  override protected def valueFromDefinedJson(jsValue: JsValue): Boolean = {
    jsValue.convertTo[Boolean]
  }
}

case class NumericParameter(
    description: String,
    default: Option[Double],
    validator: Validator[Double],
    var _value: Option[Double] = None)
  extends Parameter
  with HasValidator
  with CanHaveDefault {

  type HeldValue = Double

  val parameterType = ParameterType.Numeric

  private[parameters] def replicate: Parameter = copy()

  override protected def defaultValueToJson(defaultValue: Double): JsValue = defaultValue.toJson

  override protected def definedValueToJson(definedValue: Double): JsValue = definedValue.toJson

  override protected def valueFromDefinedJson(jsValue: JsValue): Double = {
    jsValue.convertTo[Double]
  }
}

case class StringParameter(
    description: String,
    default: Option[String],
    validator: Validator[String],
    var _value: Option[String] = None)
  extends Parameter
  with HasValidator
  with CanHaveDefault {

  type HeldValue = String

  val parameterType = ParameterType.String

  private[parameters] def replicate: Parameter = copy()

  /**
   * Substitutes placeholders with concrete variable values.
   *
   * E.g. "${username}" will be replaced with value of variable named "username".
   *
   * @param variables mapping from variable names to values
   */
  override private[parameters] def substitutePlaceholders(variables: Map[String, String]): Unit = {
    maybeValue match {
      case Some(v) =>
        val placeholderRegex = new Regex("\\$\\{(.*?)\\}", "name")
        value = placeholderRegex.replaceAllIn(v, m => Regex.quoteReplacement {
          val variableName = m.group("name")
          variables.getOrElse(variableName, throw VariableNotDefinedException(variableName))
        })
      case None =>
    }
  }

  override protected def defaultValueToJson(defaultValue: String): JsValue = defaultValue.toJson

  override protected def definedValueToJson(definedValue: String): JsValue = definedValue.toJson

  override protected def valueFromDefinedJson(jsValue: JsValue): String = {
    jsValue.convertTo[String]
  }
}

/**
 * Holds choice parameter - its possible values and chosen value.
 * Its value is label pointing to one of possible choice values.
 * @param default label of option selected by default
 * @param options possible choices - their labels and schemas
 */
case class ChoiceParameter(
    description: String,
    default: Option[String],
    options: ListMap[String, ParametersSchema],
    var _value: Option[String] = None)
  extends Parameter
  with HasOptions
  with CanHaveDefault {

  type HeldValue = String

  val parameterType = ParameterType.Choice

  override protected def validateDefined(definedValue: String): Vector[DeepLangException] = {
    validateChoice(definedValue)
  }

  private[parameters] def replicate: Parameter = copy()

  override protected def defaultValueToJson(defaultValue: String): JsValue = defaultValue.toJson

  override protected def definedValueToJson(definedValue: String): JsValue = {
    JsObject(choiceToJson(definedValue))
  }

  /**
   * Side effect of this function is filling selected schemas with corresponding json.
   */
  override protected def valueFromDefinedJson(jsValue: JsValue): String = jsValue match {
    case JsObject(map) =>
      if (map.size != 1) {
        throw new DeserializationException(s"There should be only one selected option in choice" +
          s"parameter, but there are ${map.size} in $jsValue.")
      }
      val (label, innerJsValue) = map.iterator.next()
      choiceFromJson(label, innerJsValue)
      label
    case _ => throw new DeserializationException(s"Cannot fill choice parameter with $jsValue:" +
      s"object expected.")
  }

  def selection: Selection = selectionForChoice(value)
}

object ChoiceParameter {
  object BinaryChoice extends Enumeration {
    type BinaryChoice = Value
    val YES = Value("Yes")
    val NO = Value("No")
  }

  def binaryChoice(
      description: String,
      default: Option[String],
      yesSchema: ParametersSchema,
      noSchema: ParametersSchema): ChoiceParameter = {

    ChoiceParameter(
      description,
      default,
      options = ListMap(
        BinaryChoice.YES.toString -> yesSchema,
        BinaryChoice.NO.toString -> noSchema
      )
    )
  }
}

/**
 * Holds multiple choice parameter - its possible values and chosen values.
 * Its value is a set of chosen labels.
 * @param default labels of options selected by default
 * @param options possible choices - their labels and schemas
 */
case class MultipleChoiceParameter(
    description: String,
    default: Option[Traversable[String]],
    options: ListMap[String, ParametersSchema],
    var _value: Option[Traversable[String]] = None)
  extends Parameter
  with HasOptions
  with CanHaveDefault {

  type HeldValue = Traversable[String]

  val parameterType = ParameterType.MultipleChoice

  override protected def validateDefined(
      definedValue: Traversable[String]): Vector[DeepLangException] = {
    definedValue.flatMap(choice => validateChoice(choice)).toVector
  }

  private[parameters] def replicate: Parameter = copy()

  override protected def defaultValueToJson(defaultValue: Traversable[String]): JsValue = {
    defaultValue.toList.toJson
  }

  override protected def definedValueToJson(definedValue: Traversable[String]): JsValue = {
    val fields = for (choice <- definedValue.toSeq) yield choiceToJson(choice)
    JsObject(fields: _*)
  }

  /**
   * Side effect of this function is filling selected schemas with corresponding json.
   */
  override protected def valueFromDefinedJson(jsValue: JsValue): Traversable[String] = {
    jsValue match {
      case JsObject(map) =>
        for ((label, innerJsValue) <- map) yield {
          choiceFromJson(label, innerJsValue)
          label
        }
      case _ => throw new DeserializationException(s"Cannot fill multiple choice parameter" +
        s"with $jsValue: object expected.")
    }
  }

  def selections: Traversable[Selection] = {
    value.map(selectionForChoice)
  }
}

/**
 * Value of this parameter is list of filled schemas which all conform to the predefined schema.
 * @param predefinedSchema predefined schema that all schemas in value should conform to
 */
case class ParametersSequence(
    description: String,
    predefinedSchema: ParametersSchema,
    var _value: Option[Vector[ParametersSchema]] = None)
  extends Parameter {
  type HeldValue = Vector[ParametersSchema]

  val parameterType = ParameterType.Multiplier

  def replicateSchema: ParametersSchema = predefinedSchema.replicate

  /**
   * Validates each filled schema.
   * Does not check if all filled schemas conform to predefined schema.
   */
  override protected def validateDefined(
      definedValue: Vector[ParametersSchema]): Vector[DeepLangException] = {
    definedValue.foldLeft(Vector.empty[DeepLangException])(
      (allErrors, currentErrors ) => allErrors ++ currentErrors.validate)
  }

  private[parameters] def replicate: Parameter = copy()

  override private[parameters] def substitutePlaceholders(variables: Map[String, String]): Unit =
    _value.foreach(_.foreach(_.substitutePlaceholders(variables)))

  override def jsDescription: Map[String, JsValue] =
    super.jsDescription + ("values" -> predefinedSchema.toJson)

  override protected def definedValueToJson(definedValue: Vector[ParametersSchema]): JsValue = {
    val fields = for (schema <- definedValue) yield schema.valueToJson
    JsArray(fields: _*)
  }

  /**
   * Side effect of this function is creating schemas conforming to predefined schema
   * and filling them with corresponding json.
   */
  override protected def valueFromDefinedJson(jsValue: JsValue): Vector[ParametersSchema] = {
    jsValue match {
      case JsArray(vector) =>
        for (innerJsValue <- vector) yield {
          val replicatedSchema = predefinedSchema.replicate
          replicatedSchema.fillValuesWithJson(innerJsValue)
          replicatedSchema
        }
      case _ => throw new DeserializationException(s"Cannot fill parameters sequence" +
        s"with $jsValue: array expected.")
    }
  }
}

/**
 * Abstract parameter that allows to select columns.
 */
abstract sealed class AbstractColumnSelectorParameter extends Parameter {
  val parameterType = ParameterType.ColumnSelector

  /** Tells if this selectors selects single column or many. */
  protected val isSingle: Boolean

  /** Input port index of the data source. */
  protected val portIndex: Int

  override def jsDescription: Map[String, JsValue] =
    super.jsDescription + ("isSingle" -> isSingle.toJson, "portIndex" -> portIndex.toJson)
}

/**
 * Holds value that points single column.
 */
case class SingleColumnSelectorParameter(
    description: String,
    portIndex: Int,
    var _value: Option[SingleColumnSelection] = None)
  extends AbstractColumnSelectorParameter {
  type HeldValue = SingleColumnSelection

  protected val isSingle = true

  private[parameters] def replicate: Parameter = copy()

  override protected def definedValueToJson(definedValue: SingleColumnSelection): JsValue = {
    definedValue.toJson
  }

  override protected def valueFromDefinedJson(jsValue: JsValue): SingleColumnSelection = {
    SingleColumnSelection.fromJson(jsValue)
  }
}

/**
 * Holds value that points to multiple columns.
 */
case class ColumnSelectorParameter(
    description: String,
    portIndex: Int,
    var _value: Option[MultipleColumnSelection] = None,
    default: Option[MultipleColumnSelection] = None)
  extends AbstractColumnSelectorParameter
  with CanHaveDefault {
  type HeldValue = MultipleColumnSelection
  protected val isSingle = false

  private[parameters] def replicate: Parameter = copy()

  override protected def definedValueToJson(definedValue: MultipleColumnSelection): JsValue = {
    val fields = definedValue.selections.map(_.toJson)
    JsObject(
      MultipleColumnSelection.selectionsField -> JsArray(fields: _*),
      MultipleColumnSelection.excludingField -> JsBoolean(definedValue.excluding)
    )
  }

  override protected def valueFromDefinedJson(jsValue: JsValue): MultipleColumnSelection = {
    MultipleColumnSelection.fromJson(jsValue)
  }

  override protected def validateDefined(definedValue: HeldValue): Vector[DeepLangException] = {
    definedValue.validate
  }

  override protected def defaultValueToJson(defaultValue: MultipleColumnSelection): JsValue = {
    definedValueToJson(defaultValue)
  }
}

/**
 * Holds the name of a column generated by an operation.
 */
case class SingleColumnCreatorParameter(
    description: String,
    default: Option[String],
    var _value: Option[String] = None)
  extends Parameter
  with CanHaveDefault {

  type HeldValue = String

  val parameterType = ParameterType.SingleColumnCreator

  private[parameters] def replicate: Parameter = copy()

  override protected def defaultValueToJson(defaultValue: String): JsValue = defaultValue.toJson

  override protected def definedValueToJson(definedValue: String): JsValue = definedValue.toJson

  override protected def valueFromDefinedJson(jsValue: JsValue): String = {
    jsValue.convertTo[String]
  }
}

/**
 * Holds the names of columns generated by an operation.
 */
case class MultipleColumnCreatorParameter(
    description: String,
    default: Option[Vector[String]],
    var _value: Option[Vector[String]] = None)
  extends Parameter
  with CanHaveDefault {

  type HeldValue = Vector[String]

  val parameterType = ParameterType.MultipleColumnCreator

  private[parameters] def replicate: Parameter = copy()

  override protected def defaultValueToJson(defaultValue: Vector[String]):
    JsValue = defaultValue.toJson

  override protected def definedValueToJson(definedValue: Vector[String]):
    JsValue = definedValue.toJson

  override protected def valueFromDefinedJson(jsValue: JsValue): Vector[String] = {
    jsValue.convertTo[Vector[String]]
  }
}

/**
 * Holds the prefix prepended to generated columns' names.
 */
case class PrefixBasedColumnCreatorParameter(
    description: String,
    default: Option[String],
    var _value: Option[String] = None)
  extends Parameter
  with CanHaveDefault {

  type HeldValue = String

  val parameterType = ParameterType.PrefixBasedColumnCreator

  private[parameters] def replicate: Parameter = copy()

  override protected def defaultValueToJson(defaultValue: String): JsValue = defaultValue.toJson

  override protected def definedValueToJson(definedValue: String): JsValue = definedValue.toJson

  override protected def valueFromDefinedJson(jsValue: JsValue): String = {
    jsValue.convertTo[String]
  }
}
