package com.plweegie.heritage.model

data class HeritagePlace(
    var id: String?,
    var title: String?,
    var description: String?,
    var path: String?,
    var imagePath: String?,
    var category: String?,
    var isFreeEntry: Boolean?,
    var latitude: Double?,
    var longitude: Double?,
    var county: String?,
    var region: String?
) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null)
}
