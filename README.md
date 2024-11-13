# fetch_android_eval

A simple, minimal-UI Android application that requests and displays a list of items from:
https://fetch-hiring.s3.amazonaws.com/hiring.json

- Language: Kotlin
- UI: Jetpack Compose
- Dependency injection: Hilt
- HTTP Client: OkHttp

A button at the top can be used to re-request the item list.

The lists are sorted by ID, and the items in the list can be viewed by clicking on the list's title to expand.

Individual items within the list are ordered by ID. The requirement instructed to order by item name, but based on the context of the name format I elected to sort by ID instead. This was to match a human-readable ordering while avoiding adding string processing complexity and requirements, as this additional processing would be undesirable in most realistic settings.
