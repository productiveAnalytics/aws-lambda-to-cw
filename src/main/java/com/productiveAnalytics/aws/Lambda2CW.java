package com.productiveAnalytics.aws;

import java.util.concurrent.CompletableFuture;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;
import software.amazon.awssdk.services.cloudwatch.model.MetricDatum;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataRequest;
import software.amazon.awssdk.services.cloudwatch.model.PutMetricDataResponse;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;



public class Lambda2CW implements RequestHandler<String, String> {
	public String handleRequest(String str, Context ctx) {
		LambdaLogger lambdaLogger = ctx.getLogger();
		
		lambdaLogger.log("Received request : "+ str);
		
		int i = Integer.parseInt(str);
		
		// limit within 0-100 for simplicity
		if (i < 0) {
			i = 0;
		}
		else if (i >= 100) {
			i = 100;
		}
		
		Double data_point = Double.parseDouble("99.89");

		CloudWatchAsyncClient cw = CloudWatchAsyncClient.builder().build();

        Dimension dimension = Dimension.builder()
            .name("UNIQUE_PAGES")
            .value("URLS")
            .build();

        MetricDatum datum = MetricDatum.builder()
            .metricName("PAGES_VISITED")
            .unit(StandardUnit.COUNT)
            .value(data_point)
            .dimensions(dimension)
            .build();

        PutMetricDataRequest request = PutMetricDataRequest.builder()
            .namespace("SITE/TRAFFIC")
            .metricData(datum)
            .build();

        CompletableFuture<PutMetricDataResponse> futurePutMetricResponse = cw.putMetricData(request);
        futurePutMetricResponse.whenComplete((item, err)
        									 -> {
        										try {
        										  System.out.println(item.toString());	
        										} finally {
        										  cw.close();	
        										}
        									 }
        									);

        System.out.printf("Successfully put data point %f", data_point);
		
		
		return "Received "+ i +". Returning "+ (i*i);
	}
}
