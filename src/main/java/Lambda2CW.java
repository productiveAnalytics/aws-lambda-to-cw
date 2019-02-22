package com.productiveAnalytics.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.RequestHandler; 
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Lambda2CW implements RequestHandler<Integer, String> {
	public String handleRequest(int i, Context ctx) {
		return "Received "+ i +". Returning "+ (i*i);
	}
}
