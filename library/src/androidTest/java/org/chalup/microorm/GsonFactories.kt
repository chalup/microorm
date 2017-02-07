package org.chalup.microorm

import com.google.gson.*
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.StringReader
import java.io.StringWriter
import java.lang.reflect.Type

val json = "{\"bottomShape\":{\"payload\":{\"width\":10,\"height\":5,\"x\":0,\"y\":0},\"type\":\"Diamond\"},\"topShape\":{\"type\":\"Circle\",\"payload\":{\"radius\":2,\"x\":4,\"y\":1}}}"
val expectedDrawing = Drawing(bottomShape = Diamond(0, 0, 10, 5), topShape = Circle(4, 1, 2))

abstract class Shape(val x: Int, val y: Int)

class Circle(x: Int, y: Int, val radius: Int) : Shape(x, y) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other?.javaClass != javaClass) return false

    other as Circle

    if (radius != other.radius) return false
    if (x != other.x) return false
    if (y != other.y) return false

    return true
  }

  override fun hashCode(): Int {
    var result = radius
    result = 31 * result + x
    result = 31 * result + y
    return result
  }
}

class Rectangle(x: Int, y: Int, val width: Int, val height: Int) : Shape(x, y) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other?.javaClass != javaClass) return false

    other as Rectangle

    if (width != other.width) return false
    if (height != other.height) return false
    if (x != other.x) return false
    if (y != other.y) return false

    return true
  }

  override fun hashCode(): Int {
    var result = width
    result = 31 * result + height
    result = 31 * result + x
    result = 31 * result + y
    return result
  }
}
class Diamond(x: Int, y: Int, val width: Int, val height: Int) : Shape(x, y) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other?.javaClass != javaClass) return false

    other as Diamond

    if (width != other.width) return false
    if (height != other.height) return false
    if (x != other.x) return false
    if (y != other.y) return false

    return true
  }

  override fun hashCode(): Int {
    var result = width
    result = 31 * result + height
    result = 31 * result + x
    result = 31 * result + y
    return result
  }
}

data class Drawing(val bottomShape: Shape, val topShape: Shape)

class JsonTreeAdapter : JsonDeserializer<Shape> {
  override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Shape {
    val type = json.asJsonObject.getAsJsonPrimitive("type").asString

    val shapeClass = when (type) {
      "Diamond" -> Diamond::class.java
      "Rectangle" -> Rectangle::class.java
      "Circle" -> Circle::class.java
      else -> throw IllegalArgumentException("unknown type")
    }

    return context.deserialize(json.asJsonObject["payload"], shapeClass)
  }
}

class StreamingShapeFactory : TypeAdapterFactory {
  override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
    if (type.rawType != Shape::class.java) return null

    val delegates = mapOf(
        "Diamond" to gson.getDelegateAdapter(this, TypeToken.get(Diamond::class.java)),
        "Rectangle" to gson.getDelegateAdapter(this, TypeToken.get(Rectangle::class.java)),
        "Circle" to gson.getDelegateAdapter(this, TypeToken.get(Circle::class.java))
    )

    val adapter = object : TypeAdapter<Shape>() {
      override fun read(reader: JsonReader): Shape {
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

        return delegates[payloadType!!]!!.read(payload!!)
      }

      override fun write(out: JsonWriter, value: Shape?) = TODO()
    }

    @Suppress("UNCHECKED_CAST")
    return adapter as TypeAdapter<T>
  }
}

fun JsonReader.nextReader(): JsonReader {
  val stringWriter = StringWriter()
  val writer = JsonWriter(stringWriter)

  var depth = 0
  do {
    when (peek()!!) {
      JsonToken.BEGIN_ARRAY -> {
        depth++
        this.beginArray()
        writer.beginArray()
      }
      JsonToken.END_ARRAY -> {
        depth--
        this.endArray()
        writer.endArray()
      }
      JsonToken.BEGIN_OBJECT -> {
        depth++
        this.beginObject()
        writer.beginObject()
      }
      JsonToken.END_OBJECT -> {
        depth--
        this.endObject()
        writer.endObject()
      }
      JsonToken.NAME -> writer.name(this.nextName())
      JsonToken.STRING -> writer.value(this.nextString())
      JsonToken.NUMBER -> {
        try {
          writer.value(this.nextLong())
        } catch (ignored: Exception) {
          writer.value(this.nextDouble())
        }
      }
      JsonToken.BOOLEAN -> writer.value(this.nextBoolean())
      JsonToken.NULL -> {
        this.nextNull()
        writer.nullValue()
      }
      JsonToken.END_DOCUMENT -> throw IllegalArgumentException("Unexpected end of document")
    }

  } while (depth != 0)

  return JsonReader(StringReader(stringWriter.toString()))
}
