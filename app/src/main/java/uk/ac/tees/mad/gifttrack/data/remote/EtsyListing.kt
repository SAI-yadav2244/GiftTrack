package uk.ac.tees.mad.gifttrack.data.remote

data class EtsyListing(
    val listing_id: Long,
    val title: String,
    val price: String?,
    val currency_code: String?,
    val url: String,
    val description: String?,
    val Images: List<EtsyImage>?
)

data class EtsyImage(val url_fullxfull: String?)

data class EtsyResponse(val results: List<EtsyListing>)