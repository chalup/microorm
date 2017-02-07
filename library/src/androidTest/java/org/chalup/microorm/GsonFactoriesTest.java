package org.chalup.microorm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.moshi.Moshi;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

@RunWith(AndroidJUnit4.class)
public class GsonFactoriesTest {
  @Test
  public void shouldProperlyDeserializeDrawingUsingJsonTree() throws Exception {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Shape.class, new JsonTreeAdapter())
        .create();

    Drawing drawingFromJson = gson.fromJson(GsonFactoriesKt.getJson(), Drawing.class);
    Assert.assertEquals(GsonFactoriesKt.getExpectedDrawing(), drawingFromJson);
  }

  @Test
  public void shouldProperlyDeserializeDrawingUsingStreaming() throws Exception {
    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new StreamingShapeFactory())
        .create();

    Drawing drawingFromJson = gson.fromJson(GsonFactoriesKt.getJson(), Drawing.class);
    Assert.assertEquals(GsonFactoriesKt.getExpectedDrawing(), drawingFromJson);
  }

  @Test
  public void shouldProperlyDeserializeDrawingUsingMoshi() throws Exception {
    Moshi moshi = new Moshi.Builder()
        .add(new MoshiShapeFactory())
        .build();

    Drawing drawingFromJson = moshi.adapter(Drawing.class).fromJson(GsonFactoriesKt.getJson());
    Assert.assertEquals(GsonFactoriesKt.getExpectedDrawing(), drawingFromJson);
  }
}