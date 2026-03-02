# OfferControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createOffer**](OfferControllerApi.md#createofferoperation) | **POST** /offers/task/{taskId} |  |



## createOffer

> OfferResponse createOffer(taskId, createOfferRequest)



### Example

```ts
import {
  Configuration,
  OfferControllerApi,
} from '';
import type { CreateOfferOperationRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const config = new Configuration({ 
    // Configure HTTP bearer authorization: bearerAuth
    accessToken: "YOUR BEARER TOKEN",
  });
  const api = new OfferControllerApi(config);

  const body = {
    // string
    taskId: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
    // CreateOfferRequest
    createOfferRequest: ...,
  } satisfies CreateOfferOperationRequest;

  try {
    const data = await api.createOffer(body);
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

// Run the test
example().catch(console.error);
```

### Parameters


| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **taskId** | `string` |  | [Defaults to `undefined`] |
| **createOfferRequest** | [CreateOfferRequest](CreateOfferRequest.md) |  | |

### Return type

[**OfferResponse**](OfferResponse.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: `application/json`
- **Accept**: `*/*`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

