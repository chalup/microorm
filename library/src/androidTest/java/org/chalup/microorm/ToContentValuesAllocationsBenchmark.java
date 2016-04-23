package org.chalup.microorm;

import org.chalup.microorm.annotations.Column;
import org.junit.runner.RunWith;

import android.content.ContentValues;
import android.os.Debug;

import dk.ilios.spanner.AfterExperiment;
import dk.ilios.spanner.BeforeExperiment;
import dk.ilios.spanner.Benchmark;
import dk.ilios.spanner.CustomMeasurement;
import dk.ilios.spanner.junit.SpannerRunner;

@RunWith(SpannerRunner.class)
public class ToContentValuesAllocationsBenchmark {
  private static final int REPS = 10000;

  // Private fields used by benchmark methods
  private MicroOrm mMicroOrm;
  private TestEntity mTestEntity;

  public static class TestEntity {
    @Column("id")
    long id;

    @Column("name")
    String name;

    @Column("fieldA")
    String fieldA;

    @Column("fieldB")
    String fieldB;

    @Column("fieldC")
    String fieldC;

    @Column("email")
    String email;
  }

  @BeforeExperiment
  public void before() {
    mMicroOrm = new MicroOrm();

    mTestEntity = new TestEntity();
    mTestEntity.id = 42;
    mTestEntity.name = "Joe Shmoe";
    mTestEntity.email = "joe@shmoe.com";
    mTestEntity.fieldA = "fieldA";
    mTestEntity.fieldB = "fieldB";
    mTestEntity.fieldC = "fieldC";
  }

  @AfterExperiment
  public void after() {
  }

  @CustomMeasurement(units = "allocs")
  public double usingMicroOrm() {
    Debug.resetAllCounts();
    Debug.startAllocCounting();
    for (int i = 0; i < REPS; i++) {
      mMicroOrm.toContentValues(mTestEntity);
    }
    Debug.stopAllocCounting();
    return (double) Debug.getThreadAllocCount() / REPS;
  }

  private ContentValues toContentValues(TestEntity entity) {
    ContentValues values = new ContentValues(6);

    values.put("id", entity.id);
    values.put("name", entity.name);
    values.put("email", entity.email);
    values.put("fieldA", entity.fieldA);
    values.put("fieldB", entity.fieldB);
    values.put("fieldC", entity.fieldC);

    return values;
  }

  @CustomMeasurement(units = "allocs")
  public double usingHandRolledMethod() {
    Debug.resetAllCounts();
    Debug.startAllocCounting();
    for (int i = 0; i < REPS; i++) {
      toContentValues(mTestEntity);
    }
    Debug.stopAllocCounting();
    return (double) Debug.getThreadAllocCount() / REPS;
  }
}
