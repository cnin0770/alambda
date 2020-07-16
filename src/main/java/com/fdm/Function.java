package com.fdm;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

public class Function {

//    @FunctionName("HttpExample")
    @FunctionName("convert")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String decQuery = request.getQueryParameters().get("dec");
        final String decStr = request.getBody().orElse(decQuery);

        if (decStr == null) return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a decimal " +
                "number on the query string or in the request body. e.g. /api/convert?dec=100").build();

        try {
            int dec = Integer.parseInt(decStr);
            String bin = Integer.toBinaryString(dec);
            return request.createResponseBuilder(HttpStatus.OK).body("Decimal " + decStr + " equals to " + bin + " in binary.").build();
        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Invalid decimal input.").build();
        }
    }
}
