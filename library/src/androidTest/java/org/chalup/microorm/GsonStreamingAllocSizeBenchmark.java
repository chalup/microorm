package org.chalup.microorm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.moshi.Moshi;

import org.junit.runner.RunWith;

import android.os.Debug;

import java.io.IOException;

import dk.ilios.spanner.AfterExperiment;
import dk.ilios.spanner.BeforeExperiment;
import dk.ilios.spanner.CustomMeasurement;
import dk.ilios.spanner.junit.SpannerRunner;

@RunWith(SpannerRunner.class)
public class GsonStreamingAllocSizeBenchmark {
  private static final int REPS = 10000;

  // Private fields used by benchmark methods
  private Gson mStreamingGson;
  private Gson mJsonTreeGson;
  private Moshi mMoshi;

  @BeforeExperiment
  public void before() {
    mStreamingGson = new GsonBuilder().registerTypeAdapterFactory(new StreamingShapeFactory()).create();
    mJsonTreeGson = new GsonBuilder().registerTypeAdapter(Shape.class, new JsonTreeAdapter()).create();
    mMoshi = new Moshi.Builder().add(new MoshiShapeFactory()).build();
  }

  @AfterExperiment
  public void after() {
  }

  @CustomMeasurement(units = "alloc size")
  public double usingGsonStreaming() {
    Debug.resetAllCounts();
    Debug.startAllocCounting();
    for (int i = 0; i < REPS; i++) {
      mStreamingGson.fromJson(GsonFactoriesKt.getJson(), Drawing.class);
    }
    Debug.stopAllocCounting();
    return (double) Debug.getThreadAllocSize() / REPS;
  }

  @CustomMeasurement(units = "alloc size")
  public double usingGsonJsonTree() {
    Debug.resetAllCounts();
    Debug.startAllocCounting();
    for (int i = 0; i < REPS; i++) {
      mJsonTreeGson.fromJson(GsonFactoriesKt.getJson(), Drawing.class);
    }
    Debug.stopAllocCounting();
    return (double) Debug.getThreadAllocSize() / REPS;
  }

  @CustomMeasurement(units = "alloc size")
  public double usingMoshi() throws IOException {
    Debug.resetAllCounts();
    Debug.startAllocCounting();
    for (int i = 0; i < REPS; i++) {
      mMoshi.adapter(Drawing.class).fromJson(GsonFactoriesKt.getJson());
    }
    Debug.stopAllocCounting();
    return (double) Debug.getThreadAllocSize() / REPS;
  }
}
