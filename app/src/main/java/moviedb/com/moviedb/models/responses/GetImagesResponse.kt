package moviedb.com.moviedb.models.responses

import moviedb.com.moviedb.data.pojos.CelebrityImageEntity

data class GetImagesResponse(
    val profiles:ArrayList<CelebrityImageEntity>
)