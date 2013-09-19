package com.neetomo.mahout_in_action;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

import java.io.File;

public class Recommender4_6_1 {
    public static void main(String[] args) throws Exception {
        RandomUtils.useTestSeed();
        DataModel model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(new FileDataModel(new File(args[0]))));
        RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();

        RecommenderBuilder builder = new RecommenderBuilder() {
                @Override
                public Recommender buildRecommender(DataModel model) throws TasteException {
                    UserSimilarity similarity = new LogLikelihoodSimilarity(model);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, model);
                    return new SVDRecommender(model, new ALSWRFactorizer(model, 10, 0.5, 10));
                }
            };

        DataModelBuilder modelBuilder =new DataModelBuilder() {
                @Override
                public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
                    return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(trainingData));
                }
            };

        RecommenderEvaluator absEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        double score = absEvaluator.evaluate(builder, null, model, 0.95, 0.01);
        System.out.println("score : " + score);

        RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
        IRStatistics stats = statsEvaluator.evaluate(builder, null, model, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.01);
        System.out.println("Prcision : " + stats.getPrecision());
        System.out.println("Recall : " + stats.getRecall());
    }
}
