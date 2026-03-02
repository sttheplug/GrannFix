
# AdminUserDto


## Properties

Name | Type
------------ | -------------
`id` | string
`name` | string
`email` | string
`phoneNumber` | string
`city` | string
`area` | string
`active` | boolean
`verified` | boolean
`role` | string
`ratingAverage` | number
`ratingCount` | number
`createdAt` | Date
`updatedAt` | Date

## Example

```typescript
import type { AdminUserDto } from ''

// TODO: Update the object below with actual values
const example = {
  "id": null,
  "name": null,
  "email": null,
  "phoneNumber": null,
  "city": null,
  "area": null,
  "active": null,
  "verified": null,
  "role": null,
  "ratingAverage": null,
  "ratingCount": null,
  "createdAt": null,
  "updatedAt": null,
} satisfies AdminUserDto

console.log(example)

// Convert the instance to a JSON string
const exampleJSON: string = JSON.stringify(example)
console.log(exampleJSON)

// Parse the JSON string back to an object
const exampleParsed = JSON.parse(exampleJSON) as AdminUserDto
console.log(exampleParsed)
```

[[Back to top]](#) [[Back to API list]](../README.md#api-endpoints) [[Back to Model list]](../README.md#models) [[Back to README]](../README.md)


