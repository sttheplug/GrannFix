# AdminUserControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**deactivateUser**](AdminUserControllerApi.md#deactivateuser) | **POST** /admin/users/{id} |  |
| [**getAllUsers**](AdminUserControllerApi.md#getallusers) | **GET** /admin/users |  |
| [**reactivateUser**](AdminUserControllerApi.md#reactivateuser) | **POST** /admin/users/{id}:reactivate |  |



## deactivateUser

> deactivateUser(id)



### Example

```ts
import {
  Configuration,
  AdminUserControllerApi,
} from '';
import type { DeactivateUserRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const config = new Configuration({ 
    // Configure HTTP bearer authorization: bearerAuth
    accessToken: "YOUR BEARER TOKEN",
  });
  const api = new AdminUserControllerApi(config);

  const body = {
    // string
    id: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
  } satisfies DeactivateUserRequest;

  try {
    const data = await api.deactivateUser(body);
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
| **id** | `string` |  | [Defaults to `undefined`] |

### Return type

`void` (Empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## getAllUsers

> PageAdminUserDto getAllUsers(pageable)



### Example

```ts
import {
  Configuration,
  AdminUserControllerApi,
} from '';
import type { GetAllUsersRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const config = new Configuration({ 
    // Configure HTTP bearer authorization: bearerAuth
    accessToken: "YOUR BEARER TOKEN",
  });
  const api = new AdminUserControllerApi(config);

  const body = {
    // Pageable
    pageable: ...,
  } satisfies GetAllUsersRequest;

  try {
    const data = await api.getAllUsers(body);
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
| **pageable** | [](.md) |  | [Defaults to `undefined`] |

### Return type

[**PageAdminUserDto**](PageAdminUserDto.md)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: `*/*`


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


## reactivateUser

> reactivateUser(id)



### Example

```ts
import {
  Configuration,
  AdminUserControllerApi,
} from '';
import type { ReactivateUserRequest } from '';

async function example() {
  console.log("🚀 Testing  SDK...");
  const config = new Configuration({ 
    // Configure HTTP bearer authorization: bearerAuth
    accessToken: "YOUR BEARER TOKEN",
  });
  const api = new AdminUserControllerApi(config);

  const body = {
    // string
    id: 38400000-8cf0-11bd-b23e-10b96e4ef00d,
  } satisfies ReactivateUserRequest;

  try {
    const data = await api.reactivateUser(body);
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
| **id** | `string` |  | [Defaults to `undefined`] |

### Return type

`void` (Empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)

