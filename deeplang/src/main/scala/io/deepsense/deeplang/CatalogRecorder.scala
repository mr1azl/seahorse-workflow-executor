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

package io.deepsense.deeplang

import io.deepsense.deeplang.catalogs.doperable.DOperableCatalog
import io.deepsense.deeplang.catalogs.doperations.DOperationsCatalog
import io.deepsense.deeplang.doperables._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.file.File
import io.deepsense.deeplang.doperables.machinelearning.gradientboostedtrees.regression.{TrainedGradientBoostedTreesRegression, UntrainedGradientBoostedTreesRegression}
import io.deepsense.deeplang.doperables.machinelearning.kmeans.{TrainedKMeansClustering, UntrainedKMeansClustering}
import io.deepsense.deeplang.doperables.machinelearning.lassoregression.{TrainedLassoRegression, UntrainedLassoRegression}
import io.deepsense.deeplang.doperables.machinelearning.logisticregression.{TrainedLogisticRegression, UntrainedLogisticRegression}
import io.deepsense.deeplang.doperables.machinelearning.randomforest.classification.{TrainedRandomForestClassification, UntrainedRandomForestClassification}
import io.deepsense.deeplang.doperables.machinelearning.randomforest.regression.{TrainedRandomForestRegression, UntrainedRandomForestRegression}
import io.deepsense.deeplang.doperables.machinelearning.ridgeregression.{TrainedRidgeRegression, UntrainedRidgeRegression}
import io.deepsense.deeplang.doperables.machinelearning.svm.classification.{TrainedSupportVectorMachineClassifier, UntrainedSupportVectorMachineClassifier}
import io.deepsense.deeplang.doperables.transformations.MathematicalTransformation
import io.deepsense.deeplang.doperations._

/**
 * Object used to register all desired DOperables and DOperations.
 */
object CatalogRecorder {

  def registerDOperables(catalog: DOperableCatalog): Unit = {
    catalog.registerDOperable[File]()
    catalog.registerDOperable[DataFrame]()
    catalog.registerDOperable[Report]()
    catalog.registerDOperable[Normalizer]()

    // Regression

    catalog.registerDOperable[UntrainedRidgeRegression]()
    catalog.registerDOperable[TrainedRidgeRegression]()

    catalog.registerDOperable[UntrainedLassoRegression]()
    catalog.registerDOperable[TrainedLassoRegression]()

    catalog.registerDOperable[TrainedGradientBoostedTreesRegression]()
    catalog.registerDOperable[UntrainedGradientBoostedTreesRegression]()

    catalog.registerDOperable[UntrainedRandomForestRegression]()
    catalog.registerDOperable[TrainedRandomForestRegression]()

    // Classification

    catalog.registerDOperable[UntrainedRandomForestClassification]()
    catalog.registerDOperable[TrainedRandomForestClassification]()

    catalog.registerDOperable[UntrainedSupportVectorMachineClassifier]()
    catalog.registerDOperable[TrainedSupportVectorMachineClassifier]()

    catalog.registerDOperable[UntrainedLogisticRegression]()
    catalog.registerDOperable[TrainedLogisticRegression]()

    // Clustering

    catalog.registerDOperable[UntrainedKMeansClustering]
    catalog.registerDOperable[TrainedKMeansClustering]

    // Other

    catalog.registerDOperable[MathematicalTransformation]()
  }

  def registerDOperations(catalog: DOperationsCatalog): Unit = {

    catalog.registerDOperation[ReadDataFrame](
      DOperationCategories.IO,
      "Loads a DataFrame from a file"
    )

    catalog.registerDOperation[WriteDataFrame](
      DOperationCategories.IO,
      "Saves a DataFrame to a file"
    )

    catalog.registerDOperation[CreateMathematicalTransformation](
      DOperationCategories.Transformation,
      "Creates a Transformation that creates a new column based on a mathematical formula"
    )

    catalog.registerDOperation[Split](
      DOperationCategories.DataManipulation,
      "Splits a DataFrame into two DataFrames"
    )

    catalog.registerDOperation[Union](
      DOperationCategories.DataManipulation,
      "Creates a new DataFrame containing all rows from both input DataFrames"
    )

    catalog.registerDOperation[Join](
      DOperationCategories.DataManipulation,
      "Joins two DataFrames to a DataFrame"
    )

    catalog.registerDOperation[OneHotEncoder](
      DOperationCategories.DataManipulation,
      "One-hot encodes categorical columns of a DataFrame"
    )

    catalog.registerDOperation[FilterColumns](
      DOperationCategories.DataManipulation,
      "Creates a DataFrame containing only selected columns"
    )

    catalog.registerDOperation[DecomposeDatetime](
      DOperationCategories.DataManipulation,
      "Extracts Numeric fields (year, month, etc.) from a Timestamp"
    )

    catalog.registerDOperation[CreateRidgeRegression](
      DOperationCategories.ML.Regression,
      "Creates an untrained ridge regression model"
    )

    catalog.registerDOperation[CreateLassoRegression](
      DOperationCategories.ML.Regression,
      "Creates an untrained lasso regression model"
    )

    catalog.registerDOperation[TrainRegressor](
      DOperationCategories.ML.Regression,
      "Trains a regression model"
    )

    catalog.registerDOperation[ScoreRegressor](
      DOperationCategories.ML.Regression,
      "Scores a trained regression model"
    )

    catalog.registerDOperation[CrossValidateRegressor](
      DOperationCategories.ML.Regression,
      "Cross-validates a regression model"
    )

    catalog.registerDOperation[EvaluateRegression](
      DOperationCategories.ML.Regression,
      "Evaluates a regression model"
    )

    catalog.registerDOperation[CreateGradientBoostedTreesRegression](
      DOperationCategories.ML.Regression,
      "Creates an untrained gradient boosted trees model"
    )

    catalog.registerDOperation[CreateLogisticRegression](
      DOperationCategories.ML.Classification,
      "Creates an untrained logistic regression model"
    )

    catalog.registerDOperation[CreateRandomForestRegression](
      DOperationCategories.ML.Regression,
      "Creates an untrained random forest regression model"
    )

    catalog.registerDOperation[CreateRandomForestClassification](
      DOperationCategories.ML.Classification,
      "Creates an untrained random forest classification model"
    )

    catalog.registerDOperation[TrainClassifier](
      DOperationCategories.ML.Classification,
      "Trains a classification model"
    )

    catalog.registerDOperation[ScoreClassifier](
      DOperationCategories.ML.Classification,
      "Scores a trained classification model"
    )

    catalog.registerDOperation[CrossValidateClassifier](
      DOperationCategories.ML.Classification,
      "Cross-validates a classification model"
    )

    catalog.registerDOperation[EvaluateClassification](
      DOperationCategories.ML.Classification,
      "Evaluates a classification model"
    )

    catalog.registerDOperation[CreateSupportVectorMachineClassification](
      DOperationCategories.ML.Classification,
      "Creates an untrained SVM classification model"
    )

    catalog.registerDOperation[TrainClustering](
      DOperationCategories.ML.Clustering,
      "Trains a clustering model and assigns data to clusters"
    )

    catalog.registerDOperation[CreateKMeansClustering](
      DOperationCategories.ML.Clustering,
      "Creates an untrained k-means clustering model"
    )

    catalog.registerDOperation[AssignToClusters](
      DOperationCategories.ML.Clustering,
      "Assigns data to clusters using trained clustering model"
    )

    catalog.registerDOperation[ApplyTransformation](
      DOperationCategories.Transformation,
      "Applies a Transformation to a DataFrame"
    )

    catalog.registerDOperation[ConvertType](
      DOperationCategories.DataManipulation,
      "Converts selected columns of a DataFrame to a different type"
    )

    catalog.registerDOperation[SqlExpression](
      DOperationCategories.DataManipulation,
      "Executes an SQL expression on a DataFrame"
    )

    catalog.registerDOperation[TrainNormalizer](
      DOperationCategories.Transformation,
      "Trains Normalizer on a DataFrame"
    )

    catalog.registerDOperation[MissingValuesHandler](
      DOperationCategories.DataManipulation,
      "Handles missing values in a DataFrame"
    )
  }
}
