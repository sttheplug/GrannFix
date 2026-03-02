
# RegisterRequest


## Properties

Name | Type
------------ | -------------
`email` | string
`password` | string
`name` | string
`phoneNumber` | string
`city` | string
`area` | string

## Example

```typescript
import type { RegisterRequest } from ''

// TODO: Update the object below with actual values
const example = {
  "email": null,
  "password": null,
  "name": null,
  "phoneNumber": null,
  "city": null,
  "area": null,
} satisfies RegisterRequest

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as RegisterRequest
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


