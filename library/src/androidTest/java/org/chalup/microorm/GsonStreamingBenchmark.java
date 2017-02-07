package org.chalup.microorm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.runner.RunWith;

import java.io.IOException;

import dk.ilios.spanner.AfterExperiment;
import dk.ilios.spanner.BeforeExperiment;
import dk.ilios.spanner.Benchmark;
import dk.ilios.spanner.junit.SpannerRunner;

@RunWith(SpannerRunner.class)
public class GsonStreamingBenchmark {
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

  @Benchmark
  public void usingGsonStreaming(int reps) {
    for (int i = 0; i < reps; i++) {
      mStreamingGson.fromJson(GsonFactoriesKt.getJson(), Drawing.class);
    }
  }

  @Benchmark
  public void usingGsonJsonTree(int reps) {
    for (int i = 0; i < reps; i++) {
      mJsonTreeGson.fromJson(GsonFactoriesKt.getJson(), Drawing.class);
    }
  }

  @Benchmark
  public void usingMoshi(int reps) throws IOException {
    JsonAdapter<Drawing> adapter = mMoshi.adapter(Drawing.class);

    for (int i = 0; i < reps; i++) {
      adapter.fromJson(GsonFactoriesKt.getJson());
    }
  }
}
