package com.neetomo.mahout_in_action;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

import java.util.List;
import java.io.File;


public class Recommender2 {
    public static void main(String[] args) throws Exception {
        RandomUtils.useTestSeed();
        DataModel model = new FileDataModel(new File(args[0]));
        RecommenderBuilder builder = new RecommenderBuilder() {
                @Override
                public Recommender buildRecommender(DataModel model) throws TasteException {
                    //UserSimilarity similarity = new PearsonCorrelationSimilarity(model, Weighting.WEIGHTED);
                    //UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
                    UserSimilarity similarity = new LogLikelihoodSimilarity(model);
                    UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, model);
                    return new GenericUserBasedRecommender(model, neighborhood, similarity);
                }
            };

        Recommender recommender = builder.buildRecommender(model);
        List<RecommendedItem> recommendations = recommender.recommend(1, 5);
        for(RecommendedItem item : recommendations) {
            System.out.println("recommend : " + item);
        }


        RecommenderEvaluator absEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        double score = absEvaluator.evaluate(builder, null, model, 0.95, 0.01);
        System.out.println("score : " + score);

        RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
        IRStatistics stats = statsEvaluator.evaluate(builder, null, model, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.01);
        System.out.println("Prcision : " + stats.getPrecision());
        System.out.println("Recall : " + stats.getRecall());

    }
}
