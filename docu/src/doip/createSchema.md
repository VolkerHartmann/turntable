#### Create a Schema/Application Profile

While creating a schema/AP the DO has to contain
two elements:
1. Serialization of metadata ([DataCite Schema(JSON)](../datacite/datacite.md#metadata-of-a-schema-document--application-profile))
   - id = datacite
   - type = application/json
2. Serialization of schema/AP
   - id = application_profile | schema
   - type = application/ld+json | application/json | application/xml

##### (DataCite) Metadata
The dataCite metadata for creating a DO of a schema/application profile must contain the following fields:
- titles
- publisher
- formats

Some fields are filled by server. See also [here](../datacite/datacite.md#metadata-for-creating-a-schema-document--application-profile) 
for further information.
 
###### formats
Defines the format of the schema
  - application/ld+json -> Application Profile
  - application/json    -> JSON Schema
  - application/xml     -> XSD
  
