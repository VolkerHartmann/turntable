## Extended Operations

### 0.DOIP/Op.Validation
This operation validates a metadata document with given schema.
Attributes has to contain one property:
1. schema/AP - Identifier of schema/AP

DO has to contain one element with the serialization of the metadata document:
- id = metadata
- type= application/ld+json | application/json | application/xml

***Return***
- 0.DOIP/Status.001 - Validation was successful
- 0.DOIP/Status.xxx - An error occurred. 
