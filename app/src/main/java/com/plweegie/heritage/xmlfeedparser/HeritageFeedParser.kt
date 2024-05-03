package com.plweegie.heritage.xmlfeedparser

import android.util.Xml
import com.plweegie.heritage.model.HeritagePlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.StringReader
import javax.inject.Inject

class HeritageFeedParser @Inject constructor(private val feedApi: FeedApi) {

    private companion object {
        const val FEED_START_TAG = "PropertyFeedViewModel"
        const val FEED_LIST_TAG = "PropertyList"
        const val PLACE_TAG = "PropertyFeedItem"
        const val TITLE_TAG = "Title"
        const val DESCRIPTION_TAG = "Description"
        const val PATH_TAG = "Path"
        const val IMAGE_PATH_TAG = "ImagePath"
        const val CATEGORY_TAG = "Category"
        const val IS_FREE_ENTRY_TAG = "IsFreeEntry"
        const val COUNTY_TAG = "County"
        const val REGION_TAG = "Region"
        const val LATITUDE_TAG = "Latitude"
        const val LONGITUDE_TAG = "Longitude"
    }

    fun parseFeed(): Flow<HeritagePlace> {
        return flow {
            val reader = StringReader(feedApi.getPlacesFeed())
            val parser = Xml.newPullParser().apply {
                setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                setInput(reader)
                nextTag()
            }
            emitAll(readFeed(parser))
        }.flowOn(Dispatchers.IO)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): Flow<HeritagePlace> {
        parser.require(XmlPullParser.START_TAG, null, FEED_START_TAG)

        return flow {
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.eventType != XmlPullParser.START_TAG) {
                    continue
                }
                if (parser.name == PLACE_TAG) {
                    emit(readEntry(parser))
                } else {
                    skip(parser)
                }
            }
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readEntry(parser: XmlPullParser): HeritagePlace {
        parser.require(XmlPullParser.START_TAG, null, PLACE_TAG)
        val place = HeritagePlace()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                TITLE_TAG -> place.title = readText(parser, TITLE_TAG)
                DESCRIPTION_TAG -> place.description = readText(parser, DESCRIPTION_TAG)
                PATH_TAG -> place.path = readText(parser, PATH_TAG)
                IMAGE_PATH_TAG -> place.imagePath = readText(parser, IMAGE_PATH_TAG)
                CATEGORY_TAG -> place.category = readText(parser, CATEGORY_TAG)
                IS_FREE_ENTRY_TAG -> place.isFreeEntry = readText(parser, IS_FREE_ENTRY_TAG).toBoolean()
                COUNTY_TAG -> place.county = readText(parser, COUNTY_TAG)
                REGION_TAG -> place.region = readText(parser, REGION_TAG)
                LATITUDE_TAG -> place.latitude = readText(parser, LATITUDE_TAG).toDouble()
                LONGITUDE_TAG -> place.longitude = readText(parser, LONGITUDE_TAG).toDouble()
                else -> skip(parser)
            }
        }
        return place
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser, startTag: String): String {
        parser.require(XmlPullParser.START_TAG, null, startTag)

        var text = ""
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.text
            parser.nextTag()
        }

        parser.require(XmlPullParser.END_TAG, null, startTag)
        return text
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}