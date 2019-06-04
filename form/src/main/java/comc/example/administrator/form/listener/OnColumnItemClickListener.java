package comc.example.administrator.form.listener;


import comc.example.administrator.form.data.column.Column;

/**
 * Created by huang on 2017/11/4.
 */

public interface OnColumnItemClickListener<T> {

    void onClick(Column<T> column, String value, T t, int position);
}
