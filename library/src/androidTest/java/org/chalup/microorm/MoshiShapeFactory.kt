package org.chalup.microorm

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import okio.Buffer
import java.lang.reflect.Type

class MoshiShapeFactory : JsonAdapter.Factory {
  override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
    if (type != Shape::class.java) return null

    val delegates = mapOf(
        "Diamond" to moshi.nextAdapter<Diamond>(this, Diamond::class.java, emptySet()),
        "Rectangle" to moshi.nextAdapter<Rectangle>(this, Rectangle::class.java, emptySet()),
        "Circle" to moshi.nextAdapter<Circle>(this, Circle::class.java, emptySet())
    )

    return object : JsonAdapter<Shape>() {
      override fun toJson(writer: JsonWriter, value: Shape) = TODO()
      override fun fromJson(reader: JsonReader): Shape? {
        var payloadType: String? = null
        var payload: JsonReader? = null

        reader.beginObject()
        while (reader.hasNext()) {
          when (reader.nextName()) {
            "type" -> payloadType = reader.nextString()
            "payload" -> payload = reader.nextReader()
            else -> reader.skipValue()
          }
        }
        reader.endObject()

        return delegates[payloadType!!]!!.fromJson(payload!!)
      }
    }
  }
}

fun JsonReader.nextReader() : JsonReader {
  val buffer = Buffer()
  val writer = JsonWriter.of(buffer)

  var depth = 0
  do {
    when (peek()!!) {
      JsonReader.Token.BEGIN_ARRAY -> {
        depth++
        this.beginArray()
        writer.beginArray()
      }
      JsonReader.Token.END_ARRAY -> {
        depth--
        this.endArray()
        writer.endArray()
      }
      JsonReader.Token.BEGIN_OBJECT -> {
        depth++
        this.beginObject()
        writer.beginObject()
      }
      JsonReader.Token.END_OBJECT -> {
        depth--
        this.endObject()
        writer.endObject()
      }
      JsonReader.Token.NAME -> writer.name(this.nextName())
      JsonReader.Token.STRING -> writer.value(this.nextString())
      JsonReader.Token.NUMBER -> {
        try {
          writer.value(this.nextLong())
        } catch (ignored: Exception) {
          writer.value(this.nextDouble())
        }
      }
      JsonReader.Token.BOOLEAN -> writer.value(this.nextBoolean())
      JsonReader.Token.NULL -> {
        this.nextNull<Void>()
        writer.nullValue()
      }
      JsonReader.Token.END_DOCUMENT -> throw IllegalArgumentException("Unexpected end of document")
    }

  } while (depth != 0)

  return JsonReader.of(buffer)
}
