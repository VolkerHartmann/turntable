## Create a Schema/Application Profile

While creating a schema/AP the DO has to contain
two elements:
1. Serialization of metadata ([DataCite (JSON)](../datacite/schema.md))
   - id = datacite
   - type = application/json   
2. Serialization of schema/AP
   - id = application_profile | schema
   - type = application/ld+json | application/json | application/xml

### Metadata
The dataCite metadata must contain the following fields:
#### formats
Defines the format of the schema
  - application/ls+json -> Application Profile
  - application/json    -> JSON Schema
  - application/xml     -> XSD
  