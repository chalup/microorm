package com.chalup.microorm.tests;

import android.content.ContentValues;
import android.database.Cursor;
import com.chalup.microorm.MicroOrm;
import com.chalup.microorm.annotations.Column;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentValues;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SuperclassTest {

  private static final long TEST_ID = 5L;
  private static final String TEST_NAME = "Joseph";


  private static final String COLUMN_ID = "id";
  private static final String COLUMN_NAME = "name";

  private MicroOrm testSubject;
  private Cursor cursorMock;

  @Before
  public void setUp() throws Exception {
    testSubject = new MicroOrm();
    cursorMock = mock(Cursor.class);

    when(cursorMock.getColumnIndex(COLUMN_ID)).thenReturn(0);
    when(cursorMock.getColumnIndexOrThrow(COLUMN_ID)).thenReturn(0);
    when(cursorMock.getLong(0)).thenReturn(TEST_ID);

    when(cursorMock.getColumnIndex(COLUMN_NAME)).thenReturn(1);
    when(cursorMock.getColumnIndexOrThrow(COLUMN_NAME)).thenReturn(1);
    when(cursorMock.getString(1)).thenReturn(TEST_NAME);
  }

  @Test
  public void shouldPutSuperclassFieldsToContentValues() throws Exception {
    final SampleModel model = new SampleModel();
    model.setId(5L);
    model.setName(TEST_NAME);
    final ContentValues values = testSubject.toContentValues(model);
    ShadowContentValues shadowValues = Robolectric.shadowOf(values);
    assertThat(shadowValues.getAsString(COLUMN_NAME)).isEqualTo(TEST_NAME);
    assertThat(shadowValues.getAsLong(COLUMN_ID)).isEqualTo(5L);
  }

  @Test
  public void shouldSetValueToSuperclassFields() throws Exception {
    final SampleModel model = testSubject.fromCursor(cursorMock, SampleModel.class);
    assertThat(model.getId()).isEqualTo(TEST_ID);
    assertThat(model.getName()).isEqualTo(TEST_NAME);
  }

  public static class BaseClass {

    @Column(COLUMN_ID)
    private long id;

    public void setId(long id) {
      this.id = id;
    }

    public long getId() {
      return id;
    }
  }

  public static class SampleModel extends BaseClass {

    @Column(COLUMN_NAME)
    private String name;

    private void setName(String name) {
      this.name = name;
    }

    private String getName() {
      return name;
    }
  }
}
