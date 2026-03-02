
# UpdateMeRequest


## Properties

Name | Type
------------ | -------------
`name` | string
`bio` | string
`city` | string
`area` | string
`street` | string
`email` | string

## Example

```typescript
import type { UpdateMeRequest } from ''

// TODO: Update the object below with actual values
const example = {
  "name": null,
  "bio": null,
  "city": null,
  "area": null,
  "street": null,
  "email": null,
} satisfies UpdateMeRequest

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as UpdateMeRequest
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


