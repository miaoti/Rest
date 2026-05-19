package es.us.isa.restest.writers.restassured.filters;

import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

/**
 * REST-Assured filter to assert that negative test cases return a 4XX or 5XX status code
 * and that nominal test cases fulfilling all inter-parameter dependencies do not
 * return a 400 status code. A test case may be negative in two situations:
 * <ol>
 *     <li>If the test case was made negative on purpose (e.g., removing a required
 *     parameter or using invalid values).</li>
 *     <li>If the request body does not conform to the Swagger schema. This happens
 *     when the {@link es.us.isa.restest.inputs.perturbation.ObjectPerturbator ObjectPerturbator}
 *     mutates a valid input request body into an invalid one.</li>
 * </ol>
 */
public class NominalOrFaultyTestCaseFilter extends RESTestFilter implements OrderedFilter {

    public NominalOrFaultyTestCaseFilter() {
        super();
    }

    public NominalOrFaultyTestCaseFilter(Boolean testCaseIsFaulty, Boolean dependenciesFulfilled, String faultyReason) {
        super(testCaseIsFaulty, dependenciesFulfilled, faultyReason);
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        filterValidation(response);

        return response;
    }

    // If nominal/negative validation error is found, throw exception
    public void filterValidation(Response response) {
        if(testCaseIsFaulty != null) {
            // If test case [is negative/faulty] AND [returned status code below 400 (success codes 2XX-3XX)]
            // Negative tests should return 4XX or 5XX error codes, not success codes
            if (testCaseIsFaulty && response.getStatusCode() < 400)
                saveTestResultAndThrowException(response, "This negative test case was expecting a 4XX or 5XX error status code (" + faultyReason + "), but received a " + response.getStatusCode() + " success code. Negative tests with invalid inputs should fail, not succeed.");
            // If test case [is valid] AND [returned status code 400]
            else if (!testCaseIsFaulty && dependenciesFulfilled && response.getStatusCode() == 400)
                saveTestResultAndThrowException(response, "This test case's input was (possibly) correct, but received a 400 (Bad Request) status code.");
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE-2; // Third lowest priority of all filters, so it runs third-to-last before sending the request and third after sending it
    }
}
