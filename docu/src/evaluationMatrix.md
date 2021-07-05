## Evaluation Matrix

Status: 08.06.2021

| Function              | [AIMS](aims/atAGlance.md) | [Ckan](ckan/atAGlance.md) | [Cordra](cordra/atAGlance.md) | [Coscine](coscine/atAGlance.md) | [DSpace](dspace/atAGlance.md) | [Fedora](fedora/atAGlance.md) | [Gerdi](gerdi/atAGlance.md) |[Invenio](invenio/atAGlance.md) | [MetaStore](metastore/atAGlance.md) | [OSF](openScienceFramework/atAGlance.md) | [Zenodo](zenodo/atAGlance.md) |
| ---------------------:| :-----------------------: | :-----------------------: | :---------------------------: | :-----------------------------: | :---------------------------: | :---------------------------: | :-------------------------: | :----------------------------: | :---------------------------------: | :--------------------------------------: | :---------------------------: |
| Register Schema       | +                         | -                         | (+)[^1]                       | +                               |  -                            | -                             | -                           | -                              | +                                   | -                                        | -                             | 
| Update Schema         | +                         | -                         | (+)[^2]                       | +                               |  -                            | -                             | -                           | -                              | +                                   | -                                        | -                             | 
| Validate Schema       | (+)[^3]                   | -                         |  -                            | (+)[^3]                         |  -                            | -                             | -                           | -                              | +                                   | -                                        | -                             | 
| Ingest Metadata       | +                         | +                         |  +                            | +                               |  +                            | +                             | +                           | +                              | +                                   | +                                        | +                             | 
| Update Metadata       |                           | +                         |  +                            | +                               |  +                            | +                             | +                           | +                              | +                                   | +                                        | +                             | 
| Validate Metadata     | +                         | (+)[^4]                   |  +                            | +                               |  -                            | -                             | +                           | -                              | +                                   | -                                        | -                             | 
| Search by ...         |                           |                           |                               |                                 |                               |                               |                             |                                |                                     |                                          |                               | 
| ... Administrative MD | +                         | +                         |  +                            | +                               |  +                            | +                             | +                           | +                              | +                                   | +                                        | +                             | 
| ... Content           |                           | (+)[^5]                   |  +                            | (+)[^6]                         |  -                            | +                             | +                           | -                              | (+)[^7]                             | -                                        | -                             | 
| Persistent Identifier | (+)                       | +                         |  +                            | +                               |  +                            | +                             | +                           | +                              | +[^8]                               | +                                        | +                             | 

For more detailed information please use the links in the table header.

### Description 
- **Register Schema:**
  - Support for arbitrary schemas of a specific format (e.g. JSON Schema, XSD)
  - The schema should at least be referencable by a unique identifier.
- **Update Schema:**
  - Possibility to 
    - work on different versions of a schema 
    - adapt schemas over time
- **Validate Schema:**
  - Check schema for correct syntax
- **Ingest Metadata:**
  - Store metadata (document) in repository
    - Ideally with previous validation 
- **Update Metadata:** 
  - Possibility to update already ingested metadata (documents).
- **Validate Metadata:**
  - Possibility to validate documents on the basis of registered schemas.
- **Search by Administrative MD:**
  - Search documents by their metadata (e.g. ingest date, ingester, ...)
- **Search by Content:**
  - Search documents by their content
- **Persistent Identifier:**
  - Support for Persistent Identifiers (e.g. DOI, Handle)

---
**Footnotes**

[^1]: Support for arbitrary (JSON Schema) via configuration only (-> restricted to administrators)

[^2]: Manually by administrators

[^3]: Syntax check

[^4]: Via 'scheming' plugin with a rudimentary schema format

[^5]: Via 'DataStore' plugin (supports SQL queries)

[^6]: Planned

[^7]: Via enhanced service

[^8]: PID via enhanced service


