# Negative Test Validation Fix

## Problem
In the Allure report, negative test cases were showing as **PASS** when they received expected 2XX return codes (from OpenAPI spec), but showing as **FAILED** when they received 4XX or 5XX error codes.

This was backwards - negative tests should:
- ✅ **PASS** when they get 4XX or 5XX error codes (invalid inputs should be rejected)
- ❌ **FAIL** when they get 2XX success codes (invalid inputs should NOT succeed)

## Root Cause
The code was using `.statusCode(step.getExpectedStatus())` for ALL tests, which validates against the OpenAPI specification's expected status code (typically 200 for successful operations).

For negative tests with intentionally invalid inputs, we should expect error codes instead.

## Solution Implemented

### 1. Updated Status Code Validation Logic
**File:** `src/main/java/es/us/isa/restest/writers/restassured/MultiServiceRESTAssuredWriter.java`

**Change:** Modified the status code validation to conditionally check based on test type:

```java
// For negative tests, expect 4XX or 5XX error codes instead of the OpenAPI expected status
if (scenario.getFaulty()) {
    pw.println("                               // Negative test: expect error code (4XX or 5XX), not success code");
    pw.println("                               .statusCode(org.hamcrest.Matchers.greaterThanOrEqualTo(400))");
} else {
    pw.println("                               .statusCode(" + step.getExpectedStatus() + ")");
}
```

**Result:**
- **Positive tests:** Validate against OpenAPI expected status (e.g., 200)
- **Negative tests:** Validate that status code >= 400 (any 4XX or 5XX error)

### 2. Updated Step Titles in Allure Report
**Change:** Modified step titles to show correct expected status:

```java
String expectedStatusDisplay = scenario.getFaulty() ? "4XX/5XX error" : String.valueOf(step.getExpectedStatus());
String stepTitle = stepNumber + ": "
        + step.getServiceName() + " "
        + verb.toUpperCase() + " " + step.getPath()
        + " (expect " + expectedStatusDisplay + ")";
```

**Result:** Step titles now show:
- Positive tests: `Step 1: service POST /api/path (expect 200)`
- Negative tests: `Step 1: service POST /api/path (expect 4XX/5XX error)`

### 3. Added Hamcrest Matchers Import
**Change:** Added required import for status code matching:

```java
pw.println("import org.hamcrest.Matchers;");
```

## Test Behavior After Fix

### Negative Tests (with invalid inputs)
| Status Code Received | Test Result | Reason |
|---------------------|-------------|--------|
| 400, 404, 500, etc. | ✅ **PASS** | Invalid inputs correctly rejected by API |
| 200, 201, 204, etc. | ❌ **FAIL** | Invalid inputs should NOT succeed |

### Positive Tests (with valid inputs)
| Status Code Received | Test Result | Reason |
|---------------------|-------------|--------|
| Expected (e.g., 200) | ✅ **PASS** | Valid inputs correctly processed |
| Any other code | ❌ **FAIL** | Unexpected status code |

## Benefits
1. **Correct Test Semantics:** Negative tests now properly validate error handling
2. **Clear Allure Reports:** Test names show `test_negative_*` with proper status expectations
3. **Better Error Detection:** API properly rejecting invalid inputs is now a passing test
4. **Improved Debugging:** Failed negative tests indicate API accepting invalid inputs (a bug)

## Files Modified
1. `src/main/java/es/us/isa/restest/writers/restassured/MultiServiceRESTAssuredWriter.java`
   - Line ~1177-1182: Conditional status code validation
   - Line ~1033: Step title display logic
   - Line ~132: Import org.hamcrest.Matchers

## Related Changes
This fix complements the earlier terminology changes:
- Renamed test cases from `test_faulty_*` to `test_negative_*`
- Updated Allure metadata from "FAULTY" to "NEGATIVE"
- Updated validation messages to clarify expectations

