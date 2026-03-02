
# Permissions


## Properties

Name | Type
------------ | -------------
`canEdit` | boolean
`canCancel` | boolean
`canOffer` | boolean
`canChat` | boolean

## Example

```typescript
import type { Permissions } from ''

// TODO: Update the object below with actual values
const example = {
  "canEdit": null,
  "canCancel": null,
  "canOffer": null,
  "canChat": null,
} satisfies Permissions

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as Permissions
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


