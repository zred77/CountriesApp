# CountriesApp
An app using [restcountries.eu](http://restcountries.eu/) rest API written in kotlin language

It starts with displaying a list of countries. The list is filterable (currently just by region) through a filter panel. By clicking a list item a detail screen appears with more details about the selected country. 

Libraries/solutions used in app:

- MVVM
- LiveData
- kotlin coroutines
- Dagger 2
- Retrofit
- Moshi
- Coil

Add your own google maps api key to local.properties with the following key:
`maps.api.key`
