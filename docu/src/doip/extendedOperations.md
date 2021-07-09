## Extended Operations

### 0.DOIP/Op.Validation
This operation validates the provided metadata document with a registered schema.
Attributes has to contain one property:
1. schema/AP - Identifier of schema/AP

DO has to contain one element with the serialization of the metadata document:
- id = document
- type= application/ld+json | application/json | application/xml

***Return***
- 0.DOIP/Status.001 - Validation was successful
- 0.DOIP/Status.xxx - An error occurred. 
