package comc.example.administrator.form.data.format.sequence;

import android.graphics.Canvas;
import android.graphics.Rect;

import comc.example.administrator.form.core.TableConfig;
import comc.example.administrator.form.data.format.IFormat;

/**
 * Created by huang on 2017/11/7.
 *
 *序号格式化
 */

public interface ISequenceFormat extends IFormat<Integer> {


   void draw(Canvas canvas, int sequence, Rect rect, TableConfig config);

}
